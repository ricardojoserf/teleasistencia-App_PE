/**
 * 
 */
package com.example.app;
/**
 *
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		Button boton = (Button)findViewById(R.id.button1);
		Button boton2 = (Button)findViewById(R.id.button2);
		Button boton3 = (Button)findViewById(R.id.button3);

		ImageView imagen = (ImageView)findViewById(R.id.imageView1);

	    boton.setOnClickListener(new View.OnClickListener() {

	      @Override
	      public void onClick(View view) {
	        Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
	        startActivity(intent);
	      }

	    });
	    
	    boton2.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
		        startActivity(intent);
		      }

		    });
	    
	    boton3.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		        Intent intent = new Intent(MainActivity.this, FourthActivity.class);
		        startActivity(intent);
		      }

		    });
	    
	    
	    imagen.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		    	  
		    	  Toast toast1 = Toast.makeText(getApplicationContext(),"Botón pulsado - Mandando datos", Toast.LENGTH_SHORT);
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
	
	
	

	
	
	
}
