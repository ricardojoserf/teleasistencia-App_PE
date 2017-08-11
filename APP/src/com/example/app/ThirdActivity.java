package com.example.app;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
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
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ThirdActivity extends Activity implements LocationListener,
		SensorEventListener {

	private LocationManager locationManager;
	boolean isGPSEnabled = false;
	boolean isNetworkEnabled = false;
	boolean canGetLocation = false;
	//Location location; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.third_activity);

		ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton1);

	
		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		List<Sensor> listaSensores = sensorManager
				.getSensorList(Sensor.TYPE_ALL);

		// Cargamos listeners de acelerómetro y giroscopio

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

		cargarBaseDeDatos();
		
		getLocation();
		


		imageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(ThirdActivity.this,
						MainActivity.class);
				startActivity(intent);
			}

		});

	}

	public void cargarBaseDeDatos() {

		// Cargamos los datos anteriores
		ValoresSQLiteOpenHelper admin = new ValoresSQLiteOpenHelper(this,"valores", null, 1);
		SQLiteDatabase bd = admin.getReadableDatabase();

		ContentValues registro = new ContentValues();

		registro.put("pulso", 0);
		registro.put("tension", 0);
		registro.put("azucar", 0);
		registro.put("humedad", 0);
		registro.put("gas", 0);

		bd.insert("valores", null, registro);
		
		if (bd != null) {

			String[] valores_recuperar = { "pulso", "tension", "azucar", "humedad", "gas" };

			Cursor c = bd.query("valores", valores_recuperar, null, null, null, null, null, null);

			if (c != null) {
				c.moveToFirst();

				EditText text2 = (EditText) findViewById(R.id.editText2);
				EditText text3 = (EditText) findViewById(R.id.editText3);
				EditText text4 = (EditText) findViewById(R.id.editText4);
				EditText text9 = (EditText) findViewById(R.id.editText9);
				EditText text10 = (EditText) findViewById(R.id.editText10);

				text2.setText(c.getString(0));
				text3.setText(c.getString(1));
				text4.setText(c.getString(2));
				text9.setText(c.getString(3));
				text10.setText(c.getString(4));

				c.close();
			}

		}

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

	public void getLocation() {
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
							EditText text7 = (EditText) findViewById(R.id.editText7);
							EditText text8 = (EditText) findViewById(R.id.editText8);
							text7.setText(String.valueOf(location.getLatitude()));
							text8.setText(String.valueOf(location.getLongitude()));
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, 1, 1, this);
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								EditText text7 = (EditText) findViewById(R.id.editText7);
								EditText text8 = (EditText) findViewById(R.id.editText8);
								text7.setText(String.valueOf(location.getLatitude()));
								text8.setText(String.valueOf(location.getLongitude()));
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

	

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent evento) {
		synchronized (this) {

			switch (evento.sensor.getType()) {

			case Sensor.TYPE_GYROSCOPE:

				EditText text6 = (EditText) findViewById(R.id.editText6);
				String gX = new DecimalFormat("#.##").format(evento.values[0]);
				String gY = new DecimalFormat("#.##").format(evento.values[1]);
				String gZ = new DecimalFormat("#.#").format(evento.values[2]);
				text6.setText(gX+"/"+gY+"/"+gZ);

				break;

			case Sensor.TYPE_ACCELEROMETER:

				EditText text5 = (EditText) findViewById(R.id.editText5);
				String aX = new DecimalFormat("#.##").format(evento.values[0]);
				String aY = new DecimalFormat("#.##").format(evento.values[1]);
				String aZ = new DecimalFormat("#.#").format(evento.values[2]);
				text5.setText(aX+"/"+aY+"/"+aZ);

				break;
			}
		}

	}

}