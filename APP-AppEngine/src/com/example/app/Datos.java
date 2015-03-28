package com.example.app;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Datos {
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private double latitud;
	@Persistent
	private double longitud;
	
	@Persistent
	private float aceleracion_x;
	@Persistent
	private float aceleracion_y;
	@Persistent
	private float aceleracion_z;
	
	@Persistent	
	private float giroscopio_x;
	@Persistent
	private float giroscopio_y;
	@Persistent
	private float giroscopio_z;
	
	@Persistent
	private int pulso;
	@Persistent
	private double tension;
	@Persistent
	private double azucar;
	
	@Persistent
	private int temperatura;
	@Persistent
	private boolean gas;
	
	@Persistent
	private String imei;
	@Persistent
	private Long tiempo;
	
	@Persistent
	private boolean alarma;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public float getAceleracion_x() {
		return aceleracion_x;
	}

	public void setAceleracion_x(float aceleracion_x) {
		this.aceleracion_x = aceleracion_x;
	}

	public float getAceleracion_y() {
		return aceleracion_y;
	}

	public void setAceleracion_y(float aceleracion_y) {
		this.aceleracion_y = aceleracion_y;
	}

	public float getAceleracion_z() {
		return aceleracion_z;
	}

	public void setAceleracion_z(float aceleracion_z) {
		this.aceleracion_z = aceleracion_z;
	}

	public float getGiroscopio_x() {
		return giroscopio_x;
	}

	public void setGiroscopio_x(float giroscopio_x) {
		this.giroscopio_x = giroscopio_x;
	}

	public float getGiroscopio_y() {
		return giroscopio_y;
	}

	public void setGiroscopio_y(float giroscopio_y) {
		this.giroscopio_y = giroscopio_y;
	}

	public float getGiroscopio_z() {
		return giroscopio_z;
	}

	public void setGiroscopio_z(float giroscopio_z) {
		this.giroscopio_z = giroscopio_z;
	}

	public int getPulso() {
		return pulso;
	}

	public void setPulso(int pulso) {
		this.pulso = pulso;
	}

	public double getTension() {
		return tension;
	}

	public void setTension(double tension) {
		this.tension = tension;
	}

	public double getAzucar() {
		return azucar;
	}

	public void setAzucar(double azucar) {
		this.azucar = azucar;
	}

	public int getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(int temperatura) {
		this.temperatura = temperatura;
	}

	public boolean isGas() {
		return gas;
	}

	public void setGas(boolean gas) {
		this.gas = gas;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Long getTiempo() {
		return tiempo;
	}

	public void setTiempo(Long tiempo) {
		this.tiempo = tiempo;
	}

	public boolean isAlarma() {
		return alarma;
	}

	public void setAlarma(boolean alarma) {
		this.alarma = alarma;
	}

	
		
}