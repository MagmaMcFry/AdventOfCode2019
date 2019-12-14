package aoc19.days;

import aoc19.util.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day14Solver {
	public static void main(String[] args) {
		solvePart1();
		solvePart2();
	}

	public static List<String> getReactionStrings() {
		return FileReader.readLines("/day14/recipes.txt");
	}

	public static Process getProcess(List<String> strings) {
		return new Process(strings.stream().map(Reaction::new).collect(Collectors.toList()));
	}

	private static void solvePart1() {
		long count = getProcess(getReactionStrings()).computeOreRequirements(1);
		System.out.println("Total ore required per fuel: " + count);
	}

	private static void solvePart2() {
		long fuel = getProcess(getReactionStrings()).computePossibleFuel(1000000000000L);
		System.out.println("Total fuel producable: " + fuel);
	}

	static class Reaction {

		final List<String> reagents = new ArrayList<>();
		final List<Long> reagentAmounts = new ArrayList<>();
		final String product;
		final long productAmount;

		private Reaction(String reactionString) {
			String[] parts = reactionString.split("\\s*(=>|,)\\s*");
			for (int i = 0; i < parts.length - 1; ++i) {
				String reagentDef = parts[i];
				String[] splitDef = reagentDef.split(" ");
				reagents.add(splitDef[1]);
				reagentAmounts.add(Long.parseLong(splitDef[0]));
			}
			String[] productDef = parts[parts.length - 1].split(" ");
			product = productDef[1];
			productAmount = Long.parseLong(productDef[0]);
		}

		private void unapply(Map<String, Long> storage) {
			long productRequired = storage.getOrDefault(product, 0L);
			long reactionCount = 1+(productRequired-1)/productAmount;
			storage.put(product, 0L);
			for (int i = 0; i < reagents.size(); ++i) {
				String reagent = reagents.get(i);
				long reagentAmount = reagentAmounts.get(i);
				storage.put(reagent, storage.getOrDefault(reagent, 0L) + reactionCount * reagentAmount);
			}
		}
	}

	static class Process {
		List<Reaction> reactions = new ArrayList<>();

		private Process(List<Reaction> reactions) {
			Set<Reaction> unsortedReactions = new HashSet<>(reactions);
			Set<String> availableProducts = new HashSet<>();
			availableProducts.add("ORE");
			while (!unsortedReactions.isEmpty()) {
				int unsortedSize = unsortedReactions.size();
				Iterator<Reaction> unsortedIterator = unsortedReactions.iterator();
				CANDIDATE:
				while (unsortedIterator.hasNext()) {
					Reaction nextUnsorted = unsortedIterator.next();
					for (String reagent : nextUnsorted.reagents) {
						if (!availableProducts.contains(reagent)) {
							continue CANDIDATE; // Can't add this one yet
						}
					}
					unsortedIterator.remove();
					this.reactions.add(nextUnsorted);
					availableProducts.add(nextUnsorted.product);
				}
				if (unsortedSize == unsortedReactions.size()) {
					throw new IllegalStateException("Infinite loop found");
				}
			}
		}


		long computeOreRequirements(long desiredFuel) {
			Map<String, Long> storage = new HashMap<>();
			storage.put("FUEL", desiredFuel);
			for (int i = reactions.size()-1; i >= 0; --i) {
				reactions.get(i).unapply(storage);
			}
			return storage.getOrDefault("ORE", 0L);
		}

		long computePossibleFuel(long availableOre) {
			long fuelUpperLimit = 1;
			while (computeOreRequirements(fuelUpperLimit) <= availableOre) {
				fuelUpperLimit *= 2;
			}
			long possibleFuel = 0;
			long fuelIncrement = fuelUpperLimit;
			while (fuelIncrement > 0) {
				if (computeOreRequirements(possibleFuel + fuelIncrement) <= availableOre) {
					possibleFuel += fuelIncrement;
				}
				fuelIncrement /= 2;
			}
			return possibleFuel;
		}
	}
}
