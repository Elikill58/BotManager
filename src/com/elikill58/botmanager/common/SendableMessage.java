package com.elikill58.botmanager.common;

import java.io.Serializable;

public abstract class SendableMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Get the sender ID
	 * 
	 * @return the sender ID
	 */
	public abstract int getId();
	
	/**
	 * Get the name of the sender
	 * 
	 * @return the sender name
	 */
	public abstract String getName();
	
	/**
	 * Compile message data to make it showable
	 * 
	 * @return a showable message
	 */
	public abstract String toShow();
}
