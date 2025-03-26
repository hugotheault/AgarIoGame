package sae.launch.agario.models.serverFiles;

import sae.launch.agario.models.serverFiles.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketRunnerConnections implements Runnable{
	
	private Server server;
	
	public ServerSocketRunnerConnections(Server serv) {
		this.server = serv;
	}
    @Override
    public void run() {
    	try {
			ServerSocket serverSocket = new ServerSocket(8081);
			while(true) {
				Socket socket = serverSocket.accept();
				server.addClient(socket);
    		}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
}
