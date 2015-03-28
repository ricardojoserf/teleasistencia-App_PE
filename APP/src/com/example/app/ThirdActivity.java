package com.example.app;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class ThirdActivity extends Activity implements LocationListener,
		SensorEventListener {

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

		// Estaría guay que en la pantalla de datos apareciese un mapa en vez de
		// números, pero si es complicado... Nada

		// GPS ...
		/*
		 * 
		 * 
		 * 
		 * LocationManager mlocManager = (LocationManager)
		 * getSystemService(Context.LOCATION_SERVICE); boolean gps_enabled =
		 * mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER); boolean
		 * network_enabled =
		 * mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		 * 
		 * 
		 * 
		 * 
		 * if (network_enabled && gps_enabled) {
		 * 
		 * Toast.makeText(getBaseContext(),"Está conectada la red",
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * LocationListener mlocListener = new ThirdActivity();
		 * mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
		 * 0, mlocListener); Criteria hdCrit = new Criteria();
		 * hdCrit.setAccuracy(Criteria.ACCURACY_COARSE); String mlocProvider =
		 * mlocManager.getBestProvider(hdCrit, true); Location currentLocation =
		 * mlocManager.getLastKnownLocation(mlocProvider);
		 * //text7.setText(""+currentLocation.getLatitude());
		 * //text8.setText(""+currentLocation.getLongitude()); }
		 */
		// ... GPS

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
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
				"administracion", null, 2);
		SQLiteDatabase bd = admin.getReadableDatabase();

		if (bd != null) {

			String[] valores_recuperar = { "pulso", "tension", "azucar",
					"aceleracion", "giroscopio", "latitud", "longitud",
					"humedad", "gas" };

			Cursor c = bd.query("valores", valores_recuperar, null, null, null,
					null, null, null);

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
				text9.setText(c.getString(7));
				text10.setText(c.getString(8));

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

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onLocationChanged(Location loc) {
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent evento) {
		synchronized (this) {

			// Lo suyo es que se muestre el valor en la casilla correspondiente
			// (que aún no se ha creado).

			switch (evento.sensor.getType()) {

			case Sensor.TYPE_GYROSCOPE:

				/*
				 * aceleracion_x = evento.values[0]; aceleracion_y =
				 * evento.values[1]; aceleracion_z = evento.values[2];
				 */

				break;

			case Sensor.TYPE_ACCELEROMETER:

				/*
				 * giroscopio_x = evento.values[0]; giroscopio_y =
				 * evento.values[1]; giroscopio_z = evento.values[2];
				 */

				break;

			}

		}

	}

}
