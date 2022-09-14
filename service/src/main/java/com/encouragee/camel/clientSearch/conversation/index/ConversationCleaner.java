package com.encouragee.camel.clientSearch.conversation.index;

import com.google.common.collect.Ordering;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.ConversationResource;
import com.zoomint.encourage.model.conversation.event.DeleteEvent;
import com.zoomint.encourage.model.conversation.event.MediaEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Instant;
import java.util.*;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.collect.Sets.intersection;
import static java.util.stream.Collectors.toMap;

/**
 * Applies "garbage collection" to conversation by removing all inactive events
 * and events that reference a non-existing media resource URI (not referenced by a media event).
 * If conversation has no events, it's fully emptied and will result in deletion.
 *
 * @since 6.4
 */
@Component
@Slf4j
public class ConversationCleaner {

	/**
	 * Removes events replaced by newer events,
	 * or events that reference a non-existing media resource URI (according to existing media events).
	 *
	 * @param events events to filter
	 * @return a list of events that were removed from the provided list of all events
	 */
	@NotNull
	public List<ConversationEvent> removeInvalidEvents(List<ConversationEvent> events) {
		Map<String, Instant> eventIdInvalidatedTs = events.stream()
				.filter(event -> event.getPreviousId() != null)
				.collect(toMap(
						ConversationEvent::getPreviousId,
						event -> firstNonNull(event.getCreated(), Instant.EPOCH),
						Ordering.natural()::max));

		List<ConversationEvent> removedEvents = new ArrayList<>();
		Set<URI> mediaUris = new HashSet<>();
		boolean hasMediaEvents = false;

		Iterator<ConversationEvent> eventIt = events.iterator();
		while (eventIt.hasNext()) {
			ConversationEvent event = eventIt.next();
			Instant invalidatedTs = eventIdInvalidatedTs.get(event.getEventId());
			if (invalidatedTs != null && (event.getCreated() == null || !event.getCreated().isAfter(invalidatedTs))) {
				log.debug("Event was removed (replaced by a newer one): {}", event);
				removedEvents.add(event);
				eventIt.remove();
			} else if (event instanceof MediaEvent) {
				mediaUris.addAll(getResourceUris(event));
				hasMediaEvents = true;
			}
		}

		if (!hasMediaEvents) {
			removedEvents.addAll(events);
			events.clear();
			return removedEvents;
		}

		eventIt = events.iterator();
		while (eventIt.hasNext()) {
			ConversationEvent event = eventIt.next();
			if (event instanceof DeleteEvent) {
				log.debug("Event was removed (delete is not persisted): {}", event);
				removedEvents.add(event);
				eventIt.remove();
			} else if (!(event instanceof MediaEvent)
					&& event.getResource() != null
					&& intersection(getResourceUris(event), mediaUris).isEmpty()) {
				log.debug("Event was removed (matching media no longer part of the conversation): {}", event);
				removedEvents.add(event);
				eventIt.remove();
			}
		}

		return removedEvents;
	}

	private Set<URI> getResourceUris(ConversationEvent event) {
		Set<URI> uris = new HashSet<>();
		ConversationResource resource = event.getResource();
		while (resource != null) {
			if (resource.getUri() != null) {
				uris.add(resource.getUri());
			}
			resource = resource.getParent();
		}
		return uris;
	}
}
