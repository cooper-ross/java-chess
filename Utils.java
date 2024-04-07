public class Utils {

	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String reverse(String input) {
		char[] in = input.toCharArray();
		int begin = 0;
		int end = in.length - 1;
		char temp;
		while (end > begin) {
			temp = in[begin];
			in[begin] = in[end];
			in[end] = temp;
			end--;
			begin++;
		}
		return new String(in);
	}

	static byte[] reverseByteArray(byte a[], byte n) {
		byte i, t;
		for (i = 0; i < n / 2; i++) {
			t = a[i];
			a[i] = a[n - i - 1];
			a[n - i - 1] = t;
		}

		return a;
	}

	public static byte findIndex(byte arr[], byte t) {
		if (arr == null) {
			return -1;
		}

		byte len = (byte) (arr.length);
		byte i = 0;

		while (i < len) {
			if (arr[i] == t) {
				return i;
			} else {
				i = (byte) (i + 1);
			}
		}
		return -1;
	}

	public static void main(String[] args) {

	}
}
