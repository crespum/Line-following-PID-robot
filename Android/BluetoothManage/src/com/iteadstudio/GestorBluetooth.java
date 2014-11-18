package com.iteadstudio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

public class GestorBluetooth
{
	// Se han establecido los objetos más relevantes como estáticos para
	// poder acceder a ellos desde las distintas activities
	public static BluetoothAdapter bluetoothAdapter;
	public static BluetoothDevice bluetothDevice;
	public static BluetoothSocket socket;


	public GestorBluetooth()
	{

	}

	/**
	 * Activa el adaptador de bluetooth. Es necesario activarlo antes de hacer
	 * ninguna acción
	 */
	public void activarBluetooth()
	{
		bluetoothAdapter.enable();
	}

	/**
	 * Se desconecta del dispositivo actual al que pueda estar conectado
	 * cerrando el socket entre ambos. Si no está conectado a ningún
	 * dispositivo, no realiza ninguna acción
	 */
	public void desconectarDispositivo()
	{
		if (socket != null)
		{
			try
			{
				socket.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			socket = null;
		}
	}

	/**
	 * Inicia la búsqueda de dispositivos
	 */
	public void iniciarBusqueda()
	{
		bluetoothAdapter.startDiscovery();
	}

	/**
	 * Informa si actualmente la búsqueda está en proceso
	 */
	public boolean buscando()
	{
		return bluetoothAdapter.isDiscovering();
	}

	/**
	 * Finaliza la búsqueda de dispositivos
	 */
	public void finalizarBusqueda()
	{
		bluetoothAdapter.cancelDiscovery();
	}

	/**
	 * Procede a conectarse con el dispositivo dado. Si este dispositivo no está
	 * emparejado con este teléfono, se solicitará la contraseña.
	 * 
	 */
	public static void conectarConDispositivo()
	{

		try
		{
			// Creamos un socket de conexión
			GestorBluetooth.socket = bluetothDevice.createRfcommSocketToServiceRecord(UUID
					.fromString("00001101-0000-1000-8000-00805F9B34FB"));

			GestorBluetooth.socket.connect();
			
		} catch (IOException e)
		{
			Log.e("", ">> Imposible conectar", e);
			return;
		}
	}

}
