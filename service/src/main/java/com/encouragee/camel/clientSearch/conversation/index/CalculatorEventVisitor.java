package com.encouragee.camel.clientSearch.conversation.index;

import com.encouragee.camel.clientSearch.conversation.Conversation;
import com.zoomint.encourage.model.conversation.ConversationEvent;
import com.zoomint.encourage.model.conversation.ConversationEventVisitorBase;
import com.zoomint.encourage.model.conversation.event.*;
import com.zoomint.encourage.model.search.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Allows calculating business fields of a conversation based on conversation events.
 * Conversation should be in its final form (with all entities loaded) before the calculation is done.
 * This visitor should be applied to active events only, i.e., deleted events should be skipped.
 */
public class CalculatorEventVisitor extends ConversationEventVisitorBase {

	protected Direction direction = Direction.UNKNOWN;
	protected ZonedDateTime directionStart;
	protected Instant start = Instant.EPOCH;
	protected Instant firstCallStart;
	protected Instant lastCallEnd;

	public void applyToConversation(Conversation conversation) {
		conversation.setStart(start);
		conversation.setDirection(direction);
		conversation.setDuration(calculateDuration());
	}

	protected Duration calculateDuration() {
		if (firstCallStart == null || lastCallEnd == null || firstCallStart.isAfter(lastCallEnd)) {
			return Duration.ZERO;
		}
		return Duration.between(firstCallStart, lastCallEnd);
	}

	private void applyToStart(ConversationEvent event) {
		Optional.ofNullable(event.getStart())
				.map(ZonedDateTime::toInstant)
				// update start if sooner
				.filter(candidate -> start == Instant.EPOCH || start.isAfter(candidate))
				.ifPresent(candidate -> start = candidate);
	}

	@Override
	public void visitDirectionable(@NotNull DirectionableEvent event) {
		if (event.getDirection() == null || event.getDirection() == Direction.UNKNOWN) {
			return; // ignore unknown
		}
		if (direction == Direction.UNKNOWN || isBefore(event.getStart(), directionStart)) {
			direction = event.getDirection();
			directionStart = event.getStart();
		}
	}

	@Override
	public void visitTextMessage(@NotNull TextMessageEvent event) {
		applyToStart(event);
	}

	@Override
	public void visit(@NotNull StartedCallEvent event) {
		super.visit(event);
		applyToStart(event);
		Optional.ofNullable(event.getStart())
				.map(ZonedDateTime::toInstant)
				// update firstCallStart if sooner
				.filter(candidate -> firstCallStart == null || firstCallStart.isAfter(candidate))
				.ifPresent(candidate -> firstCallStart = candidate);
	}

	@Override
	public void visit(@NotNull LeftCallEvent event) {
		super.visit(event);
		Optional.ofNullable(event.getStart())
				.map(ZonedDateTime::toInstant)
				// update lastCallEnd if later
				.filter(candidate -> lastCallEnd == null || lastCallEnd.isBefore(candidate))
				.ifPresent(candidate -> lastCallEnd = candidate);
	}


	@Override
	public void visit(@NotNull StartedVideoEvent event) {
		super.visit(event);
		applyToStart(event);
		Optional.ofNullable(event.getStart())
				.map(ZonedDateTime::toInstant)
				// update firstCallStart if sooner
				.filter(candidate -> firstCallStart == null || firstCallStart.isAfter(candidate))
				.ifPresent(candidate -> firstCallStart = candidate);
	}

	@Override
	public void visit(@NotNull EndedVideoEvent event) {
		super.visit(event);
		Optional.ofNullable(event.getStart())
				.map(ZonedDateTime::toInstant)
				// update lastCallEnd if later
				.filter(candidate -> lastCallEnd == null || lastCallEnd.isBefore(candidate))
				.ifPresent(candidate -> lastCallEnd = candidate);
	}

	private boolean isBefore(@Nullable ZonedDateTime one, @Nullable ZonedDateTime two) {
		return one != null && (two == null || one.isBefore(two));
	}

}
