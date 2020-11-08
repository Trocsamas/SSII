package código;

import javax.crypto.*;
import javax.crypto.spec.*;

public class MAC {

	static String alg = "HmacSHA512";

	public static void main(String[] args) {
		String msg = "531456 487654 200";
		System.out.println("Mensaje         : " + msg);
		byte[] decodedKey = {32,97,54,65};
		String resumen = performMACTest(msg, decodedKey);
		System.out.println("Clave Hex       : "
				+ byteArrayToHexString(decodedKey) + "\t\tString : "
				+ new String(decodedKey));
		System.out.println("MAC             : " + resumen);
	}
		
	public static String performMACTest(String s, byte[] decodedKey) {
		String st = "";
		try {
			Mac mac = Mac.getInstance(alg);
			SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length,
					alg);
			mac.init(key);
			mac.update(s.getBytes());
			byte[] b = mac.doFinal();
			st = byteArrayToHexString(b);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return st;
	}

	private static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "a", "b", "c", "d", "e", "f" };

	// Convierte un byte a una cadena hexadecimal
	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	// Convierte un array de bytes a una cadena hexadecimal
	public static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; ++i)
			result += byteToHexString(b[i]);
		return result;
	}
}