package com.calclab.emiteuiplugin.client;

import java.util.Date;

import org.ourproject.kune.platf.client.PlatformEvents;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.extend.ExtensibleWidgetChild;
import org.ourproject.kune.platf.client.extend.ExtensibleWidgetsManager;
import org.ourproject.kune.platf.client.extend.PluginManager;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.dialog.MultiChat;
import com.calclab.emiteuiplugin.client.dialog.MultiChatListener;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence.OwnStatus;
import com.calclab.emiteuiplugin.client.params.AvatarProvider;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.google.gwt.user.client.Window;

public class EmiteDialog {
    private static final String EMITE_DEF_TITLE = "Emite Chat";
    private MultiChat multiChatDialog;
    private final Xmpp xmpp;
    private final ChatDialogFactory factory;
    private String initialWindowTitle;

    public EmiteDialog(final Xmpp xmpp, final ChatDialogFactory factory) {
	this.xmpp = xmpp;
	this.factory = factory;
    }

    public void chat(final XmppURI otherUserURI) {
	xmpp.getChatManager().openChat(otherUserURI);
    }

    public void getChatDialog(final MultiChatCreationParam param) {
	if (multiChatDialog == null) {
	    multiChatDialog = createChatDialog(param);
	    EmiteUIPlugin.preFetchImages();
	}
    }

    public void hide() {
	multiChatDialog.hide();
    }

    public void joinRoom(final XmppURI roomURI) {
	xmpp.getRoomManager().openChat(roomURI);
    }

    public void refreshUserInfo(final UserChatOptions userChatOptions) {
	multiChatDialog.setUserChatOptions(userChatOptions);
    }

    public void show(final OwnStatus status) {
	multiChatDialog.show();
	multiChatDialog.setOwnPresence(new OwnPresence(status));
    }

    public void start(final String userJid, final String userPasswd, final String httpBase, final String roomHost) {
	start(new UserChatOptions(userJid, userPasswd, ("emiteui-" + new Date().getTime()), "blue",
		RosterManager.DEF_SUBSCRIPTION_MODE), httpBase, roomHost);
    }

    public void start(final UserChatOptions userChatOptions, final String httpBase, final String roomHost) {
	initialWindowTitle = Window.getTitle();

	final AvatarProvider avatarProvider = new AvatarProvider() {
	    public String getAvatarURL(final XmppURI userURI) {
		return "images/person-def.gif";
	    }
	};
	getChatDialog(new MultiChatCreationParam(EMITE_DEF_TITLE, new BoshOptions(httpBase), roomHost,
		new I18nTranslationServiceMocked(), avatarProvider, userChatOptions));

    }

    private MultiChat createChatDialog(final MultiChatCreationParam param) {
	xmpp.setBoshOptions(param.getBoshOptions());
	final MultiChatListener listener = new MultiChatListener() {
	    public void onConversationAttended(final String chatTitle) {
		Window.setTitle(initialWindowTitle);
	    }

	    public void onConversationUnnatended(final String chatTitle) {
		Window.setTitle("(* " + chatTitle + ") " + initialWindowTitle);
	    }

	    public void onStateConnected() {
	    }

	    public void onStateDisconnected() {
		// TODO Auto-generated method stub

	    }

	    public void onUserColorChanged(final String color) {
	    }

	    public void onUserSubscriptionModeChanged(final SubscriptionMode subscriptionMode) {
	    }

	};
	final MultiChat dialog = factory.createMultiChat(xmpp, param, listener);
	return dialog;
    }

}
