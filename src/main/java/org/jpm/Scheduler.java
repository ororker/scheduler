package org.jpm;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class Scheduler implements Observer {
	
	private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);
	
	private MessageStore messageStore;
	private GatewayPool gatewayPool;
	private ExecutorService gatewayExecutorPool;
	
	public Scheduler() {
		messageStore = new MessageStore();
		gatewayPool = new GatewayPool();
		gatewayExecutorPool = Executors.newCachedThreadPool();
		
		Executors.newSingleThreadExecutor().execute( () -> process() );
	}
	
	public synchronized void add(Gateway... configs) {
		gatewayPool.addAll(configs);
		notifyAll();
	}

	public synchronized void schedule(AbstractMessage... messages) {
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
				final AbstractMessage message = messageStore.next();
				message.addObserver(this);
				
				gatewayExecutorPool.execute( () -> gateway.send(message) );
				
			} else {
				try {
					wait();
				} catch (InterruptedException e) {
					LOG.error("Exception while waiting to process", e);
				}
			}
		}
	}
	
	private boolean readyToProcess() {
		return gatewayPool.notEmpty() && messageStore.hasNext();
	}
	
}
