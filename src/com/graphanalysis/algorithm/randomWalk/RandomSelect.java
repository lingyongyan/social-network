/**
 * 
 */
package com.graphanalysis.algorithm.randomWalk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Algorithm For Generating Random Numbers If parameters of constructor is just
 * a key set, then generate a random number With equal probability. If the
 * prameters is discerate probability distribution, then it execute follow
 * algorithm: Given a discrate distribution, {a1, a2, a3..., an} with
 * probabilitiy {p1, p2, p3..., pn}, this algorithm samples a element ai with
 * probability pi.
 * 
 * @reference paper "A Linear Algorithm For Generating Random Numbers With a
 *            Given Distribution",Michael Vose. web site
 *            http://www.keithschwarz.com/darts-dice-coins/
 * 
 * @author chenxx
 *
 */
public class RandomSelect<T> {

	/* A map of {0,1,2...n} to {a1,a2,...,an} */
	private Map<Integer, T> keyMap;

	private final Random random;
	private AliasMethod alias;
	private List<T> keySet;
	private List<Double> probabilities;

	public RandomSelect(List<T> keySet) {
		if (keySet == null)
			throw new NullPointerException("Key set must be not null");
		if (keySet.size() == 0)
			throw new IllegalArgumentException(
					"Probability vector must be nonempty");
		this.random = new Random();
		this.keySet = keySet;
		this.keyMap = new HashMap<Integer, T>();
		for (int i = 0; i < keySet.size(); i++) {
			keyMap.put(i, keySet.get(i));
		}
		this.probabilities = null;
		this.alias = null;
	}

	public RandomSelect(List<T> keySet, List<Double> probabilities) {
		this(keySet);
		this.probabilities = probabilities;
		this.alias = new AliasMethod(this.probabilities);
	}

	public T next() {
		if (this.alias == null)
			return keyMap.get(random.nextInt(this.keySet.size()));
		else
			return keyMap.get(this.alias.next());
	}
}
