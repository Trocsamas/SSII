package código;

import java.io.*;
import java.net.*;
import javax.net.*;

public class IntegrityVerifierServer {

	private ServerSocket serverSocket;
	static int mensajesTotales = 0;
	static int mensajesIntegros = 0;
	
	// Constructor
	public IntegrityVerifierServer() throws Exception {
		// ServerSocketFactory para construir los ServerSockets
		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory
				.getDefault();
		// creación de un objeto ServerSocket (se establece el puerto)
		serverSocket = (ServerSocket) socketFactory.createServerSocket(7070);
	}

	// ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() {
		
				/*							MODIFICACION								*/
				// Creacion directorio para almacenar el .log
				File directorioLog = new File("C:/LogDirectory");
				directorioLog.mkdir();
				/*							MODIFICACION								*/
				
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
				
				/*                           MODIFICACION                                */
				
				mensajesTotales++;
				// a continuación habría que verificar el MAC
				String macdelMensajeCalculado = VerificadorMac.generaMac(mensaje);
				System.err.println(mensaje);
				if (macdelMensajeEnviado.equals(macdelMensajeCalculado)) {
					mensajesIntegros++;
					//CreacionLog.creaLogIntegridad("C:/LogDirectory/logIntegridad.log", "", (double)mensajesIntegros/mensajesTotales);
					output.println("Mensaje enviado integro ");
				} else {
					CreacionLog.creaLogIntegridad("C:/LogDirectory/logIntegridad.log", mensaje,(double)mensajesIntegros/mensajesTotales);
					output.println("Mensaje enviado no integro.");
				}
				
				/* 							FIN MODIFICACION							*/
				
				output.close();
				input.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	// ejecucion del servidor
	public static void main(String args[]) throws Exception {
		IntegrityVerifierServer server = new IntegrityVerifierServer();
		server.runServer();
	}
}
