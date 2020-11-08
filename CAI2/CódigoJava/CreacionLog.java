package código;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class CreacionLog {

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
}
