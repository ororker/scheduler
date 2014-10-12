package org.jpm;

import java.util.Date;

public class TestMessage extends AbstractMessage {

	/**
	 * Used to simulate "potentially very time consuming operations"
	 */
	private Duration durationToProcess;
	
	/**
	 * used to verify if and when the completed method was invoked
	 */
	private Date completed;
	
	public TestMessage(Object groupId, Duration durationToProcess) {
		this.groupId = groupId;
		this.durationToProcess = durationToProcess;
	}

	@Override
	public void completed() {
		if (completed != null) {
			throw new IllegalStateException("completed() must only be called once");
		}
		completed = new Date();
	}

	public boolean isCompleted() {
		return completed != null;
	}
	
	public Duration getDurationToProcess() {
		return durationToProcess;
	}

	public void setDurationToProcess(Duration durationToProcess) {
		this.durationToProcess = durationToProcess;
	}

	
}
