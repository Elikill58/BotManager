package com.elikill58.botmanager.client;

public class MainClient {

	public static void main(String[] args) {
		IHM.run(new Client("127.0.0.1", 10000), args);
	}
}
