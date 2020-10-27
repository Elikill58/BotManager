package com.elikill58.botmanager.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection implements Runnable {

	private final Server server;
	private ServerSocket serverSocket;
	
	/**
	 * Create a server which will receive multiples clients connections
	 * 
	 * @param server the server
	 */
	public Connection(Server server) {
		this.server = server;
		try {
			this.serverSocket = new ServerSocket(server.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		boolean active = true;
		while(active) {
			try {
				System.out.println("Loading new clients");
				Socket clientSocket = serverSocket.accept();
				System.out.println("new client on " + clientSocket.getPort());
				new Thread(new ConnectedClient(server, clientSocket)).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
