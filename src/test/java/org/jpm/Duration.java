package org.jpm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Duration {
	
	private static final Logger LOG = LoggerFactory.getLogger(Duration.class);
	
	private static final int BUFFER_AFTER_INTERVAL = 1;
	private static final int UNIT_OF_TIME_IN_MILLISSECS = 100;
	
	private int interval;

	public Duration(int interval) {
		super();
		this.interval = interval;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void past() {
		try {
		
			// sleep for the time it should take to process
			Thread.sleep(interval * UNIT_OF_TIME_IN_MILLISSECS);

			// sleep for the buffer period after message should have processed
			Thread.sleep(BUFFER_AFTER_INTERVAL * UNIT_OF_TIME_IN_MILLISSECS);
		
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
	}

	public void pause() {
		long milliSecs = interval * UNIT_OF_TIME_IN_MILLISSECS;
		try {
			Thread.sleep(milliSecs);
		} catch (InterruptedException e) {
			LOG.error("Failed to pause for {} millisecs", milliSecs, e);
			throw new RuntimeException(e);
		}
	}

}
