package com.iteadstudio;

import android.util.Log;

public class Util
{
	
	/**
	 * String -> Hex
	 * 
	 * @param s
	 * @return
	 */
	public static String stringToHex(String s)
	{
		String str = "";
		for (int i = 0; i < s.length(); i++)
		{
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			if (s4.length() == 1)
			{
				s4 = '0' + s4;
			}
			str = str + s4 + " ";
		}
		return str;
	}

	/**
	 * Hex -> String
	 * 
	 * @param s
	 * @return
	 */
	public static String hexToString(String s)
	{
		String[] strs = s.split(" ");
		byte[] baKeyword = new byte[strs.length];
		for (int i = 0; i < baKeyword.length; i++)
		{
			try
			{
				baKeyword[i] = (byte) (0xff & Integer.parseInt(strs[i], 16));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			s = new String(baKeyword, "utf-8");// UTF-16le:Not
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return s;
	}

	/**
	 * Convierte una cadena de texto en formato HEX a bytes
	 * 
	 * @param s cadena de texto que se desea convertir (EJ: 01020AFEA30F)
	 * @return 1 byte cada dos caracteres de la cadena de entrada
	 * @throws Exception
	 */
	public static byte[] hexToByte(String s) throws Exception
	{
		//Filtrar el 0x
		/*if ("0x".equals(s.substring(0, 2)))
		{
			s = s.substring(2);
		}*/
		
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++)
		{
			try
			{
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
				Log.d("MonitorActivity", baKeyword[i] + ",");
			} catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
		}
		return baKeyword;
	}

	/**
	 * Byte -> Hex
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteToHex(byte[] bytes, int count)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++)
		{
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1)
			{
				hex = '0' + hex;
			}
			sb.append(hex).append(" ");
		}
		return sb.toString();
	}
}
