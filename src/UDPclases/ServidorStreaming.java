package UDPclases;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.swing.Timer;

import org.apache.commons.io.IOUtils;

public class ServidorStreaming {
	
	public static void main (String args[]) {

		try {		
			DatagramSocket socketUDP = new DatagramSocket(8083);
			
			//Allocate memory for the sending buffer
			byte[] bufer = new byte[1000];

			while (true) {			
				// Se construye el DatagramPacket para recibir peticiones
				DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);

				// Leemos una petición del DatagramSocket
				socketUDP.receive(peticion);
				System.out.print("Datagrama recibido del host: " + peticion.getAddress());
				System.out.println("\nDesde el puerto remoto: " + peticion.getPort());
				
				//Lee la ruta del archivo que se quiere transmitir
				String ruta = new String(peticion.getData());
				System.out.println("Ruta de archivo " + ruta);
				
				File f = new File("./data/Morado.mp4");
				FileInputStream fichero = new FileInputStream(f);
				byte [] byteArr = IOUtils.toByteArray(fichero);

				byte [] rta = new byte [1000];
				int num = 0;
				String m1 = "HOLA:" + byteArr.length;

				DatagramPacket respuesta1 = armarPaquete(m1.getBytes(), peticion);
				socketUDP.send(respuesta1);

				for (int i = 0; i < byteArr.length; i++) {
					if(i%1000000 == 0)
					{
						System.out.println("Procesando video");	
					}	
					if(i%999 == 0 && i!=0) {				
						socketUDP.send(armarPaquete(rta, peticion));
						num = 0;
						if(byteArr.length-i < 1000) {
							rta = new byte[byteArr.length-i+1];
						}
					}
					num++;
					rta[num] = byteArr[i];	
				}
				System.out.println("video enviado");
			}

		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
	}

	private static DatagramPacket armarPaquete(byte[] respuesta, DatagramPacket peticion)
	{
		DatagramPacket pack = new DatagramPacket(respuesta, respuesta.length, peticion.getAddress(), peticion.getPort());
		return pack;
	}

}
