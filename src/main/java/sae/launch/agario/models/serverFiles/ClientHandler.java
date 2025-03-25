package sae.launch.agario.models.serverFiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class ClientHandler extends Thread {
	
	private InputStream inputStream;
	private OutputStream outputStream;
	private PrintWriter printWriter;
	private Server server;
	
	public ClientHandler(InputStream inputStream, OutputStream outputStream, Server serv) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.server = serv;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	@Override
	public void run() {
		while(true) {
			byte[] buffer = new byte[1024]; 
			int bytesRead;
			
			try {
				bytesRead = this.getInputStream().read(buffer);
				if (bytesRead != -1) {
				    String messageRecu = new String(buffer, 0, bytesRead);
				    System.out.println("Message du client : " + messageRecu);
				}
			} catch (IOException e) {
				e.printStackTrace();
				this.server.deleteClient(this, printWriter);
				break;
			}
			
		}
	}

	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}
	
}
