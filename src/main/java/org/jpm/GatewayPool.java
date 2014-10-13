package org.jpm;

import java.util.Arrays;
import java.util.concurrent.LinkedTransferQueue;

public class GatewayPool {

	private LinkedTransferQueue<Gateway> pool = new LinkedTransferQueue<Gateway>();
	
	public void addAll(Gateway... gateways) {
		pool.addAll(Arrays.asList(gateways));
	}

	public Gateway borrowGateway() {
		return pool.poll();
	}

	public void returnGateway(Gateway gateway) {
		pool.add(gateway);
	}

	public boolean notEmpty() {
		return ! pool.isEmpty();
	}

}
