package com.example.app;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AlarmSender {
	public static void sendAlarm(Datos datos, String name, int type,
			int severity) {

		String message =

		"{\'name\':\'" + name + "\'"

		+ ",\'type\':" + type + ""

		+ ",\'originator\':\'" + datos.getImei() +"\'"

		+ ",\'timestamp\':\'" + datos.getTiempo() + "\'"
		
		+ ",\'severity\':\'" + severity + "\'"

		+ ",\'location\': {" 
			
			+ "\'latitude\':\'" + datos.getLatitud()	+ "\'" 
		
			+ ",\'longitude\':\'" + datos.getLongitud() + "\'"
				
		+ "}}";

		try {
			URL url = new URL(
					"https://d5-ca-isst-2015.appspot.com/_ah/api/teleassistence/v1/alarm");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setInstanceFollowRedirects(false);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("content-type", "application/json");

			OutputStreamWriter writer = new OutputStreamWriter(
					connection.getOutputStream());
			writer.write(message);
			writer.close();
			
			System.out.println(message);

			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				System.out.println(connection.getResponseCode());
				System.out.println(connection.getResponseMessage());
			} else {
				System.out.println(connection.getResponseCode());
				System.out.println(connection.getResponseMessage());
			}

		} catch (MalformedURLException e) {

		} catch (IOException e) {

		}

	}
}
