package com.example.app;

public class EventProcessor {

	public void ProcesadorEventos(Datos medidas, long id) { // Usar el id para
		// recuperar cosas
		System.out.println("Empezando el procesado");
		double tension = medidas.getTension();
		double aceleracionx = medidas.getAceleracion_x();
		double aceleraciony = medidas.getAceleracion_y();
		double aceleracionz = medidas.getAceleracion_z();
		double giroscopiox = medidas.getGiroscopio_x();
		double giroscopioy = medidas.getGiroscopio_y();
		double giroscopioz = medidas.getGiroscopio_z();
		double azucar = medidas.getAzucar();
		double carbono = medidas.getGas();
		int pulso = medidas.getPulso();
		int temperatura = medidas.getTemperatura();
		String imei = medidas.getImei();
		int severity = 0;
		String name;
		int type;
		if (medidas.isAlarma()) {
			System.out.println("El paciente ha pulsado el boton de alarma");
			severity = 3;
			name = "DistressAlarm";
			type = 1;
			AlarmSender.sendAlarm(medidas, name, type, severity);
			return;
		}
		double carbonoAv = CarbonoAverage(carbono, id, imei);
		if (((carbonoAv > 200 && carbono > 200) || carbono > 450)
				&& carbonoAv != carbono) {
			// A estos niveles se pude provocar síntomas leves al cabo de 5/6
			// horas
			System.out
					.println("El paciente esta en un lugar con demasiado gas");
			severity = 1;
			if (carbonoAv > 1100)
				severity = 2;// A estos niveles se pude provocar síntomas graves
								// al cabo de 3-5 horas
			if (carbonoAv > 1600 || carbono > 5000)
				severity = 3;// A estos niveles se puede provocar el coma o la
								// muerte al cabo de 2/60 minutos, dependiendo del nivel
			medidas.setAlarma(true);
			name = "Dangerous level of CO";
			type = 5;
			AlarmSender.sendAlarm(medidas, name, type, severity);

		}
		double azucarAv = AzucarAverage(azucar, id, imei);
		if (((azucarAv < 60 && azucar < 60) || azucar < 30)
				&& azucarAv != azucar) {
			System.out.println("El paciente tiene una bajada de azucar");
			severity = 2;
			medidas.setAlarma(true);
			name = "Low level of sugar";
			type = 7;
			AlarmSender.sendAlarm(medidas, name, type, severity);

		} else if ((azucarAv > 110 && azucar > 110) && azucarAv != azucar) {
			System.out.println("El paciente tiene una subida de azucar");
			severity = 1;
			medidas.setAlarma(true);
			name = "High level of sugar";
			type = 8;
			AlarmSender.sendAlarm(medidas, name, type, severity);
		}
		int temperaturaAv = TemperaturaAverage(temperatura, id, imei);
		if (((temperaturaAv > 50 && temperatura > 50) && (carbonoAv > 200 && carbono > 200))
				&& (temperaturaAv != temperatura || carbonoAv != carbono)) {
			System.out.println("Fuego");
			severity = 3;
			medidas.setAlarma(true);
			name = "Fire";
			type = 4;
			AlarmSender.sendAlarm(medidas, name, type, severity);
		}

		if ((Math.abs(aceleraciony) > 20 || Math.abs(aceleracionx) > 20 || Math
				.abs(aceleracionz) > 20)
				&& (pulso < 60 || pulso > 140 || tension < 60 || azucar < 60)
				&& (Math.abs(giroscopiox) > 3 || Math.abs(giroscopioy) > 3 || Math
						.abs(giroscopioz) > 3)) {
			System.out.println("El paciente ha sufrido un desmayo");
			severity = 3;
			medidas.setAlarma(true);
			name = "Fall Down";
			type = 3;
			AlarmSender.sendAlarm(medidas, name, type, severity);

		}
		int pulsoAv = PulsoAverage(pulso, id, imei);
		if (((pulsoAv < 60 && pulso < 60) || pulso < 20) && (pulso != pulsoAv)) {
			medidas.setAlarma(true);
			severity = 2;
			System.out.println("El paciente sufre bradicardia ");
			name = "Bradicardia";
			type = 12;
			if (pulso < 10) {
				severity = 3;
				System.out.println("El paciente esta sufriendo un infarto");
				name = "Heart attack";
				type = 9;
			}
			AlarmSender.sendAlarm(medidas, name, type, severity);

		} else if (pulso < 10 && (pulso != pulsoAv)) {
			severity = 3;
			System.out.println("El paciente esta sufriendo un infarto");
			name = "Heart attack";
			type = 9;
		}

		else if ((pulsoAv > 180 && pulso > 180) && (pulso != pulsoAv)) {
			System.out
					.println("Ritmo cardiaco del paciente demasiado alto, puede causar un ataque");
			if (severity < 2)
				severity = 2;
			medidas.setAlarma(true);
			name = "High heart rate";
			type = 6;
			AlarmSender.sendAlarm(medidas, name, type, severity);
		}
		double tensionAv = TensionAverage(tension, id, imei);
		if ((tensionAv > 89 && tension > 89) && (tension != tensionAv)) {
			severity = 1;
			System.out
					.println("El paciente tiene la tension excesivamente alta");
			medidas.setAlarma(true);
			name = "High blood pressure";
			type = 10;
			AlarmSender.sendAlarm(medidas, name, type, severity);

		} else if (((tensionAv < 50 && tension < 50) || tension < 20)
				&& (tension != tensionAv)) {
			severity = 2;
			System.out
					.println("El paciente tiene una bajada de tension se puede desmayar");
			medidas.setAlarma(true);
			name = "Low blood pressure";
			type = 11;
			AlarmSender.sendAlarm(medidas, name, type, severity);
		}
		System.out.println("Fin del procesamiento");
		DataSender.SendDatosPE(medidas);
	}

