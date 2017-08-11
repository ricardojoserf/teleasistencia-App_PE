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
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener,
		SensorEventListener {

	private boolean alarm_flag = false;

	private int id = 0;
	private double latitud = 0;
	private double longitud = 0;
	private float aceleracion_x0 = 0;
	private float delta_aceleracion_x = 0;
	private float aceleracion_y0 = 0;
	private float delta_aceleracion_y = 0;
	private float aceleracion_z0 = 0;
	private float delta_aceleracion_z = 0;
	private float giroscopio_x0 = 0;
	private float delta_giroscopio_x = 0;
	private float giroscopio_y0 = 0;
	private float delta_giroscopio_y = 0;
	private float giroscopio_z0 = 0;
	private float delta_giroscopio_z = 0;
	private int pulso = 0;
	private double tension = 0;
	private double azucar = 0;
	private int temperatura = 0;
	private double gas = 0;
	private String imei = "";
	private Long tiempo = (long) 0;

	private LocationManager locationManager;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	// Location location;

	Timer timer = new Timer();
	TimerTask llamadaPeriodica = new TimerTask() {

		@Override
		public void run() {

			getLatLong();

			Time now = new Time();
			now.setToNow();
			tiempo = now.toMillis(false);
			id++;

			new EndpointsTask().execute(getApplicationContext());

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Log.d("MILOG", "create");

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
	protected void onStart() {
		super.onStart();
		Log.d("MILOG", "start");
		getLatLong();
		valoresBD();

		recuperarIDdeBD();

		// Se llama a la tarea llamadaPeriodica cada 10 segundos desde el
		// segundo 5
		timer.scheduleAtFixedRate(llamadaPeriodica, (long) 5000, (long) 10000);

	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d("MILOG", "pause");

		guardarIDenBD();

		llamadaPeriodica.cancel();
		timer.cancel();

	};

	public void guardarIDenBD() {

		IdSQLiteOpenHelper admin = new IdSQLiteOpenHelper(this, "valorID",
				null, 1);
		SQLiteDatabase bd = admin.getWritableDatabase();
		ContentValues registro = new ContentValues();

		registro.put("id", id);

		bd.delete("valorID", null, null);
		bd.insert("valorID", null, registro);

		bd.close();
	}

	public void recuperarIDdeBD() {

		IdSQLiteOpenHelper admin = new IdSQLiteOpenHelper(this, "valorID",
				null, 1);
		SQLiteDatabase bd = admin.getReadableDatabase();

		ContentValues registro = new ContentValues();

		registro.put("id", 0);

		bd.insert("valorID", null, registro);

		if (bd != null) {

			String[] valores_recuperar = { "id" };

			Cursor c = bd.query("ValorID", valores_recuperar, null, null, null,
					null, null, null);

			if (c != null) {

				c.moveToFirst();

				id = c.getInt(0);

				c.close();
			}

		}
		Log.w("MILOG", "id = " + id);
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

	}

	@Override
	public void onSensorChanged(SensorEvent evento) {
		synchronized (this) {

			switch (evento.sensor.getType()) {

			case Sensor.TYPE_ACCELEROMETER:

				if (aceleracion_x0 == 0 && aceleracion_y0 == 0
						&& aceleracion_z0 == 0) {
					aceleracion_x0 = evento.values[0];
					aceleracion_y0 = evento.values[1];
					aceleracion_z0 = evento.values[2];
				} else {
					if (delta_aceleracion_x < Math.abs(aceleracion_x0
							- evento.values[0])) {
						delta_aceleracion_x = Math.abs(aceleracion_x0
								- evento.values[0]);
					}
					if (delta_aceleracion_y < Math.abs(aceleracion_y0
							- evento.values[1])) {
						delta_aceleracion_y = Math.abs(aceleracion_y0
								- evento.values[1]);
					}
					if (delta_aceleracion_z < Math.abs(aceleracion_z0
							- evento.values[2])) {
						delta_aceleracion_z = Math.abs(aceleracion_z0
								- evento.values[2]);
					}
				}
				break;

			case Sensor.TYPE_GYROSCOPE:

				if (giroscopio_x0 == 0 && giroscopio_y0 == 0
						&& giroscopio_z0 == 0) {
					giroscopio_x0 = evento.values[0];
					giroscopio_y0 = evento.values[1];
					giroscopio_z0 = evento.values[2];

				} else {
					if (delta_giroscopio_x < Math.abs(giroscopio_x0
							- evento.values[0])) {
						delta_giroscopio_x = Math.abs(giroscopio_x0
								- evento.values[0]);
					}
					if (delta_giroscopio_y < Math.abs(giroscopio_y0
							- evento.values[1])) {
						delta_giroscopio_y = Math.abs(giroscopio_y0
								- evento.values[1]);
					}
					if (delta_giroscopio_z < Math.abs(giroscopio_z0
							- evento.values[2])) {
						delta_giroscopio_z = Math.abs(giroscopio_z0
								- evento.values[2]);
					}
				}

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

				datos.setLatitud(latitud);
				datos.setLongitud(longitud);

				datos.setAceleracionX((double) delta_aceleracion_x);
				datos.setAceleracionY((double) delta_aceleracion_y);
				datos.setAceleracionZ((double) delta_aceleracion_z);
				datos.setGiroscopioX((double) delta_giroscopio_x);
				datos.setGiroscopioY((double) delta_giroscopio_y);
				datos.setGiroscopioZ((double) delta_giroscopio_z);

				datos.setPulso(pulso);
				datos.setTension(tension);
				datos.setAzucar(azucar);

				datos.setTemperatura(temperatura);
				datos.setGas(gas);

				datos.setImei(imei);
				datos.setTiempo(tiempo);

				if (!alarm_flag) {
					Long code = Long.parseLong("" + String.valueOf(id) + imei);
					datos.setId(code);
				} else {
					datos.setId((long) 0);
				}

				datos.setAlarma(alarm_flag);

				alarm_flag = false;

				Log.w("MILOG", datos.toString());

				/*
				 * Log.w("aceleracion_x0", ""+aceleracion_x0);
				 * Log.w("delta_aceleracion_x", ""+delta_aceleracion_x);
				 * Log.w("aceleracion_y0", ""+aceleracion_z0);
				 * Log.w("delta_aceleracion_y", ""+delta_aceleracion_y);
				 * Log.w("aceleracion_z0", ""+aceleracion_z0);
				 * Log.w("delta_aceleracion_z", ""+delta_aceleracion_z);
				 * 
				 * Log.w("giroscopio_x0", ""+giroscopio_x0);
				 * Log.w("delta_giroscopio_x", ""+delta_giroscopio_x);
				 * Log.w("giroscopio_y0", ""+giroscopio_z0);
				 * Log.w("delta_giroscopio_y", ""+delta_giroscopio_y);
				 * Log.w("giroscopio_z0", ""+giroscopio_z0);
				 * Log.w("delta_giroscopio_z", ""+delta_giroscopio_z);
				 */

				aceleracion_x0 = 0;
				delta_aceleracion_x = 0;
				aceleracion_y0 = 0;
				delta_aceleracion_y = 0;
				aceleracion_z0 = 0;
				delta_aceleracion_z = 0;
				giroscopio_x0 = 0;
				delta_giroscopio_x = 0;
				giroscopio_y0 = 0;
				delta_giroscopio_y = 0;
				giroscopio_z0 = 0;
				delta_giroscopio_z = 0;

				endpoint.insertDatos(datos).execute();

			} catch (IOException e) {
				e.printStackTrace();
			}

			return (long) 0;
		}
	}

	public void getLatLong() {
		try {
			locationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);

			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				Toast.makeText(getBaseContext(),
						"No está conectada la red ni el gps",
						Toast.LENGTH_SHORT).show();
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER, 1, 1, this);
					if (locationManager != null) {
						Location location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitud = location.getLatitude();
							longitud = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					Location location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, 1, 1, this);
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitud = location.getLatitude();
								longitud = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}

	public void valoresBD() {

		// Cargamos los datos anteriores
		ValoresSQLiteOpenHelper admin = new ValoresSQLiteOpenHelper(this,
				"valores", null, 1);
		SQLiteDatabase bd = admin.getReadableDatabase();

		ContentValues registro = new ContentValues();

		registro.put("pulso", 0);
		registro.put("tension", 0);
		registro.put("azucar", 0);
		registro.put("humedad", 0);
		registro.put("gas", 0);

		bd.insert("valores", null, registro);

		if (bd != null) {

			String[] valores_recuperar = { "pulso", "tension", "azucar",
					"humedad", "gas" };

			Cursor c = bd.query("valores", valores_recuperar, null, null, null,
					null, null, null);

			if (c != null) {
				c.moveToFirst();

				pulso = c.getInt(0);
				tension = c.getInt(1);
				azucar = c.getInt(2);
				temperatura = c.getInt(3);
				gas = c.getInt(4);

				c.close();
			}

		}

	}

}
