package com.encouragee.camel.clientSearch.conversation.index;

import com.google.common.collect.Ordering;
import com.zoomint.encourage.model.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import org.springframework.stereotype.Component;

import java.util.Comparator;

import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsFirst;

/**
 * Responsible for calculating business fields of a conversation based on conversation events.
 */
@Component
public class ConversationCalculator {

	public static final Ordering<ConversationEvent> EVENT_ORDERING = Ordering.from(Comparator
			.comparing(ConversationEvent::getStart, nullsFirst(naturalOrder()))
			.thenComparing(ConversationEvent::getCreated, nullsFirst(naturalOrder())));

	public void calculateBusinessFields(Conversation conversation) {
		// sort the conversation events
		conversation.setEvents(EVENT_ORDERING.sortedCopy(conversation.getEvents()));

		// apply visitor - should be active events only after merging
		CalculatorEventVisitor visitor = new CalculatorEventVisitor();
		conversation.getEvents().forEach(event -> event.accept(visitor));
		visitor.applyToConversation(conversation);
	}

}
