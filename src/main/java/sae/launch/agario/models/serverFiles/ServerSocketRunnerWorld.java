package sae.launch.agario.models.serverFiles;

import java.io.PrintWriter;

public class ServerSocketRunnerWorld implements Runnable{
	
	private Server server;
	
	public ServerSocketRunnerWorld(Server serv) {
		this.server = serv;
	}

	/**
	 * Listen to the world to see its evolution
	 */
	@Override
	public void run() {
		while(true) {
			for(PrintWriter pt : server.getPrintWriterList()) {
				
			}
		}
	}
		
}
