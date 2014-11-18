package com.iteadstudio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ModoRCActivity extends Activity
{

	private final static byte[] MODO_RC = { 'M', 1 };

	private final static byte[] ADELANTE = {'R','A'};
	private final static byte[] ATRAS = { 'R','B' };
	private final static byte[] IZQUIERDA = { 'R','C' };
	private final static byte[] DERECHA = { 'R','D' };
	private final static byte[] PARAR = { 'R','E' };

	// Referencias a elementos gráficos
	private Button btnAdelante;
	private Button btnAtras;
	private Button btnIzquierda;
	private Button btnDerecha;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modo_rc);

		// Referencias a objetos graficos
		btnAdelante = (Button) findViewById(R.id.btnAdelante);
		btnAtras = (Button) findViewById(R.id.btnAtras);
		btnIzquierda = (Button) findViewById(R.id.btnIzquierda);
		btnDerecha = (Button) findViewById(R.id.btnDerecha);


		// Iniciar El hilo para enviar y recibir
		final ConnectedThread connectedThread = new ConnectedThread(GestorBluetooth.socket);
		connectedThread.start();

		// --------------------------------------------------------
		// ----- Iniciar Modo RC --------------------------------
		// --------------------------------------------------------
		connectedThread.write(MODO_RC);

		// --------------------------------------------------------
		// ----- Click Botón ADELANTE --------------------------------
		// --------------------------------------------------------
		btnAdelante.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					connectedThread.write(ADELANTE);
					return true; // if you want to handle the touch event
				case MotionEvent.ACTION_UP:
					connectedThread.write(PARAR);
					return true; // if you want to handle the touch event
				}
				return false;
			}
		});

		// --------------------------------------------------------
		// ----- Click Botón ATRAS --------------------------------
		// --------------------------------------------------------
		btnAtras.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					connectedThread.write(ATRAS);
					return true; // if you want to handle the touch event
				case MotionEvent.ACTION_UP:
					connectedThread.write(PARAR);
					return true; // if you want to handle the touch event
				}
				return false;
			}
		});

		// --------------------------------------------------------
		// ----- Click Botón IZQUIERDA --------------------------------
		// --------------------------------------------------------
		btnIzquierda.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					connectedThread.write(IZQUIERDA);
					return true; // if you want to handle the touch event
				case MotionEvent.ACTION_UP:
					connectedThread.write(PARAR);
					return true; // if you want to handle the touch event
				}
				return false;
			}
		});

		// --------------------------------------------------------
		// ----- Click Botón DERECHA --------------------------------
		// --------------------------------------------------------
		btnDerecha.setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					connectedThread.write(DERECHA);
					return true; // if you want to handle the touch event
				case MotionEvent.ACTION_UP:
					connectedThread.write(PARAR);
					return true; // if you want to handle the touch event
				}
				return false;
			}
		});

	}

	// =================================================================
	// HILO SECUNDARIO: Envia mensajes a través del bluetooth el bluetooth
	// =================================================================
	private class ConnectedThread extends Thread
	{
		private final BluetoothSocket bluetoothSocket;
		private final InputStream flujoEntrada;
		private final OutputStream flujoSalida;

		public ConnectedThread(BluetoothSocket socket)
		{
			bluetoothSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try
			{
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e)
			{
			}

			flujoEntrada = tmpIn;
			flujoSalida = tmpOut;
		}

		public void run()
		{
			byte[] buffer = new byte[1024]; // buffer store for the stream
			int bytes; // bytes returned from read()

			// Keep listening to the InputStream until an exception occurs
			while (true)
			{
				try
				{
					// Read from the InputStream
					bytes = flujoEntrada.read(buffer);
					// Send the obtained bytes to the UI activity

					// En este modo, no hacemos nada con la info recibida

				} catch (IOException e)
				{
					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes)
		{
			try
			{
				flujoSalida.write(bytes);
			} catch (IOException e)
			{
			}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel()
		{
			try
			{
				bluetoothSocket.close();
			} catch (IOException e)
			{
			}
		}
	}

}