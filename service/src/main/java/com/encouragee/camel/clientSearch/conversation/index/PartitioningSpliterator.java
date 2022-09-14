package com.encouragee.camel.clientSearch.conversation.index;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

/**
 * Allows to partition a stream of elements into a stream of lists of elements,
 * where each list is up to the specified max partition size (but could be smaller).
 */
public class PartitioningSpliterator<E> extends Spliterators.AbstractSpliterator<List<E>> {
	private final Spliterator<E> spliterator;
	private final int partitionSize;

	@NotNull
	public static <T> Stream<List<T>> partition(@NotNull Iterable<T> elements, int partitionSize) {
		return stream(new PartitioningSpliterator<>(elements.spliterator(), partitionSize), false);
	}

	@NotNull
	public static <T> Stream<List<T>> partition(@NotNull Stream<T> elements, int partitionSize) {
		return stream(new PartitioningSpliterator<>(elements.spliterator(), partitionSize), false);
	}

	public PartitioningSpliterator(Spliterator<E> spliterator, int partitionSize) {
		super(spliterator.estimateSize(), spliterator.characteristics() | Spliterator.NONNULL);
		if (partitionSize <= 0) {
			throw new IllegalArgumentException("partition size must be positive");
		}
		this.spliterator = spliterator;
		this.partitionSize = partitionSize;
	}

	@Override
	public boolean tryAdvance(Consumer<? super List<E>> action) {
		List<E> partition = new ArrayList<>(partitionSize);
		while (spliterator.tryAdvance(partition::add) && partition.size() < partitionSize) {
			; // adding elements until partition size
		}
		if (partition.isEmpty()) {
			return false;
		}
		action.accept(partition);
		return true;
	}

	@Override
	public long estimateSize() {
		long estimate = spliterator.estimateSize();
		if (estimate == Long.MAX_VALUE) {
			return Long.MAX_VALUE; // infinite / unknown
		}
		return estimate / partitionSize + (estimate % partitionSize == 0 ? 0 : 1); // round up
	}
}
