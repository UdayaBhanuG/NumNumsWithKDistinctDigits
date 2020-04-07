package coNuTh;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NumNumsWithKDistinctDigits {
	public static void main(String[] args) {
		long maxNum = 50;
		
		List<List<List<Integer>>> summations = new ArrayList<List<List<Integer>>>();
		List<Integer> sum1 = new ArrayList<Integer>();
		sum1.add(1);
		List<List<Integer>> sum2 = new ArrayList<List<Integer>>();
		sum2.add(sum1);
		summations.add(sum2);
		
		for (long n = 1; n <= maxNum; n++) {
			BigInteger runningSum = BigInteger.valueOf(0l);
			BigInteger maxSum = power(n, n);
			
			long maxK = 0l;
			BigInteger maxNumForK = BigInteger.valueOf(1l);
			
			BigInteger nFac = fact(n);
			for (long k = n; k >= 1; k--) {
				BigInteger nums = BigInteger.valueOf(1l);
				if (k == n) nums = nFac;
				else if (k == 1) nums = BigInteger.valueOf(n);
				else {
					BigInteger baseRepeats = comb(n, k);
					int repeatedDigitCnt = (int) (n - k);
					
					BigInteger allRepeats = BigInteger.valueOf(0l);
					List<List<Integer>> sumK = summations.get(repeatedDigitCnt - 1);
					for (long l = 0; l < sumK.size(); l++) {
						List<Integer> oneSummation = sumK.get((int) l);
						
						if (oneSummation.size() > k) continue;
						
						BigInteger repeatsWhenAllListRepeats = comb(k, oneSummation.size());
						BigInteger numRepeats = nFac;
						for (int i = 0; i < oneSummation.size(); i++) {
							numRepeats = numRepeats.divide(fact(oneSummation.get(i) + 1));
						}
						long repeats = oneSummation.size();
						long timesRepeated = 0;
						int numRepeated = 2;
						for (int i = 0; i < oneSummation.size(); i++) {
							int num = oneSummation.get(i);
							if (num > numRepeated) {
								numRepeats = numRepeats.multiply(comb(repeats, timesRepeated));
								
								repeats -= timesRepeated;
								timesRepeated = 1;
								numRepeated = num;
							} else if (num == numRepeated) {
								timesRepeated++;
							}
						}
						numRepeats = numRepeats.multiply(comb(repeats, timesRepeated));
						
						numRepeats = numRepeats.multiply(repeatsWhenAllListRepeats);
						allRepeats = allRepeats.add(numRepeats);
					}
					
					allRepeats = allRepeats.multiply(baseRepeats);
					nums = allRepeats;
				}
				if (nums.compareTo(maxNumForK) > 0) {
					maxNumForK = nums;
					maxK = k;
				}
				runningSum = runningSum.add(nums);
				System.out.println("N:" + n + ",K:" + k + ",Nums:" + nums);
			}
			System.out.println("N:" + n + ",Max K:" + maxK + ",Max Nums:" + maxNumForK);
			if (!runningSum.equals(maxSum)) {
				System.out.println("runningSum for N:" + runningSum); 
				System.out.println("maxSum for N:" + maxSum); 
				System.out.println("Mismatch for N:" + n); 
			}
			List<List<Integer>> combsForNextHighest = new ArrayList<List<Integer>>();
			List<List<Integer>> ramanujanCombsForHighestNum = summations.get(summations.size() - 1);
			for (int k = 0; k < ramanujanCombsForHighestNum.size(); k++) {
				List<Integer> combs = ramanujanCombsForHighestNum.get(k);
				
				List<Integer> combsNew = new ArrayList<Integer>();
				combsNew.addAll(combs);
				
				if (combs.size() == summations.size()) {
					combsNew.add(1);
					combsForNextHighest.add(combsNew);
				}
				
				int runningNum = 0;
				for (int l = 0; l < combs.size(); l++) {
					if (runningNum == combs.get(l)) continue;
					runningNum = combs.get(l);
					
					combsNew = new ArrayList<Integer>();
					combsNew.addAll(combs);
					
					combsNew.set(l, combs.get(l) + 1);
					Collections.sort(combsNew);
					combsForNextHighest.add(combsNew);
				}
			}
			Map<String, List<Integer>> combosMap = new HashMap<String, List<Integer>>();
 			Iterator<List<Integer>> it = combsForNextHighest.iterator();
			while (it.hasNext()) {
				List<Integer> combo = it.next();
				String currCombs = combo.toString();
				combosMap.put(currCombs, combo);
			}
			combsForNextHighest = new ArrayList<List<Integer>>();
			it = combosMap.values().iterator();
			while (it.hasNext()) {
				List<Integer> combo = it.next();
				combsForNextHighest.add(combo);
			}
			System.out.println(combsForNextHighest);
			summations.add(combsForNextHighest);
		}
	}
	
	static Map<Long, BigInteger> facts = new HashMap<Long, BigInteger>();
	public static BigInteger fact(long n) {
		if (facts.containsKey(n)) return facts.get(n);
		
		BigInteger fac = BigInteger.valueOf(1l);
		for (long i = 2; i <= n; i++) {
			fac = fac.multiply(BigInteger.valueOf(i));
		}
		facts.put(n, fac);
		return fac;
	}
	static Map<String, BigInteger> combs = new HashMap<String, BigInteger>();
	public static BigInteger comb(long n, long r) {
		BigInteger com1 = BigInteger.valueOf(1l);
		if (r > n) return com1;
		if (r > (n - r)) r = n - r;
		
		if (combs.containsKey(n + ":" + r)) return combs.get(n + ":" + r);
		for (long i = 1; i <= r; i++) {
			com1 = com1.multiply(BigInteger.valueOf(n - i + 1));
			com1 = com1.divide(BigInteger.valueOf(i));
		}
		combs.put(n + ":" + r, com1);
		return com1;
	}
	public static BigInteger power(long n, long k) {
		BigInteger pow = BigInteger.valueOf(1l);
		for (long i = 1; i <= k; i++) {
			pow = pow.multiply(BigInteger.valueOf(n));
		}
		return pow;
	}
}
