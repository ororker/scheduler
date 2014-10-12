package org.jpm;

import static org.mockito.Mockito.*;
import junit.framework.TestCase;

public class SchedulerTest extends TestCase {

	private Scheduler classUnderTest;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		classUnderTest = new Scheduler();
	}

	public void testSingleMessage() {
		Duration durationToProcess = new Duration(1);
		TestMessage mesg = new TestMessage("A", durationToProcess);
		
		GatewayConfig config = new GatewayConfig("A");
		
//		classUnderTest.addExternalResource(config);
		classUnderTest.process(mesg);

		durationToProcess.past();
		
		assertTrue("The completed() method on message was not invoked", mesg.isCompleted());
	}
}