	private double AzucarAverage(double Azucar, long id_App, String imei) {
		DatosEndpoint db = new DatosEndpoint();
		long max = 5;
		if (id_App < 6)
			max = id_App - 1;
		for (int i = 1; i <= max; i++) {
			String IdS = "" + (id_App - i) + imei;
			Datos datos = db.getDatos(Long.parseLong(IdS));
			Azucar += datos.getAzucar();
		}
		max++;
		return Azucar / (int) max;
	}

	private double CarbonoAverage(double carbono, long id_App, String imei) {
		DatosEndpoint db = new DatosEndpoint();
		long max = 5;
		if (id_App < 6)
			max = id_App - 1;
		for (int i = 1; i <= max; i++) {
			String IdS = "" + (id_App - i) + imei;
			Datos datos = db.getDatos(Long.parseLong(IdS));
			carbono += datos.getGas();
		}

		max++;
		return carbono / (int) max;
	}

	private int PulsoAverage(int pulso, long id_App, String imei) {
		DatosEndpoint db = new DatosEndpoint();
		long max = 5;
		if (id_App < 6)
			max = id_App - 1;
		for (int i = 1; i <= max; i++) {
			String IdS = "" + (id_App - i) + imei;
			Datos datos = db.getDatos(Long.parseLong(IdS));
			pulso += datos.getPulso();
		}
		max++;
		return pulso / (int) max;
	}

	private double TensionAverage(double tension, long id_App, String imei) {
		DatosEndpoint db = new DatosEndpoint();
		long max = 5;
		if (id_App < 6)
			max = id_App - 1;
		for (int i = 1; i <= max; i++) {
			String IdS = "" + (id_App - i) + imei;
			Datos datos = db.getDatos(Long.parseLong(IdS));
			tension += datos.getTension();
		}

		max++;
		return tension / (int) max;
	}

	private int TemperaturaAverage(int temperatura, long id_App, String imei) {
		DatosEndpoint db = new DatosEndpoint();
		long max = 5;
		if (id_App < 6)
			max = id_App - 1;
		for (int i = 1; i <= max; i++) {
			String IdS = "" + (id_App - i) + imei;
			Datos datos = db.getDatos(Long.parseLong(IdS));
			temperatura += datos.getTension();
		}
		max++;
		return temperatura / (int) max;
	}
}
