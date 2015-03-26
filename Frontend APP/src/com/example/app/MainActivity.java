package com.example.app;

import java.io.IOException;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private boolean alarm_flag = false;
	private int i = 0;

	/*
	
	Por si fuera necesario. Si no, borrar. 
	 
	private Long id = (long) 0;
	private int latitud = 0;
	private int longitud = 0;
	private int aceleracion_x = 0;
	private int aceleracion_y = 0;
	private int aceleracion_z = 0;
	private int giroscopio_x = 0;
	private int giroscopio_y = 0;
	private int giroscopio_z = 0;
	private int pulso = 0;
	private int tension = 0;
	private int azucar = 0;
	private int temperatura = 0;
	private boolean gas = false;
	private int telefono = 0;
	private int tiempo = 0;*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Button boton = (Button) findViewById(R.id.button1);
		Button boton2 = (Button) findViewById(R.id.button2);
		Button boton3 = (Button) findViewById(R.id.button3);

		ImageView imagen = (ImageView) findViewById(R.id.imageView1);

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

				new EndpointsTask().execute(getApplicationContext());

				Toast toast1 = Toast.makeText(getApplicationContext(),
						"Botón pulsado - Mandando datos", Toast.LENGTH_SHORT);
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
				datos.setAceleracionX(0.0);
				datos.setAceleracionY(0.0);
				datos.setAceleracionZ(0.0);
				datos.setGiroscopioX(0.0);
				datos.setGiroscopioY(0.0);
				datos.setGiroscopioZ(0.0);

				datos.setPulso(0);
				datos.setTension(0.0);
				datos.setAzucar(0.0);

				datos.setTemperatura(0);
				datos.setGas(false);

				datos.setTelefono(0);
				datos.setTiempo(0);

				datos.setAlarma(alarm_flag);

				alarm_flag = false;

				Datos result = endpoint.insertDatos(datos).execute();

			} catch (IOException e) {
				e.printStackTrace();
			}
			return (long) 0;
		}
	}
}
