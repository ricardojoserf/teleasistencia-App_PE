
package com.example.app;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SecondActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);
		
		ImageButton imageButton = (ImageButton)findViewById(R.id.imageButton1);
		
		
		cargarBaseDeDatos();
	
		
		
		imageButton.setOnClickListener(new View.OnClickListener() {

		      @Override
		      public void onClick(View view) {
		    	  
		    	  
		    	  
		    	  EditText text2 = (EditText)findViewById(R.id.editText2);
		  		  EditText text3 = (EditText)findViewById(R.id.editText3);
		  		  EditText text4 = (EditText)findViewById(R.id.editText4);
		  		  EditText text5 = (EditText)findViewById(R.id.editText5);
		  		  EditText text6 = (EditText)findViewById(R.id.editText6);

		  		  
		  		  
		  		  int v1 = Integer.parseInt(text2.getText().toString());
		  		  int v2 = Integer.parseInt(text3.getText().toString());
		  		  int v3 = Integer.parseInt(text4.getText().toString());
		  		  int v4 = Integer.parseInt(text5.getText().toString());
		  		  int v5 = Integer.parseInt(text6.getText().toString());
		  		  

		  		  if((v1>-1)&&(v2>-1)&&(v3>-1)&&(v4>-1)&&(v5>-1)){
		  			  
			  		  borrarBaseDeDatos();
			  		
			    	  alta(v1,v2,v3,0,0,0,0,v4,v5);
			    	  
		  		  }
		  		  
		  		  Intent intent = new Intent(SecondActivity.this, MainActivity.class);
			      startActivity(intent);
			      
		      }

		 });
	  
		
		
	}

	
	public void borrarBaseDeDatos() {

		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 2);
		SQLiteDatabase bd = admin.getWritableDatabase();

		bd.delete("valores", null, null);

		bd.close();
		
	}
	
	public void alta(int input1, int input2, int input3, int input4, int input5, int input6, int input7, int input8, int input9) {
		
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 2);
        SQLiteDatabase bd = admin.getWritableDatabase();
              
               
        int pulso 			= input1;
        int tension 		= input2;
        int azucar 			= input3;
        int humedad 		= input8;
        int gas 			= input9;
        
        if((pulso>=0)&&(tension>=0)&&(azucar>=0)&&(humedad>=0)&&(gas>=0)){
        	
	        ContentValues registro = new ContentValues();
	        
	        registro.put("pulso", pulso);
	        registro.put("tension", tension);
	        registro.put("azucar", azucar);
	        
	        registro.put("aceleracion", 0);
	        registro.put("giroscopio", 0);
	        registro.put("latitud", 0);
	        registro.put("longitud", 0);	        
	        
	        registro.put("humedad", humedad);
	        registro.put("gas", gas);


	       
	        bd.insert("valores", null, registro);
	        
	        bd.close();
	        
	        //Toast.makeText(this, "Valores guardados correctamente.", Toast.LENGTH_SHORT).show();
       
        }
        
    }

	
	public void cargarBaseDeDatos(){
		
		
		
		// Cargamos los datos anteriores	        	        
		AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 2);
		SQLiteDatabase bd = admin.getReadableDatabase();

		
		ContentValues registro = new ContentValues();

		registro.put("pulso", 0);
		registro.put("tension", 0);
		registro.put("azucar", 0);
		
		registro.put("aceleracion", 0);
		registro.put("giroscopio", 0);
		registro.put("latitud", 0);
		registro.put("longitud", 0);
		
		registro.put("humedad", 0);
		registro.put("gas", 0);
		
		bd.insert("valores", null, registro);

		if (bd != null) {
        	      	       	
			String[] valores_recuperar = { "pulso", "tension", "azucar",
					"aceleracion","giroscopio","latitud","longitud",
					"humedad","gas"};

            
        	Cursor c = bd.query("valores", valores_recuperar, null, null, null, null, null, null);
        	
        	if(c!=null){
	           
        		
	        	c.moveToFirst();
	        		
	        	EditText text2 = (EditText)findViewById(R.id.editText2);
	        	EditText text3 = (EditText)findViewById(R.id.editText3);
	    		EditText text4 = (EditText)findViewById(R.id.editText4);
	    		EditText text5 = (EditText)findViewById(R.id.editText5);
	    		EditText text6 = (EditText)findViewById(R.id.editText6);
	    		
	    		if(c.getString(0)!=null){
		        	text2.setText(c.getString(0));
		        	text3.setText(c.getString(1));
		        	text4.setText(c.getString(2));
		        	text5.setText(c.getString(7));
		        	text6.setText(c.getString(8));
	    		}
	          
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
	
		
}
