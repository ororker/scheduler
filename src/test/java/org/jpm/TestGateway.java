package org.jpm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestGateway extends Gateway {
	
	private Logger LOG = LoggerFactory.getLogger(TestGateway.class);

	public TestGateway(String id) {
		super(id);
	}

	@Override
	public void send(AbstractMessage message) {
		LOG.info("Sending message to Gateway[{}]={}", id, message);
		((TestMessage)message).getDurationToProcess().pause();
		message.completed();
		lastMessageSent = message;
	}
	
}
