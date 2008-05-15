package com.calclab.emiteuiplugin.client;

import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.Dispatcher;

import com.calclab.emiteuiplugin.client.dialog.MultiChat;

public class EmiteEvents {

    // Input events
    public static final String DESTROY_CHAT_DIALOG = "emiteuiplugin.destroychatdialog";
    public static final String CENTER_CHAT_DIALOG = "emiteuiplugin.centerchatdialog";
    public static final String CLOSE_ALLCHATS = "emiteuiplugin.closeallchats";
    public static final String SET_OWN_AVATAR = "emiteuiplugin.setavatar";

    // Output events
    public static final String ON_USER_COLOR_SELECTED = "emiteuiplugin.usercoloselected";
    public static final String ON_STATE_CONNECTED = "emiteuiplugin.onstateconnected";
    public static final String ON_STATE_DISCONNECTED = "emiteuiplugin.onstatedisconnected";
    public static final String ON_USER_SUBSCRIPTION_CHANGED = "emiteuiplugin.usersubschanged";
    public static final String ON_ROSTER_CHANGED = "emiteuiplugin.onrosterchanged";

    public static void subscribeTo(final Dispatcher dispatcher, final MultiChat multiChatDialog) {

	dispatcher.subscribe(CLOSE_ALLCHATS, new Action<Boolean>() {
	    public void execute(final Boolean param) {
		multiChatDialog.closeAllChats(param);
	    }
	});

	dispatcher.subscribe(CENTER_CHAT_DIALOG, new Action<Object>() {
	    public void execute(final Object param) {
		multiChatDialog.center();
	    }
	});

	dispatcher.subscribe(DESTROY_CHAT_DIALOG, new Action<Object>() {
	    public void execute(final Object param) {
		multiChatDialog.destroy();
	    }
	});
	dispatcher.subscribe(SET_OWN_AVATAR, new Action<String>() {
	    public void execute(final String photoBinary) {
		multiChatDialog.setVCardAvatar(photoBinary);
	    }
	});
    }

}
