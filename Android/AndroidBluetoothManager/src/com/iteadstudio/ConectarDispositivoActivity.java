package com.iteadstudio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ConectarDispositivoActivity extends Activity
{
	// Lista con dispositivos encontrados
	private ArrayList<BluetoothDevice> listaDispositivos = new ArrayList<BluetoothDevice>();

	//Referencias a elementos gráficos
	ListView listViewDispositivos;
	
	// GESTOR DEL MODULO BLUETOOTH
	GestorBluetooth gestorBlue;


	/**
	 * onCreate()
	 */
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.conectar_dispositivo);
		
		//Gestor Bluetooth
		gestorBlue = new GestorBluetooth();

		// REGISTRAR HANDLER - Se finaliza la búsqueda
		IntentFilter discoveryFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(finBusquedaReceiver, discoveryFilter);

		// REGISTRAR HANDLER - Se ha encontrado un nuevo dispositivo
		IntentFilter foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(nuevoDispositivoReceiver, foundFilter);

		// Iniciamos la búsqueda de dispositivos
		gestorBlue.iniciarBusqueda();
		
		// Inicializamos la lista de dispositivos 
		listViewDispositivos = (ListView) findViewById(R.id.listViewDispositivos);
		listViewDispositivos.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// Cuando se selcciona uno, se finaliza la búsqueda
				for (; gestorBlue.buscando();)
				{
					gestorBlue.finalizarBusqueda();
				}

				// Se establece dicho dispositivo
				GestorBluetooth.bluetothDevice = listaDispositivos.get(position);

				// Se inicia el procedimiento de conexión en un nuevo hilo
				new Thread()
				{
					public void run()
					{
						GestorBluetooth.conectarConDispositivo();
					};
				}.start();

				// Se termina la activity
				finish();				
			}
			
		});
	}
	
	
	
	
	//===========================================================================
	//-------------- HANDLERS Bluetooth --------------------------------
	//===========================================================================

	/**
	 * Un nuevo dispositivo ha sido encontrado
	 */
	private BroadcastReceiver nuevoDispositivoReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			// Obtener el nuevo dispositivo encontrado
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			// Añadirlo a la lsita de dispositivos
			listaDispositivos.add(device);

			// Actualizar la lista en pantalla
			actualizarListaDispositivos();
		}
	};

	/**
	 * Fin del proceso de descubrimiento de dispositivos
	 */
	private BroadcastReceiver finBusquedaReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			// Desregistrar receiver
			Log.d("EF-BTBee", ">>unregisterReceiver");
			unregisterReceiver(nuevoDispositivoReceiver);
			unregisterReceiver(this);
		}
	};


	//===========================================================================
	//-------------- Lista de dispositivos en pantalla --------------------------
	//===========================================================================

	protected void actualizarListaDispositivos()
	{
		List<String> list = new ArrayList<String>();
		if (listaDispositivos.size() > 0)
		{
			for (int i = 0, size = listaDispositivos.size(); i < size; ++i)
			{
				StringBuilder b = new StringBuilder();
				BluetoothDevice d = listaDispositivos.get(i);
				b.append(d.getAddress());
				b.append('\n');
				b.append(d.getName());
				String s = b.toString();
				list.add(s);
			}
		}

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

		listViewDispositivos.setAdapter(adapter);
		
		/* Prompted to select a server to connect */
		Toast.makeText(getBaseContext(), getResources().getString(R.string.selectonedevice), Toast.LENGTH_SHORT).show();
	}

	
}