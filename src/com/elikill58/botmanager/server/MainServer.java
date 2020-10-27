package com.elikill58.botmanager.server;

public class MainServer {

	public static void main(String[] args) {
		new Server(10000);
	}

	public static void printUsage() {
		System.out.println("java server.Server <port>");
		System.out.println("\t<port>: server's port");
	}
}
