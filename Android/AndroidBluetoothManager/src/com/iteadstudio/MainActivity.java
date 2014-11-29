package com.iteadstudio;

import java.io.IOException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity
{

	// Referencias a elementos gráficos
	Button btnConectarDispositivo;
	Button btnMonitor;
	Button btnConf;
	Button btnModoRC;
	Button btnModoSiguelineasBasico;
	Button btnModoSiguelineasPID;
	Button btnDesconectar;

	// GESTOR DEL MODULO BLUETOOTH
	GestorBluetooth gestorBlue;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Inicializamos el gestor BlueTooth
		gestorBlue = new GestorBluetooth();
		GestorBluetooth.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// -------------------------------------------------------------
		// --------- CONECTAR CON DISPOSITIVO ------------------------
		// -------------------------------------------------------------
		btnConectarDispositivo = (Button) findViewById(R.id.btnConectarDispositivo);
		btnConectarDispositivo.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				gestorBlue.activarBluetooth();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ConectarDispositivoActivity.class);
				startActivity(intent);
			}
		});

		// ---------------------------------------------------------
		// ------------------------- MONITOR -----------------------
		// ---------------------------------------------------------
		btnMonitor = (Button) findViewById(R.id.btnMonitor);
		btnMonitor.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				//gestorBlue.activarBluetooth();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MonitorActivity.class);
				startActivity(intent);
			}
		});
		
		// ---------------------------------------------------------
		// ------------------------- CONF -----------------------
		// ---------------------------------------------------------
		btnConf = (Button) findViewById(R.id.btnConf);
		btnConf.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				//gestorBlue.activarBluetooth();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ConfActivity.class);
				startActivity(intent);
			}
		});


		// ---------------------------------------------------------
		// ------------------------- MODO RC -----------------------
		// ---------------------------------------------------------
		btnModoRC = (Button) findViewById(R.id.btnModoRC);
		btnModoRC.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				//gestorBlue.activarBluetooth();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ModoRCActivity.class);
				startActivity(intent);
			}
		});
		
		// ---------------------------------------------------------
		// ------------------------- MODO SIGUELINEAS BASICO --------------
		// ---------------------------------------------------------
		btnModoSiguelineasBasico = (Button) findViewById(R.id.btnModoSiguelineasBasico);
		btnModoSiguelineasBasico.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				//gestorBlue.activarBluetooth();
				ModoSiguelineasActivity.MODO_SIGUELINEAS[0] = 'M';
				ModoSiguelineasActivity.MODO_SIGUELINEAS[1] = 2;
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ModoSiguelineasActivity.class);
				startActivity(intent);
			}
		});
		
		// ---------------------------------------------------------
		// ------------------------- MODO SIGUELINEAS PID --------------
		// ---------------------------------------------------------
		btnModoSiguelineasPID = (Button) findViewById(R.id.btnModoSiguelineasPID);
		btnModoSiguelineasPID.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				//gestorBlue.activarBluetooth();
				ModoSiguelineasActivity.MODO_SIGUELINEAS[0] = 'M';
				ModoSiguelineasActivity.MODO_SIGUELINEAS[1] = 3;
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ModoSiguelineasActivity.class);
				startActivity(intent);
			}
		});


		// -----------------------------------------------------------
		// ---------- DESCONECTAR DISPOSITIVO ------------------------
		// -----------------------------------------------------------

		btnDesconectar = (Button) findViewById(R.id.btnDesconectar);
		btnDesconectar.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				gestorBlue.desconectarDispositivo();
			}
		});
	}
}