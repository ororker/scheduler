package org.jpm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Hello world!
 *
 */
public class Scheduler implements Observer {
	
	private MessageStore messageStore;
	private GatewayPool gatewayPool;
	private ExecutorService gatewayExecutorPool;
	
	public Scheduler() {
		gatewayExecutorPool = Executors.newCachedThreadPool();
		
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			@Override
			public void run() {
				process();
			}
		});
	}
	
	public synchronized void add(GatewayConfig... configs) {
		gatewayPool.addAll(configs);
		notifyAll();
	}

	public synchronized void schedule(Message... messages) {
		messageStore.addAll(messages);
		notifyAll();
	}

	@Override
	public synchronized void update(Observable message, Object gatewayArg) {
		Gateway gateway = (Gateway) gatewayArg;
		gatewayPool.returnGateway(gateway);
		notifyAll();
	}
	
	private synchronized void process() {
		while (true) {
			if (readyToProcess()) {
				final Gateway gateway = gatewayPool.borrowGateway();
				final Message message = messageStore.next();
				message.addObserver(this);
				
				gatewayExecutorPool.execute(new Runnable() {
					@Override
					public void run() {
						gateway.send(message);
					}
				});
				
			} else {
				wait();
			}
		}
	}
	
	private boolean readyToProcess() {
		return gatewayPool.notEmpty() && messageStore.hasNext();
	}
	
}
