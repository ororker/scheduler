package org.jpm;

public abstract class AbstractMessage extends Message {

	protected Object groupId;
	
	public abstract void completed();

	public Object getGroupId() {
		return groupId;
	}

	public void setGroupId(Object groupId) {
		this.groupId = groupId;
	}
	
	
}
