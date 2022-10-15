package com.encouragee.camel.clientSearch;

import com.encouragee.ConversationAnonymizer;
import com.zoomint.encourage.common.model.label.Label;
import com.zoomint.encourage.model.conversation.*;
import com.zoomint.encourage.model.conversation.event.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Stream;

import static com.zoomint.encourage.model.conversation.ConversationEvent.EVENT_VERSIONING;

@Component
@Profile("solr")
public class ConversationModifier {

	/**
	 * Adds an event to conversation, possibly modifying other events in the conversation.
	 *
	 * @param newEvent             the event to add or update
	 * @param existingConversation conversation to modify by adding/updating the event
	 * @return list of changed events that need to be saved
	 */
	public Set<ConversationEvent> addModifyingEvent(@NotNull ConversationEvent newEvent, @NotNull Conversation existingConversation) {
		Assert.notNull(existingConversation, "conversation must be present");
		Assert.notNull(newEvent, "new event must be present");
		Assert.notNull(newEvent.getEventId(), "new event ID must be present");

		// remove event from conversation (e.g., if there's a stale version)
		int existingIndex = existingConversation.getEvents().indexOf(newEvent);
		if (existingIndex >= 0) {
			ConversationEvent existingEvent = existingConversation.getEvents().get(existingIndex);
			if (EVENT_VERSIONING.max(newEvent, existingEvent) == existingEvent) {
				// this user event was already submitted and saved,
				// but it's possible that not all other events were modified and saved successfully,
				// so, to be safe, event modifications should be executed again using the newer existing version
				return applyEventModifications(existingEvent, existingConversation.getEvents());
			}
		}
		// pass the existing event to applyEventModifications
		Set<ConversationEvent> modifiedEvents = applyEventModifications(newEvent, existingConversation.getEvents());

		if (existingIndex >= 0) {
			// this user event is somehow already present, and is older
			existingConversation.getEvents().remove(existingIndex);
		}

		existingConversation.getEvents().add(newEvent);
		modifiedEvents.add(newEvent);

		return modifiedEvents;
	}

	private Set<ConversationEvent> applyEventModifications(@NotNull ConversationEvent newEvent,
	                                                       @NotNull Collection<ConversationEvent> events) {

		Set<ConversationEvent> modifiedEvents = new HashSet<>();
		List<ConversationEventVisitor> visitors = new ArrayList<>();

		// find previous event, if referenced. or find an event with the same event id
		events.stream()
				.filter(event -> event.getEventId().equals(newEvent.getEventId()) || event.getEventId().equals(newEvent.getPreviousId()))
				.forEach(event -> addModificationVisitorsForRemovedEvent(event, modifiedEvents, visitors));

		addModificationVisitorsForAddedEvent(newEvent, modifiedEvents, visitors);

		events.forEach(event -> visitors.forEach(event::accept));

		return modifiedEvents;
	}

	private void addModificationVisitorsForRemovedEvent(@NotNull ConversationEvent removedEvent,
	                                                    Set<ConversationEvent> modifiedEvents,
	                                                    List<ConversationEventVisitor> visitors) {
		// handle removed data labels, if any
		getDataLabels(removedEvent)
				.map(dataLabel -> new DataLabelRemoved(dataLabel, modifiedEvents))
				.forEach(visitors::add);
	}

	private void addModificationVisitorsForAddedEvent(@NotNull ConversationEvent addedEvent,
	                                                  Set<ConversationEvent> modifiedEvents,
	                                                  List<ConversationEventVisitor> visitors) {

		// handle added data labels, if any
		getDataLabels(addedEvent)
				.map(dataLabel -> new DataLabelAdded(dataLabel, modifiedEvents))
				.forEach(visitors::add);

		if (addedEvent instanceof AnonymizeEvent) {
			// handle anonymization
			visitors.add(new ConversationAnonymizer((AnonymizeEvent) addedEvent, modifiedEvents));
		} else if (addedEvent instanceof ReviewEvent || addedEvent instanceof SurveyEvent) {
			// set CallREC segments protected
			visitors.add(new SetSegmentsProtected(modifiedEvents));
		}
	}

	@NotNull
	private Stream<Label> getDataLabels(ConversationEvent event) {
		if (event instanceof TagEvent) {
			return ((TagEvent) event).getLabels().stream()
					.filter(label -> label.getDataKey() != null);
		}
		return Stream.empty();
	}

	private class DataLabelRemoved extends ConversationEventVisitorBase {
		private final Label dataLabel;
		private final Set<ConversationEvent> modifiedEvents;

		DataLabelRemoved(Label dataLabel, Set<ConversationEvent> modifiedEvents) {
			this.dataLabel = dataLabel;
			this.modifiedEvents = modifiedEvents;
		}

		@Override
		public void visitKeyValueData(@NotNull KeyValueDataEvent event) {
			boolean removed = event.getData().remove(dataLabel.getDataKey(), dataLabel.getDataValue());
			if (removed) {
				modifiedEvents.add(event);
			}
		}
	}

	private class DataLabelAdded extends ConversationEventVisitorBase {
		private final Label dataLabel;
		private final Set<ConversationEvent> modifiedEvents;

		DataLabelAdded(Label dataLabel, Set<ConversationEvent> modifiedEvents) {
			this.modifiedEvents = modifiedEvents;
			this.dataLabel = dataLabel;
		}

		@Override
		public void visitKeyValueData(@NotNull KeyValueDataEvent event) {
			String previousValue = event.getData().put(dataLabel.getDataKey(), dataLabel.getDataValue());
			if (!Objects.equals(previousValue, dataLabel.getDataValue())) {
				modifiedEvents.add(event);
			}
		}
	}

	private static class SetSegmentsProtected implements ConversationEventVisitor {
		private final Set<ConversationEvent> modifiedEvents;

		SetSegmentsProtected(Set<ConversationEvent> modifiedEvents) {
			this.modifiedEvents = modifiedEvents;
		}

		@Override
		public void visit(@NotNull StartedCallEvent event) {
			if (event.getResource().getParent().getFlags().contains(ConversationResource.FLAG_PROTECTED)) {
				return;
			}
			HashSet<String> flags = new HashSet<>(event.getResource().getParent().getFlags());
			flags.add(ConversationResource.FLAG_PROTECTED);
			event.getResource().getParent().setFlags(flags);
			modifiedEvents.add(event);
		}
	}
}
