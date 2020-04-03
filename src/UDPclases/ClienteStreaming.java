package UDPclases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

public class ClienteStreaming {

	// Los argumentos proporcionan el mensaje y el nombre del servidor
	public static void main(String args[]) throws InterruptedException {

		try {
			
			ReproductorV reproductor = new ReproductorV();
			
			//Selecciona el video que quiere reproducir y envía al servidor la información correspondiente
			String msj = "./data/"+reproductor.selecciónVideo()+".mp4";
			DatagramSocket socketUDP = new DatagramSocket();
			
			byte[] mensaje = msj.getBytes();
			InetAddress hostServidor = InetAddress.getByName("127.0.0.1");
			int puertoServidor = 8083;

			DatagramPacket peticion = new DatagramPacket(mensaje, msj.length(), hostServidor,puertoServidor);
			
			// Envía el datagrama
			socketUDP.send(peticion);

			// Construimos el DatagramPacket que contendrá la respuesta
			byte[] bufer = new byte[1000];
			DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
			socketUDP.receive(respuesta);
			
			
			System.out.println(new String(respuesta.getData()));
			String numero = new String(respuesta.getData()).split(":")[1];
			String numero1 ="";
			char[] arreglo = numero.toCharArray();
			
			for (char caracter : arreglo){
				if ( Character.isDigit(caracter) )
					numero1 += caracter;
			}
			
			int cantidadBytes = Integer.parseInt(numero1);
			System.out.println(cantidadBytes);
			
			byte[] videoTotal = new byte [cantidadBytes];
			
			int posicion = 0;
			int cantidadProcesada = 0;
			
			while(cantidadProcesada != cantidadBytes) {
				DatagramPacket respuesta1 = new DatagramPacket(bufer, bufer.length);
				socketUDP.receive(respuesta1);
				System.arraycopy( respuesta1.getData(), 0, videoTotal, posicion, respuesta1.getData().length );
				posicion += respuesta1.getData().length;
				cantidadProcesada = videoTotal.length;
				System.out.println("Cantidad procesada "+ cantidadProcesada);
			}
			
			FileOutputStream fr = new FileOutputStream("./dataCliente/video.mp4");
			fr.write(videoTotal);
			fr.close();
			
			File f = new File (msj);
			reproductor.createScene(f);

	
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}
	}
}
