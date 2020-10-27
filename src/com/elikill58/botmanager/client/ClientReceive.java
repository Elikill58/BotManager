package com.elikill58.botmanager.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import com.elikill58.botmanager.common.SendableMessage;

public class ClientReceive implements Runnable {

	private ObjectInputStream in;
	private final Client client;

	/**
	 * Create a new client receive
	 * 
	 * @param client the client which have to receive data
	 * @param socket the client's socket
	 */
	public ClientReceive(Client client, Socket socket) {
		this.client = client;
		try {
			this.in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean isActive = true;
		while (isActive) {
			try {
				SendableMessage message = (SendableMessage) in.readObject();
				if (message != null) {
					client.messageReceived(message);
				} else {
					isActive = false;
				}
			} catch (SocketException e) {
				System.out.println("Just disconnect to server.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		client.disconnectServer();
	}
}
