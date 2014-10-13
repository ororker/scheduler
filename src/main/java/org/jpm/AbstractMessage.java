package org.jpm;

import java.util.Observable;

public abstract class AbstractMessage extends Observable {

	protected Object groupId;
	
	public abstract void completed();

	public Object getGroupId() {
		return groupId;
	}

	public void setGroupId(Object groupId) {
		this.groupId = groupId;
	}
	
	
}
