package com.example.app;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

public class DataSender {
	public static void SendDatosPE(Datos medidas) {
		double tension = medidas.getTension();
		double aceleracionx = medidas.getAceleracion_x();
		double aceleraciony = medidas.getAceleracion_y();
		double aceleracionz = medidas.getAceleracion_z();
		double longitud = medidas.getLongitud();
		double latitud = medidas.getLatitud();
		double azucar = medidas.getAzucar();
		double carbono = medidas.getGas();
		int pulso = medidas.getPulso();
		int temperatura = medidas.getTemperatura();
		String imei = medidas.getImei();
		Long timestamp = medidas.getTiempo();
		// Envio datos a el Operador

		String username = "isst.teleasistencia@gmail.com";
		String password = "teleasistencia";

		RemoteApiOptions options = new RemoteApiOptions().server(
				"d5-ca-isst-2015.appspot.com", 443).credentials(username,
				password);

		RemoteApiInstaller installer = new RemoteApiInstaller();

		try {

			installer.install(options);

			// All API calls executed remotely

			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

			List<Double> dataAcceleracion = Arrays.asList(
					(Double) aceleracionx, (Double) aceleraciony,
					(Double) aceleracionz);
			List<Double> dataLocation = Arrays.asList((Double) latitud,
					(Double) longitud);
			List<Double> dataTemperature = Arrays.asList((Double) 0.0
					+ temperatura);
			List<Double> dataCarbonMonoxide = Arrays.asList((Double) carbono);
			List<Double> dataHeartRateMonitor = Arrays.asList((Double) 0.0
					+ pulso);
			List<Double> dataGlucoseMeter = Arrays.asList((Double) azucar);
			List<Double> dataBloodPressure = Arrays.asList((Double) tension);

			sendDatos(ds, "Acceleration", imei, timestamp, 103,
					dataAcceleracion, "m/s2");
			sendDatos(ds, "Location", imei, timestamp, 102, dataLocation,
					"string");
			sendDatos(ds, "Temperature", imei, timestamp, 201, dataTemperature,
					"C");
			sendDatos(ds, "CarbonMonoxide", imei, timestamp, 202,
					dataCarbonMonoxide, "ppm");
			sendDatos(ds, "HeartRateMonitor", imei, timestamp, 301,
					dataHeartRateMonitor, "bpm");
			sendDatos(ds, "GlucoseMeter", imei, timestamp, 302,
					dataGlucoseMeter, "mg/dl");
			sendDatos(ds, "BloodPressure", imei, timestamp, 303,
					dataBloodPressure, "mmHg");

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			installer.uninstall();
		}
	}

	private static void sendDatos(DatastoreService ds, String name, String originator,
			Long timestamp, Integer type, List<Double> data, String units) {
		Entity event = new Entity("Event");
		event.setProperty("name", name);
		event.setProperty("originator", originator);
		event.setProperty("timestamp", timestamp);
		event.setProperty("type", type);
		event.setProperty("data", data);
		event.setProperty("units", units);

		System.out.println("Key of new entity is " + ds.put(event));
	}
}
