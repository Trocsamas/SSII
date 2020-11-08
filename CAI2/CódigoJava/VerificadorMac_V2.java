package código;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

public class VerificadorMac_V2 {

	//static String alg = "HmacSHA512";
	//Clave en formato String para comodidad
	static String clave = "";
	static String alg = "";
	
	public static String generaMacCliente(String mensaje){
		String res = "";
		algoritmoElegido();
		clave = JOptionPane.showInputDialog(null, "Introduzca clave:");
		byte[] key = claveEnByte(clave);
		
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
	
	public static String generaMacServidor(String mensaje){
		String res = "";
		byte[] key = claveEnByte(clave);
		
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
	
	//Método para elegir el algoritmo de codificacion
	private static void algoritmoElegido() {
		int sel = JOptionPane.showOptionDialog(null, "Selecccione Algoritmo", "Algoritmo", JOptionPane.YES_NO_CANCEL_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, new Object[]{"HmacSHAMD5","HmacSHA1","HmacSHA256", "HmacSHA512"}, null);
		if(sel != -1) {
			switch (sel) {
			case 0:
				alg = "HmacSHAMD5";
				break;
			case 1:
				alg = "HmacSHA1";
				break;
			case 2:
				alg = "HmacSHA256";
				break;
			case 3:
				alg = "HmacSHA512";
				break;
			}
		}
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
