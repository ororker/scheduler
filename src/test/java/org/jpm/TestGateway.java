package org.jpm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestGateway implements Gateway {
	
	private Logger LOG = LoggerFactory.getLogger(TestGateway.class);

	private String id;

	public TestGateway(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void send(Message message) {
		LOG.info("Sending message to Gateway[{}]={}", id, message);
		((TestMessage)message).getDurationToProcess().pause();
		message.completed();
	}
	
}
