package servidor;

import java.io.*;
import java.net.*;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class BYODServer {

	private ServerSocket serverSocket;

	// Constructor
	public BYODServer() throws Exception {
		// ServerSocketFactory para construir los ServerSockets
		SSLServerSocketFactory socketFactory = (SSLServerSocketFactory)	SSLServerSocketFactory.getDefault();

		serverSocket = (SSLServerSocket) socketFactory.createServerSocket(7070);

	}

	// ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() {
		while (true) {
			// espera las peticiones del cliente para comprobar mensaje/MAC
			try {
				System.err.print("Esperando conexiones de clientes...");
				Socket socket = (Socket) serverSocket.accept();
				// abre un BufferedReader para leer los datos del cliente
				BufferedReader input = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				// abre un PrintWriter para enviar datos al cliente
				PrintWriter output = new PrintWriter(new OutputStreamWriter(
						socket.getOutputStream()));
				// se lee del cliente el mensaje y el macdelMensajeEnviado
				String mensaje = input.readLine();
				String macdelMensajeEnviado = input.readLine();
				// a continuación habría que verificar el MAC
				String macdelMensajeCalculado = "";
				System.err.println(mensaje);
				if (macdelMensajeEnviado.equals(macdelMensajeCalculado)) {
					output.println("Mensaje enviado integro ");
				} else {
					output.println("Mensaje enviado no integro.");
				}
				output.close();
				input.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) throws Exception {
		BYODServer server = new BYODServer();
		server.runServer();
	}
}
