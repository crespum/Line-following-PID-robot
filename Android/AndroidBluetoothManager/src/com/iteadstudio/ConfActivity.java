package com.iteadstudio;

import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConfActivity extends Activity
{
	private final static byte[] VELOCIDAD_A = {'V','A'};
	private final static byte[] VELOCIDAD_B = {'V','B'};
	private final static byte[] VELOCIDAD_C = {'V','C'};
	private final static byte[] VELOCIDAD_D = {'V','D'};
	private final static byte[] VELOCIDAD_E = {'V','E'};
	private final static byte[] VELOCIDAD_F = {'V','F'};

	// Referencias a elementos gráficos
	Button btnVA;
	Button btnVB;
	Button btnVC;
	Button btnVD;
	Button btnVE;
	Button btnVF;
	
	//
	OutputStream out;

	// GESTOR DEL MODULO BLUETOOTH
	GestorBluetooth gestorBlue;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conf);

		// Stream de salida
		try
		{
			out =	GestorBluetooth.socket.getOutputStream();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// -------------------------------------------------------------
		// --------- VELOCIDAD A ------------------------
		// -------------------------------------------------------------
		btnVA = (Button) findViewById(R.id.btnVA);
		btnVA.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					out.write(VELOCIDAD_A);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// -------------------------------------------------------------
		// --------- VELOCIDAD B ------------------------
		// -------------------------------------------------------------
		btnVB = (Button) findViewById(R.id.btnVB);
		btnVB.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					out.write(VELOCIDAD_B);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		
		// -------------------------------------------------------------
		// --------- VELOCIDAD C ------------------------
		// -------------------------------------------------------------
		btnVC = (Button) findViewById(R.id.btnVC);
		btnVC.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					out.write(VELOCIDAD_C);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		
		// -------------------------------------------------------------
		// --------- VELOCIDAD D ------------------------
		// -------------------------------------------------------------
		btnVD = (Button) findViewById(R.id.btnVD);
		btnVD.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					out.write(VELOCIDAD_D);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		
		// -------------------------------------------------------------
		// --------- VELOCIDAD E ------------------------
		// -------------------------------------------------------------
		btnVE = (Button) findViewById(R.id.btnVE);
		btnVE.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					out.write(VELOCIDAD_E);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		
		// -------------------------------------------------------------
		// --------- VELOCIDAD F ------------------------
		// -------------------------------------------------------------
		btnVF = (Button) findViewById(R.id.btnVF);
		btnVF.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				try
				{
					out.write(VELOCIDAD_F);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


	}
}