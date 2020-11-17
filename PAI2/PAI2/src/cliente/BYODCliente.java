package cliente;

import java.io.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.*;

public class BYODCliente {

	public BYODCliente() {
		// Constructor que abre una conexión Socket para enviar mensaje/MAC al
		// servidor
		try {
			
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 7070);

			
			// crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream()));
			String mensaje = JOptionPane.showInputDialog(null,
					"Introduzca su mensaje:");
			output.println(mensaje); // envio del mensaje al servidor
			// habría que calcular el correspondiente MAC con la clave
			// compartida por servidor/cliente
			String macdelMensaje = "";
			output.println(macdelMensaje);
			output.flush();
			// crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String respuesta = input.readLine(); // lee la respuesta del servidor
			System.out.println(respuesta); // muestra la respuesta al cliente
			output.close();
			input.close();
			socket.close();
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
		new BYODCliente();
	}
}
