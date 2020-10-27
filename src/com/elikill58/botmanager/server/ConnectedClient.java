package com.elikill58.botmanager.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.elikill58.botmanager.common.SendableMessage;

public class ConnectedClient implements Runnable {

	public static int idCounter = 0;
	private final int id;
	private final Socket socket;
	private final Server server;
	private String name;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	/**
	 * Create a new connected client
	 * 
	 * @param server the server which manage this client
	 * @param socket the client socket
	 */
	public ConnectedClient(Server server, Socket socket) {
		this.name = String.valueOf(this.id = (idCounter++));
		this.socket = socket;
		this.server = server;
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
			this.out = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("New client with ID: " + id);
		server.addClient(this);
	}
	
	/**
	 * Get the name of the connected client
	 * 
	 * @return the client name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Change the client name
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void run() {
		while(socket.isConnected()) {
			try {
				if(in == null) {
					System.out.println("Failed to get input stream.");
					closeClient();
					return;
				}
				SendableMessage m = (SendableMessage) in.readObject();
				if(m == null) {
					server.disconnectClient(this);
					return;
				}
				name = m.getName();
				server.broadcastMessage(m, id);
			} catch (EOFException e) {
				System.out.println("User " + id +  " disconnected.");
				break;
			} catch (SocketException e) {
				System.out.println("User " + id +  " just disconnect.");
				return;
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * Get the new client ID
	 * 
	 * @return client ID
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Send a message to the client
	 * 
	 * @param m the message to send
	 */
	public void sendMessage(SendableMessage m) {
		try {
			out.writeObject(m);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Close all clients connection
	 * (in/out/socket)
	 */
	public void closeClient() {
		if(socket != null && socket.isConnected()) {
			try {
				if(in != null)
					in.close();
				if(out != null)
					out.close();
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
