package com.calclab.emite.client.xep.muc;

import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.chat.ChatManagerListenerAdapter;
import com.calclab.suco.client.signal.Slot;

public class RoomManagerListenerAdapter extends ChatManagerListenerAdapter {

    public RoomManagerListenerAdapter(final ChatManager manager, final RoomManagerListener listener) {
	super(manager, listener);

	((RoomManager) manager).onInvitationReceived(new Slot<RoomInvitation>() {
	    public void onEvent(final RoomInvitation invitation) {
		listener.onInvitationReceived(invitation.getInvitor(), invitation.getRoomURI(), invitation.getReason());
	    }
	});
    }

}
