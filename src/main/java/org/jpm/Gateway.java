package org.jpm;

public abstract class Gateway {

	protected String id;
	protected AbstractMessage lastMessageSent;

	public Gateway(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AbstractMessage getLastMessageSent() {
		return lastMessageSent;
	}

	public void setLastMessageSent(AbstractMessage lastMessageSent) {
		this.lastMessageSent = lastMessageSent;
	}

	/**
	 * As well as sending the message this method must set the lastMessageSent attribute
	 * @param message
	 */
	abstract void send(AbstractMessage message);
	
}
