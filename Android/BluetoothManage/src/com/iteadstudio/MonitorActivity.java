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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MonitorActivity extends Activity
{

	// Handler para el intercambio de información entre el hilo principal y los secundarios
	private Handler handler;

	// Referencias a elementos gráficos
	private EditText sEditText;
	private TextView sTextViewSalida;
	private TextView sTextViewEntrada;
	private Button btnClear;
	private Button btnSend;

	// Texto de las consolas de entrada y de salida
	public static StringBuffer consolaSalida = new StringBuffer("SALIDA: ");
	public static StringBuffer consolaEntrada = new StringBuffer("ENTRADA: ");

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitor);

		// Referencias a objetos graficos
		sTextViewSalida = (TextView) findViewById(R.id.textViewSalida);
		sTextViewEntrada = (TextView) findViewById(R.id.textViewEntrada);
		sEditText = (EditText) findViewById(R.id.sEditText);
		btnClear = (Button) findViewById(R.id.btnClear);
		btnSend = (Button) findViewById(R.id.btnSend);

		// Handler - recibe los mensajes del hilo secundario 
		handler = new Handler(Looper.getMainLooper())
		{
			@Override
			public void handleMessage(Message inputMessage)
			{
				// Obtiene un texto recibido a través del módulo bluetooth
				// y actualiza la consola de entrada
				String mensaje = (String) inputMessage.obj;
				consolaEntrada.append(mensaje);
				sTextViewEntrada.setText(consolaEntrada.toString());
			}
		};

		// Iniciar la lectura de datos
		final ConnectedThread connectedThread = new ConnectedThread(GestorBluetooth.socket);
		connectedThread.start();

		// Inicializar texto en consolas
		sTextViewEntrada.setText(consolaEntrada.toString());
		sTextViewSalida.setText(consolaSalida.toString());

		//--------------------------------------------------------
		//----- Limpiar consolas --------------------------------
		//--------------------------------------------------------
		btnClear.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				consolaSalida = new StringBuffer("SALIDA: ");
				sTextViewSalida.setText(consolaSalida.toString());

				consolaEntrada = new StringBuffer("ENTRADA: ");
				sTextViewEntrada.setText(consolaEntrada.toString());
			}
		});
		
		//--------------------------------------------------------
		//----- Enviar información --------------------------------
		//--------------------------------------------------------
		btnSend.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				//Obtener texto
				String editText = sEditText.getText().toString();

				//Convertir texto en bytes
				byte bytes[];
				try
				{
					bytes = Util.hexToByte(editText);
				} catch (Exception e)
				{
					e.printStackTrace();
					Toast.makeText(getBaseContext(), getResources().getString(R.string.number), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				
				//Actualizar consola de salida
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < editText.length(); i = i + 2)
				{
					sb.append(editText.substring(i, i + 2)).append(" ");
				}
				consolaSalida.append(sb);
				sTextViewSalida.setText(consolaSalida.toString());

				//Enviar bytes
				connectedThread.write(bytes);
			}
		});

	}


	// =================================================================
	// HILO SECUNDARIO: Atiende a mensajes entrantes desde el bluetooth
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
					
					String mensaje = "";
					for (int i = 0; i < bytes; i++)
					{
						mensaje = mensaje + (char) buffer[i];
					}
					//consolaSalida.append(mensaje);
					//sTextViewSalida.setText(consolaSalida.toString());

					 handler.obtainMessage(0, 0, 0, mensaje).sendToTarget();
					
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