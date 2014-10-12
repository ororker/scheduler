package org.jpm;

import java.util.Observable;

public class Message extends Observable {

	private Object groupId;
	private Gateway gateway;

	/**
	 * when a Message has completed processing
	 */
	public void completed() {
		notifyObservers(gateway);
	}

	/**
	 * @return the logical grouping of a message. Several Messages form a "group".
	 */
	public Object getGroupId() {
		return groupId;
	}

	public void setGroupId(Object groupId) {
		this.groupId = groupId;
	}

	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}
	
	

}
