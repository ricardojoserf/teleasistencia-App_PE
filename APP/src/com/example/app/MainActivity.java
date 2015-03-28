/*
 * Qué queda por hacer:
 * 	
 * 	Volver a poner GPS
 * 	
 * 	Eliminar acelerómetros y giroscopios de la base de datos
 * 	
 * 	Leer los sensores externos de la BD y ponerlos en los métodos de la 
 * API de Google Endpoints
 * 
 * 	Hacer que se muestren los valores en la pantalla de datos
 * 	
 */

package com.example.app;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.app.datosendpoint.Datosendpoint;
import com.example.app.datosendpoint.model.Datos;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	private boolean alarm_flag = false;
	private int i = 0; // Esta variable se borrará cuando se quite la BD de
						// pruebas de GAE

	private int latitud = 0;
	private int longitud = 0;
	private float aceleracion_x = 0;
	private float aceleracion_y = 0;
	private float aceleracion_z = 0;
	private float giroscopio_x = 0;
	private float giroscopio_y = 0;
	private float giroscopio_z = 0;
	private int pulso = 0;
	private int tension = 0;
	private int azucar = 0;
	private int temperatura = 0;
	private boolean gas = false;
	private String imei = "";
	private Long tiempo = (long) 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Button boton = (Button) findViewById(R.id.button1);
		Button boton2 = (Button) findViewById(R.id.button2);
		Button boton3 = (Button) findViewById(R.id.button3);

		ImageView imagen = (ImageView) findViewById(R.id.imageView1);

		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		List<Sensor> listaSensores = sensorManager
				.getSensorList(Sensor.TYPE_ALL);

		listaSensores = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);

		if (!listaSensores.isEmpty()) {

			Sensor orientationSensor = listaSensores.get(0);

			sensorManager.registerListener(this, orientationSensor,
					SensorManager.SENSOR_DELAY_UI);
		}

		listaSensores = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);

		if (!listaSensores.isEmpty()) {

			Sensor acelerometerSensor = listaSensores.get(0);

			sensorManager.registerListener(this, acelerometerSensor,
					SensorManager.SENSOR_DELAY_UI);
		}

		TelephonyManager mTelephonyManager;
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		imei = mTelephonyManager.getDeviceId();

		Timer timer = new Timer();
		TimerTask llamadaPeriodica = new TimerTask() {

			@Override
			public void run() {

				Log.d("LOG PERIÓDICO", "Enviando datos periódicos");
				
				Time now = new Time();
				now.setToNow();
				tiempo = now.toMillis(false);

				new EndpointsTask().execute(getApplicationContext());

			}

		};

		// Se llama a la tarea llamadaPeriodica cada 10 segundos desde el segundo 5

		timer.scheduleAtFixedRate(llamadaPeriodica, (long) 5000, (long) 10000);

		boton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this,
						ThirdActivity.class);
				startActivity(intent);
			}

		});

		boton2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this,
						SecondActivity.class);
				startActivity(intent);
			}

		});

		boton3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this,
						FourthActivity.class);
				startActivity(intent);
			}

		});

		imagen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				alarm_flag = true;

				Time now = new Time();
				now.setToNow();
				tiempo = now.toMillis(false);

				new EndpointsTask().execute(getApplicationContext());

				Toast toast1 = Toast.makeText(getApplicationContext(),
						"Datos enviados", Toast.LENGTH_SHORT);
				toast1.show();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent evento) {
		synchronized (this) {

			switch (evento.sensor.getType()) {

			case Sensor.TYPE_GYROSCOPE:

				aceleracion_x = evento.values[0];
				aceleracion_y = evento.values[1];
				aceleracion_z = evento.values[2];

				break;

			case Sensor.TYPE_ACCELEROMETER:

				giroscopio_x = evento.values[0];
				giroscopio_y = evento.values[1];
				giroscopio_z = evento.values[2];

				break;
			}
		}
	}

	/*
	 * Esta función genera la conexión con GAE, crea un objeto de tipo Datos
	 * (definido en el backend) y se van añadiendo los distintos parámetros, ya
	 * estén guardados como variables o en la base de datos de la app.
	 */

	public class EndpointsTask extends AsyncTask<Context, Integer, Long> {
		protected Long doInBackground(Context... contexts) {

			Datosendpoint.Builder endpointBuilder = new Datosendpoint.Builder(
					AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					new HttpRequestInitializer() {
						public void initialize(HttpRequest httpRequest) {
						}
					});

			Datosendpoint endpoint = CloudEndpointUtils.updateBuilder(
					endpointBuilder).build();

			try {

				Datos datos = new Datos();

				datos.setId((long) ++i);

				// Cambiar los ceros por los datos guardados en la BD de la app

				datos.setLatitud(0.0);
				datos.setLongitud(0.0);

				datos.setAceleracionX(aceleracion_x);
				datos.setAceleracionY(aceleracion_y);
				datos.setAceleracionZ(aceleracion_z);
				datos.setGiroscopioX(giroscopio_x);
				datos.setGiroscopioY(giroscopio_y);
				datos.setGiroscopioZ(giroscopio_z);

				datos.setPulso(0);
				datos.setTension(0.0);
				datos.setAzucar(0.0);

				datos.setTemperatura(0);
				datos.setGas(false);

				datos.setImei(imei);
				datos.setTiempo(tiempo);

				datos.setAlarma(alarm_flag);

				alarm_flag = false;

				endpoint.insertDatos(datos).execute();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return (long) 0;
		}
	}
}
