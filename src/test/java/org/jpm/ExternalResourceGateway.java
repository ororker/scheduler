package org.jpm;

public class ExternalResourceGateway implements Gateway {

	private TestGateway config;

	public ExternalResourceGateway(TestGateway config) {
		this.config = config;
	}

	@Override
	public void send(Message message) {
		// TODO Auto-generated method stub
		
	}

}
