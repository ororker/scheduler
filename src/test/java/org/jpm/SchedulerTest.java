package org.jpm;

import java.util.Arrays;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.TestCase;

public class SchedulerTest extends TestCase {
	
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerTest.class);

	private Scheduler classUnderTest;

	private Duration defaultDurationToProcess;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		classUnderTest = new Scheduler();
		defaultDurationToProcess = new Duration(1);
	}

	public void testSingleMessage() {
		TestMessage mesg = new TestMessage(group(1), defaultDurationToProcess);
		
		TestGateway config = new TestGateway("A");
		
		classUnderTest.add(config);
		classUnderTest.schedule(mesg);

		waitForMessagesToBeProcessed(mesg);
		
		assertTrue("The completed() method on message was not invoked", mesg.isCompleted());
	}

	public void testMultipleMessagesSameGroup() {
		
		TestMessage[] mesgs = new TestMessage[] {
				new TestMessage(group(1), defaultDurationToProcess),
				new TestMessage(group(1), defaultDurationToProcess),
				new TestMessage(group(1), defaultDurationToProcess),
				new TestMessage(group(1), defaultDurationToProcess)
		};
		printMessages(mesgs);

		TestGateway config = new TestGateway("A");
		
		classUnderTest.add(config);
		classUnderTest.schedule(mesgs);

		waitForMessagesToBeProcessed(mesgs);
		
		assertAllMessagesComplete(mesgs);
		
		assertMessageOrder(mesgs);
	}

	public void testMultipleMessagesDifferentGroups() {
		
		TestMessage[] mesgs = new TestMessage[] {
				new TestMessage(group(2), defaultDurationToProcess),
				new TestMessage(group(1), defaultDurationToProcess),
				new TestMessage(group(2), defaultDurationToProcess),
				new TestMessage(group(3), defaultDurationToProcess)
		};
		printMessages(mesgs);

		TestGateway config = new TestGateway("A");
		
		classUnderTest.add(config);
		classUnderTest.schedule(mesgs);

		waitForMessagesToBeProcessed(mesgs);
		
		assertAllMessagesComplete(mesgs);
		
		TestMessage[] correctOrderOfMessages = new TestMessage[] {
				mesgs[0], mesgs[2], mesgs[1], mesgs[3]
		};
		assertMessageOrder(correctOrderOfMessages);
	}

	public void testCancellingMessageAfterTerminationMessage() {
		
		TestMessage[] mesgs = new TestMessage[] {
				new TestMessage(group(2), defaultDurationToProcess),
				new TestMessage(group(1), defaultDurationToProcess),
				new TestMessage(group(2), defaultDurationToProcess),
				new TestMessage(group(3), defaultDurationToProcess)
		};
		
		printMessages(mesgs);

		TestGateway config = new TestGateway("A");
		
		classUnderTest.add(config);
		classUnderTest.schedule(mesgs);
		classUnderTest.cancelMessagesForGroup(group(3));

		waitForMessagesToBeProcessed(mesgs);
		
		assertAllMessagesComplete(new TestMessage[] {mesgs[0], mesgs[1], mesgs[2]});
		assertFalse("Message for group 3 should not have been processed", mesgs[3].isCompleted());
		
		TestMessage[] correctOrderOfMessages = new TestMessage[] {
				mesgs[0], mesgs[2], mesgs[1]
		};
		assertMessageOrder(correctOrderOfMessages);

	}

	private void printMessages(TestMessage... mesgs) {
		Arrays.asList(mesgs).forEach( (e) -> LOG.info(e.toString()) );
	}

	private void waitForMessagesToBeProcessed(TestMessage... mesgs) {
		Arrays.asList(mesgs).forEach( (e) -> e.getDurationToProcess().past() );
	}

	private void assertMessageOrder(TestMessage... mesgs) {
		Date datePreviousMessageWasConfirmed = mesgs[0].getCompletedDate();
		LOG.info("Mesg[{}]={} - {}", 0, mesgs[0].getCompletedDate().getTime(), mesgs[0]);
		
		for (int i = 1; i < mesgs.length; i++) {
			LOG.info("Mesg[{}]={} - {}", i, mesgs[i].getCompletedDate().getTime(), mesgs[i]);
			assertTrue(String.format("The message with index %d was not completed after the previous one", i), 
					datePreviousMessageWasConfirmed.before(mesgs[i].getCompletedDate()));
			
			datePreviousMessageWasConfirmed = mesgs[i].getCompletedDate();
		}
	}
	
	private Object group(int i) {
		return Integer.valueOf(i);
	}

	private void assertAllMessagesComplete(TestMessage... mesgs) {
		for (int i = 0; i < mesgs.length; i++) {
			assertTrue(String.format("The completed() method on message[%d] was not invoked", i), mesgs[i].isCompleted());
		}
	}
	
}
