package utils;

public class StringToHash {

	public static int getDeepHash(String string) {
		int h = 0;		
		char val[] = string.toCharArray();
		int len = string.length();

		for (int i = 0; i < len; i++) {
			h = 31 * 7 * h + val[i];
		}
		return h;
	}
}
