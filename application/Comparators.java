package application;

/*
 * 
 * unused
 * 
 */

import java.util.Comparator;

public class Comparators {
	public static Comparator<String> ALPHABETICAL = new alphabetical();
	
	private static class alphabetical implements Comparator<String> {
		public int compare(String a, String b) {
			return a.compareTo(b);
		}
	}
}
