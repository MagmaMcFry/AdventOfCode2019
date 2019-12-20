package aoc19.util;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BiConsumer;

public class DijkstraPathfinder {

	public static <T extends State<T>> StateAndDistance<T> findClosestGoalState(T initialState) {
		int stateCount = 0;
		PriorityQueue<StateAndDistance<T>> states = new PriorityQueue<>(); // What kind of dumb priority queue sorts objects by their natural order????
		Set<T> completedStates = new HashSet<>(); // Extra data structure to check duplicates because the PriorityQueue can't do it
		states.add(new StateAndDistance<>(initialState, 0));
		while (!states.isEmpty()) {
			++stateCount;
			StateAndDistance<T> stateAndDistance = states.poll();
			T state = stateAndDistance.state;
			int currentDistance = stateAndDistance.distance;
			if ((stateCount & 0xfffff) == 0) {
				System.out.println("States processed so far: " + stateCount);
				System.out.println("Waiting states: " + states.size());
				System.out.println("Current state: " + state.toString());
			}
			if (completedStates.contains(state)) {
				continue;
			}
			completedStates.add(state);
			if (state.isDone()) {
				return stateAndDistance;
			}
			state.getNextStates((nextState, nextDistance) -> {
				states.add(new StateAndDistance<>(nextState, currentDistance + nextDistance));
			});
		}
		return null;
	}


	public interface State<T extends State<T>> {
		void getNextStates(BiConsumer<T, Integer> output);
		boolean isDone();
	}

	public static class StateAndDistance<T> implements Comparable<StateAndDistance<T>> {
		public final T state;
		public final int distance;

		private StateAndDistance(T state, int distance) {
			this.state = state;
			this.distance = distance;
		}

		@Override
		public int compareTo(StateAndDistance<T> that) {
			return Integer.compare(this.distance, that.distance);
		}
	}

}
