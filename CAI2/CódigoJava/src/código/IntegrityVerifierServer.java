package código;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import javax.net.*;

public class IntegrityVerifierServer {

	private ServerSocket serverSocket;
	static int mensajesTotales = 0;
	static int mensajesIntegros = 0;
	//diccionario nºcuenta-clave. Queda rellenar para las pruebas. Convendría acceder
	//a una BD o un csv
	static Map<Integer, String> diccionarioCuentas = new HashMap<Integer, String>();
	
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
				diccionarioCuentas.put(13579, "abcd");
				diccionarioCuentas.put(24680, "qwer");
				diccionarioCuentas.put(12345, "wasd");
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
				String timestampEnviado = input.readLine();
				//separamos el mensaje para obtener el número de cuenta
				String[] diseccionMensaje = mensaje.split(" ");
				Integer numeroCuenta = Integer.parseInt(diseccionMensaje[0]);
				//buscamos la clave en el diccionario introduciendo la cuenta
				String claveCliente = diccionarioCuentas.get(numeroCuenta);
				
				/*                           MODIFICACION                                */
				
				mensajesTotales++;
				// a continuación habría que verificar el MAC
				String macdelMensajeCalculado = VerificadorMac.generaMac(mensaje, claveCliente);
				System.err.println(mensaje);
				if (macdelMensajeEnviado.equals(macdelMensajeCalculado)) {
					//comprobamos si existe en nuestro log
					
					mensajesIntegros++;
					CreacionLog.creaLogIntegridad("C:/LogDirectory/logIntegridad.log", mensaje + " " + timestampEnviado, (double)mensajesIntegros/mensajesTotales);
					output.println("Mensaje enviado integro.");
					
				} else {
					CreacionLog.creaLogIntegridad("C:/LogDirectory/logIntegridad.log", mensaje + "",(double)mensajesIntegros/mensajesTotales);
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
