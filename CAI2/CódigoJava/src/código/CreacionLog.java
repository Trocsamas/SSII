package código;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class CreacionLog {
	
	private static boolean isNumeric(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}

	public static void creaLogIntegridad(String ruta, String mensaje, double ratio) {
		FileHandler f;
		Logger lg = Logger.getLogger("Log");
		
		try {
			f = new FileHandler(ruta, true);
			lg.addHandler(f);
			
			SimpleFormatter sf = new SimpleFormatter();
			f.setFormatter(sf);
			
			lg.info("Información de la Integridad de los mensajes intercambiados");
			lg.info(mensaje);
			lg.info("Ratio de integridad (Mensajes integros / Mensajes Totales) : " + (ratio*100) + "%\n");
			f.close();
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean compruebaLog (String ruta, String mensaje,String timestamp) {
		try{
			boolean existeLog = false; 
			FileInputStream fstream = new FileInputStream(ruta);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;
			   /* read log line by line */
			while ((strLine = br.readLine()) != null)   {
			     /*Buscamos las lineas en las que haya mensaje*/
				String[] descomposicion = strLine.split(" ");
				if (descomposicion[0]=="INFO:") {
					if (isNumeric(descomposicion[1])) {
						String mensajeLog = descomposicion[1]+" "+descomposicion[2]+" "+descomposicion[3];
						/*comprobamos si el mensaje coincide con el del log*/
						if (mensaje==mensajeLog) {
							System.out.println("Hay un match con este mensaje");
						}
						else {
							continue;
						}
					}
				}
				else {
					continue;
				}
					
			}
			fstream.close();
			return existeLog; 
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return false;
	}
}
