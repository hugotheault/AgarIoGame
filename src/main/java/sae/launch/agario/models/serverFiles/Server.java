package sae.launch.agario.models.serverFiles;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private Thread askConnexion = new Thread(new ServerSocketRunnerConnections(this));
	private Thread threadWorld = new Thread(new ServerSocketRunnerWorld(this));
	private ArrayList<Thread> clientConnectionList = new ArrayList<Thread>();
	private ArrayList<PrintWriter> printWriterList = new ArrayList<PrintWriter>();
	
	public Server() {
		threadWorld.start();
		askConnexion.start();
	}
	
	
	public Thread getAskConnexion() {
		return askConnexion;
	}



	public Thread getThreadWorld() {
		return threadWorld;
	}



	public ArrayList<Thread> getClientConnexionList() {
		return clientConnectionList;
	}



	public ArrayList<PrintWriter> getPrintWriterList() {
		return printWriterList;
	}



	/*
	* @parameters : The socket managing the connection to the server
	* @does : Add a PrintWriter and a ClientHandler to the lists
	* 			starts the ClientHandler thread
	*
	* */
	public void addClient(Socket socket) {
		ClientHandler clientHandler;
		try {
			clientHandler = new ClientHandler(socket.getInputStream(), socket.getOutputStream(), this);
			//Le "false" indique que l'écriture dans l'outputStream  ne se fera qu'à l'appel de la fonction flush
			this.getPrintWriterList().add(new PrintWriter(clientHandler.getOutputStream(), false));
			
			PrintWriter lastPrintWriter = this.getPrintWriterList().get(this.getPrintWriterList().size()-1);
			clientHandler.setPrintWriter(lastPrintWriter);
			clientHandler.start();
			this.getClientConnexionList().add(clientHandler);
			this.getPrintWriterList().get(this.getPrintWriterList().size()-1).write("Bonjour");
			this.getPrintWriterList().get(this.getPrintWriterList().size()-1).flush();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteClient(ClientHandler ch, PrintWriter printWriter) {
		
		this.getClientConnexionList().remove(ch);
		PrintWriter nouveauPt = new PrintWriter(ch.getOutputStream(), false);
		this.getPrintWriterList().remove(printWriter);
	}
}
