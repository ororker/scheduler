package org.jpm;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedTransferQueue;

import org.apache.commons.collections4.map.MultiValueMap;

public class MessageStore {

	private MultiValueMap<Object, AbstractMessage> groupedMessageMap = new MultiValueMap<Object, AbstractMessage>();
	private LinkedTransferQueue<AbstractMessage> messageQueue = new LinkedTransferQueue<AbstractMessage>();
	
	
	public void addAll(AbstractMessage... messages) {
		List<AbstractMessage> messageList = Arrays.asList(messages);
		messageList.forEach(p -> groupedMessageMap.put(p.getGroupId(), p));
		messageList.forEach(p -> messageQueue.put(p));
	}


	public AbstractMessage next(AbstractMessage lastMessageSent) {
		AbstractMessage next = null;

		if (lastMessageSent != null) {
			Object groupId = lastMessageSent.getGroupId();
			Collection<AbstractMessage> groupMessageList = groupedMessageMap.getCollection(groupId);
			if (groupMessageList != null) {
				Optional<AbstractMessage> firstMessageFromGroup = groupMessageList.stream().findFirst();
				if (firstMessageFromGroup.isPresent()) {
					next = firstMessageFromGroup.get();
				}
			}
		}
		
		if (next == null) {
			next = messageQueue.poll();
		}

		remove(next);
		
		return next;
	}
	
	private void remove(AbstractMessage message) {
		if (message != null) {
			groupedMessageMap.removeMapping(message.getGroupId(), message);
			messageQueue.remove(message);
		}
	}

	public AbstractMessage next() {
		AbstractMessage message = messageQueue.poll();
		remove(message);
		return message;
	}

	public boolean hasNext() {
		return ! messageQueue.isEmpty();
	}

}
