package com.iteadstudio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ModoSiguelineasActivity extends Activity {

	public static byte[] MODO_SIGUELINEAS = {'M', '3'};
	private final static byte[] CLAVE_IZQ = { 'E', 'L' };
	private final static byte[] CLAVE_DER = { 'E', 'R' };

	// private int aux = 0;
	// private ArrayList<byte[]> aux_llegadas;

	// Handler para el intercambio de información entre el hilo principal y los
	// secundarios
	private Handler handler;
	private int numRecibidos;

	// Vueltas de cada rueda
	private ArrayList<Float> listaVueltasIzq;
	private ArrayList<Float> listaVueltasDer;

	// Posiciones computadas tras cada numero de vueltas recibidos
	private ArrayList<Float> listaX;
	private ArrayList<Float> listaY;
	private ArrayList<Float> listaTheta;

	// Valores en mm
	private float distanciaRuedas = 130;
	private float perimetroRuedas = (float) (60 * Math.PI);
	private float ticksVuelta = 20;

	// Controles gráficos
	// private Button btnNuevaLlegada;
	private Button btnPintar;
	private TextView textRecibido;

	// Canvas
	MapaView mapaView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.siguelineas);

		
		//mapaView = new MapaView(getApplication(), listaX, listaY);
		//setContentView(mapaView);
		
		//textRecibido = (TextView) findViewById(R.id.TextRecibido);

		// -------------------------------------------------------------
		// --------- INICIALIZACIONES ------------------------
		// -------------------------------------------------------------
		// AUX
		/*
		 * aux_llegadas = new ArrayList<byte[]>(); byte[] llegada = { 'E', 'L',
		 * 0, 0, 'E', 'R', 0, 5 }; aux_llegadas.add(llegada);
		 * aux_llegadas.add(llegada); aux_llegadas.add(llegada);
		 * aux_llegadas.add(llegada); aux_llegadas.add(llegada);
		 * aux_llegadas.add(llegada); aux_llegadas.add(llegada);
		 * aux_llegadas.add(llegada); aux_llegadas.add(llegada);
		 * aux_llegadas.add(llegada); aux_llegadas.add(llegada);
		 * aux_llegadas.add(llegada); aux_llegadas.add(llegada);
		 * aux_llegadas.add(llegada); aux_llegadas.add(llegada);
		 * aux_llegadas.add(llegada); aux_llegadas.add(llegada);
		 */

		//
		listaVueltasDer = new ArrayList<Float>();
		listaVueltasIzq = new ArrayList<Float>();
		listaVueltasDer.add((float) 0.0001);
		listaVueltasIzq.add((float) 0.0001);

		listaX = new ArrayList<Float>();
		listaY = new ArrayList<Float>();
		listaTheta = new ArrayList<Float>();
		listaX.add((float) 0.0001);
		listaY.add((float) 0.0001);
		listaTheta.add((float) 0.0001);

		// -------------------------------------------------------------
		// --------- Boton simulacion llegada ------------------------
		// -------------------------------------------------------------
		/*
		 * btnNuevaLlegada = (Button) findViewById(R.id.btnNuevaLlegada);
		 * btnNuevaLlegada.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { for (byte[] b : aux_llegadas)
		 * { nuevaLlegada(); } } });
		 */

		// -------------------------------------------------------------
		// --------- Boton pintar ------------------------
		// -------------------------------------------------------------
		/*btnPintar = (Button) findViewById(R.id.btnPintar);
		btnPintar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mapaView = new MapaView(getApplication(), listaX, listaY);
				setContentView(mapaView);
			}

		});*/

		// Handler - recibe los mensajes del hilo secundario
		handler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message inputMessage) {
				// Obtiene un texto recibido a través del módulo bluetooth
				int bytes = inputMessage.arg1;
				byte[] buffer = (byte[]) inputMessage.obj;
				nuevaLlegada(buffer);
				mapaView = new MapaView(getApplication(), listaX, listaY);
				setContentView(mapaView);
				
			}
		};

		// Iniciar la lectura de datos
		final ConnectedThread connectedThread = new ConnectedThread(
				GestorBluetooth.socket);
		connectedThread.start();

		// --------------------------------------------------------
		// ----- Iniciar Modo Siguelias--------------------------
		// --------------------------------------------------------
		connectedThread.write(MODO_SIGUELINEAS);

	}

	// =================================================================
	// Funciones auxiliares
	// =================================================================
	private void nuevaLlegada(byte[] buffer) {

		float izquierdo = extraerVueltas(CLAVE_IZQ, buffer);
		float derecho = extraerVueltas(CLAVE_DER, buffer);

		if (izquierdo != 0 || derecho != 0) {
			//textRecibido.setText("Num recibidos = " + numRecibidos + "\n"+ "IZQ: " + izquierdo + "\n" + "DER: " + derecho);
			numRecibidos++;
			// Guardamos nuevas vueltas
			listaVueltasIzq.add(izquierdo);
			listaVueltasDer.add(derecho);

			// Computamos nueva posicion
			float x_old = listaX.get(listaX.size() - 1);
			float y_old = listaY.get(listaY.size() - 1);
			float theta_old = listaTheta.get(listaTheta.size() - 1);

			// media de las distancias recorridas en cada rueda, en mm
			float distancia = (float) ((izquierdo * perimetroRuedas + derecho
					* perimetroRuedas) / 2.0);
			float x, y, theta;

			theta = (float) theta_old
					+ ((izquierdo * perimetroRuedas - derecho * perimetroRuedas) / distanciaRuedas);
			theta = (float) (theta % (Math.PI * 2));

			x = (float) (x_old + distancia * Math.sin(theta));
			y = (float) (y_old + distancia * Math.cos(theta));
			// phi = (float) (phi_old + (izquierdo * perimetroRuedas + derecho *
			// perimetroRuedas ) / distanciaRuedas);

			// Guardar la nueva posicion en arrays
			listaX.add(x);
			listaY.add(y);
			listaTheta.add(theta);
		} /*else {
			textRecibido.setText("Num recibidos = " + numRecibidos + "\n"	+ "Problema en recepcion!!!");
		}*/
		// refrescaPantalla();

	}

	private float extraerVueltas(byte[] clave, byte[] buffer) {
		float vueltas = 0;

		for (int i = 0; i < 5; i++) {
			if (buffer[i] == clave[0] && buffer[i + 1] == clave[1]) {
				vueltas = buffer[i + 2] * 256 + buffer[i + 3];
				break;
			}
		}

		// convertir a vueltas
		vueltas = vueltas / ticksVuelta;
		return vueltas;
	}

	/*
	 * private void refrescaPantalla() {
	 * System.out.println("#############################");
	 * 
	 * for (int i = 0; i < listaVueltasIzq.size(); i++) {
	 * System.out.println("-------------------------"); System.out.println("I:"
	 * + listaVueltasIzq.get(i)); System.out.println("D:" +
	 * listaVueltasDer.get(i)); System.out.println("X:" + listaX.get(i));
	 * System.out.println("Y:" + listaY.get(i)); System.out.println("T:" +
	 * listaTheta.get(i));
	 * 
	 * } }
	 */

	// =================================================================
	// HILO SECUNDARIO: Atiende a mensajes entrantes desde el bluetooth
	// =================================================================
	private class ConnectedThread extends Thread {
		private final BluetoothSocket bluetoothSocket;
		private final InputStream flujoEntrada;
		private final OutputStream flujoSalida;

		public ConnectedThread(BluetoothSocket socket) {
			bluetoothSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			// Get the input and output streams, using temp objects because
			// member streams are final
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
			}

			flujoEntrada = tmpIn;
			flujoSalida = tmpOut;
		}

		public void run() {
			byte[] buffer = new byte[8]; // buffer store for the stream
			int bytes; // bytes returned from read()
			int valor;

			// Keep listening to the InputStream until an exception occurs
			while (true) {
				try {
					// Read from the InputStream
					valor = flujoEntrada.read();
					// Send the obtained bytes to the UI activity

					// consolaSalida.append(mensaje);
					// sTextViewSalida.setText(consolaSalida.toString());

					if (valor == 255) 
					{
						valor = -1;
						for (int num = 0; num <= 7; num++) 
						{
							while (valor == -1) 
							{
								valor = flujoEntrada.read();
							}

							buffer[num] = (byte) valor;
							valor = -1;
						}

						String aux = "";
						for (int i = 0; i < 8; i++) {
							aux = aux + Integer.toHexString(buffer[i]);
						}
						Log.d("run", aux);

						handler.obtainMessage(0, 0, 0, buffer).sendToTarget();
					}
				} catch (IOException e) {
					break;
				}
			}
		}

		/* Call this from the main activity to send data to the remote device */
		public void write(byte[] bytes) {
			try {
				flujoSalida.write(bytes);
			} catch (IOException e) {
			}
		}

		/* Call this from the main activity to shutdown the connection */
		public void cancel() {
			try {
				bluetoothSocket.close();
			} catch (IOException e) {
			}
		}
	}

	// =================================================================
	// CLASE CANVAS: Pinta el mapa del circuito
	// =================================================================
	private class MapaView extends View {
		int width;
		int height;

		private ArrayList<Float> listaX_redim;
		private ArrayList<Float> listaY_redim;

		public MapaView(Context context, ArrayList<Float> listaX,
				ArrayList<Float> listaY) {
			super(context);

			listaX_redim = new ArrayList<Float>();
			listaY_redim = new ArrayList<Float>();
		}

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		private void redimensionarValores(ArrayList<Float> listaX,
				ArrayList<Float> listaY) {
			// Valores maximos y minimos de los vectores
			float minX = minValue(listaX);
			float minY = minValue(listaY);
			float maxX = maxValue(listaX);
			float maxY = maxValue(listaY);

			// Correccion de ambos ejes. Se utilizará la mas restrictiva
			float correccionX = (width - 20) / (maxX - minX);
			float correccionY = (height - 20) / (maxY - minY);
			float correccion, sumaX = 0, sumaY = 0;

			if (correccionX < correccionY) {
				correccion = correccionX;
				sumaY = (float) (height / 2 - correccion
						* ((maxY - minY) / 2.0)); // Para
													// centrar
													// la
													// otra
													// coordenada
			} else {
				correccion = correccionY;
				sumaX = (float) (width / 2 - correccion * ((maxX - minX) / 2.0));// Para
																					// centrar
																					// la
																					// otra
																					// coordenada

			}

			// Se redimensionan ambos vectores
			for (int i = 0; i < listaX.size(); i++) {
				listaX_redim.add(10 + sumaX
						+ ((listaX.get(i) - (minX)) * correccion));
				listaY_redim.add(10 + sumaY
						+ ((listaY.get(i) - (minY)) * correccion));
			}

			System.out.println("MIN_X: " + minX);
			System.out.println("MIN_Y: " + minY);
			System.out.println("MAX_X: " + maxX);
			System.out.println("MAX_Y: " + maxY);
			System.out.println("MAX_Y: " + maxY);
			System.out.println("CORR_X: " + correccionX);
			System.out.println("CORR_Y: " + correccionY);

		}

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		private float maxValue(ArrayList<Float> lista) {
			float max = -1000000;
			for (Float valor : lista) {
				if (valor > max) {
					max = valor;
				}
			}

			return max;
		}

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		private float minValue(ArrayList<Float> lista) {
			float min = 1000000;
			for (Float valor : lista) {
				if (valor < min) {
					min = valor;
				}
			}

			return min;
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			width = getWidth();
			height = getHeight();

			System.out.println("W: " + width);
			System.out.println("H: " + height);

			redimensionarValores(listaX, listaY);

			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.BLUE);
			canvas.drawPaint(paint);
			paint.setColor(Color.BLACK);
			paint.setAntiAlias(true);
			canvas.drawRect(10, 10, getWidth() - 16, getHeight() - 16, paint);


			
			paint.setColor(Color.WHITE);
			paint.setStrokeWidth(6);
			
			//Espello
			

			for (int i = 0; i < listaX.size() - 1; i++) {
				canvas.drawLine(listaX_redim.get(i), height - listaY_redim.get(i),
						listaX_redim.get(i + 1), height -listaY_redim.get(i + 1), paint);
			}
			
			paint.setColor(Color.RED);
			paint.setStrokeWidth(15);
			
			canvas.drawPoint(listaX_redim.get(0), height - listaY_redim.get(0), paint);
			
			paint.setColor(Color.GREEN);
			paint.setStrokeWidth(15);
			
			canvas.drawPoint( listaX_redim.get(listaX.size() - 1), height - listaY_redim.get(listaX.size() - 1), paint);

		}
	}

}