package código;

import java.io.*;

import javax.net.*;
import javax.swing.*;
import java.net.*;
import java.sql.Timestamp;
 

public class IntegrityVerifierClient {

	public IntegrityVerifierClient() {
		// Constructor que abre una conexión Socket para enviar mensaje/MAC al
		// servidor
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory
					.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost",
					7070);
			// crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			String mensaje = JOptionPane.showInputDialog(null,
					"Introduzca su mensaje:");
			if(mensaje == null) {
				JOptionPane.showMessageDialog(null, "Mensaje no especificado");
			}else {
				
			output.println(mensaje); // envio del mensaje al servidor
			// habría que calcular el correspondiente MAC con la clave
			// compartida por servidor/cliente
			String macdelMensaje = VerificadorMac.generaMac(mensaje);
			
			//Sello de tiempo para ataques por Replay TODO
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			
			output.println(macdelMensaje);
			output.println(ts);
			
			output.flush();
			// crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String respuesta = input.readLine(); // lee la respuesta del servidor
			if(respuesta.equals("Mensaje enviado no integro.")) {
				JOptionPane.showMessageDialog(null, "Mensaje envidado al servidor no integro, existe un problema de seguridad con su clave");
			}else {
				JOptionPane.showMessageDialog(null, "Mensaje enviado integro al servidor");
			}
			//System.out.println(respuesta); // muestra la respuesta al cliente
			output.close();
			input.close();
			socket.close();
			}
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		// Salida de la aplicacion
		finally {
			System.exit(0);
		}
	}

	// ejecución del cliente de verificación de la integridad
	public static void main(String args[]) {
		new IntegrityVerifierClient();
	}
}
