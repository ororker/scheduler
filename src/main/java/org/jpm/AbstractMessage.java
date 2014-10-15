package org.jpm;

import java.util.Observable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public abstract class AbstractMessage extends Observable {

	protected Object groupId;
	protected Gateway gateway;
	
	public abstract void completed();

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

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("groupId", groupId)
			.append("gateway", gateway == null ? null : gateway.getId())
			.toString();
	}
	
	
}
