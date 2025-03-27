package sae.launch.agario.models.serverFiles;

import sae.launch.agario.controllers.OnlineInGameController;
import sae.launch.agario.models.IDGenerator;
import sae.launch.agario.models.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {
	private Thread askConnexion = new Thread(new ServerSocketRunnerConnections(this));
	private Thread threadWorld = new Thread(new ServerSocketRunnerWorld(this));
	private ArrayList<ClientHandler> clientConnectionList = new ArrayList<ClientHandler>();
	private ArrayList<PrintWriter> printWriterList = new ArrayList();
	private OnlineInGameController onlineInGameController;
	
	public Server(OnlineInGameController onlineInGameController) {
		try {
			threadWorld.start();
			askConnexion.start();
			this.onlineInGameController = onlineInGameController;

		}catch(Exception exception){
			System.out.println(exception.getMessage());
		}
	}
	
	
	public Thread getAskConnexion() {
		return askConnexion;
	}



	public Thread getThreadWorld() {
		return threadWorld;
	}



	public ArrayList<ClientHandler> getClientConnexionList() {
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

			synchronized (this.getPrintWriterList()) {
				this.getPrintWriterList().add(new PrintWriter(clientHandler.getOutputStream(), false));
				PrintWriter lastPrintWriter = this.getPrintWriterList().get(this.getPrintWriterList().size()-1);

				clientHandler.setPrintWriter(lastPrintWriter);
				clientHandler.start();
				this.getClientConnexionList().add(clientHandler);


				this.getPrintWriterList().get(this.getPrintWriterList().size() - 1).write("Vous êtes connecté");

				Random random = new Random();
				int ID = IDGenerator.getGenerator().NextID();
				System.out.println("Longueur : " + this.onlineInGameController.getQuadTree().getLength());
				System.out.println("Largeur : " + this.onlineInGameController.getQuadTree().getHeight());
				this.onlineInGameController.getPlayers().add(new Player(ID,
						random.nextDouble(this.onlineInGameController.getQuadTree().getLength()),
						random.nextDouble(this.onlineInGameController.getQuadTree().getHeight()),
						onlineInGameController.getInitialSize()));
				this.getPrintWriterList().get(this.getPrintWriterList().size() - 1).write("id:"+ID);
				this.getPrintWriterList().get(this.getPrintWriterList().size()-1).flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteClient(ClientHandler ch, PrintWriter printWriter) {

		synchronized (this.getClientConnexionList()) {
			this.getClientConnexionList().remove(ch);
		}
		synchronized (this.getPrintWriterList()) {
			this.getPrintWriterList().remove(printWriter);
		}
	}
}
