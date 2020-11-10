package código;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class VerificadorMac {

	static String alg = "HmacSHA512";
	//Clave en formato String para comodidad
	//static String clave = " a6A";
	
	public static String generaMac(String mensaje,String claveCliente){
		String res = "";
		byte[] key = claveEnByte(claveCliente);
		try {
			Mac mac = Mac.getInstance(alg);
			SecretKey sk = new SecretKeySpec(key, 0, key.length,alg);
			mac.init(sk);
			mac.update(mensaje.getBytes());
			byte[] b = mac.doFinal();
			res = byteArrayToHexString(b);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
		
	}
	
	//Método privado para cambiar la clave de String a byte[]
	private static byte[] claveEnByte(String s) {
		byte[] res = new byte[s.length()];
		for(int i = 0; i < s.length(); i++) {
			res[i] = (byte) s.charAt(i);
		}
		return res;
	}
	
	private static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6",
			"7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	// Convierte un array de bytes a una cadena hexadecimal
	public static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; ++i)
			result += byteToHexString(b[i]);
		return result;
	}
	
	// Convierte un byte a una cadena hexadecimal
	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}
	
}
