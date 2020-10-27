package com.elikill58.botmanager.common;

public class Message extends SendableMessage {

	
	private static final long serialVersionUID = -5002633107567001554L;

	private int id;
	private String message, name;
	
	public Message(int id, String message, String name) {
		this.id = id;
		this.message = message;
		this.name = name;
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toShow() {
		return message;
	}

}
