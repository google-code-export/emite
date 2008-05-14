package com.calclab.emiteuiplugin.client;

import java.util.Date;

import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.extend.ExtensibleWidgetsManager;
import org.ourproject.kune.platf.client.extend.PluginManager;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence.OwnStatus;
import com.calclab.emiteuiplugin.client.params.AvatarProvider;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.google.gwt.user.client.Window;
import static com.calclab.emiteuiplugin.client.EmiteEvents.*;

public class EmiteDialog {
    private static final String EMITE_DEF_TITLE = "Emite Chat";
    private final DefaultDispatcher dispatcher;

    public EmiteDialog(final DefaultDispatcher dispatcher) {
	this.dispatcher = dispatcher;
    }

    public void chat(final XmppURI otherUserURI) {
	dispatcher.fire(CHATOPEN, otherUserURI);
    }

    public void hide() {
	dispatcher.fire(HIDE_CHAT_DIALOG, null);
    }

    public void joinRoom(final XmppURI roomURI) {
	dispatcher.fire(ROOMOPEN, roomURI);
    }

    public void refreshUserInfo(final UserChatOptions userChatOptions) {
	dispatcher.fire(REFLESH_USER_OPTIONS, userChatOptions);
    }

    public void show(final OwnStatus stutus) {
	dispatcher.fire(SHOW_CHAT_DIALOG, null);
	dispatcher.fire(SET_OWN_PRESENCE, new OwnPresence(stutus));

    }

    public void start(final String userJid, final String userPasswd, final String httpBase, final String roomHost) {
	start(new UserChatOptions(userJid, userPasswd, ("emiteui-" + new Date().getTime()), "blue",
		RosterManager.DEF_SUBSCRIPTION_MODE), httpBase, roomHost);
    }

    public void start(final UserChatOptions userChatOptions, final String httpBase, final String roomHost) {
	final String initialWindowTitle = Window.getTitle();
	Window.getTitle();
	final PluginManager kunePluginManager = new PluginManager(dispatcher, new ExtensibleWidgetsManager(),
		new I18nTranslationServiceMocked());
	kunePluginManager.install(new EmiteUIPlugin());

	dispatcher.subscribe(ON_UNHIGHTLIGHTWINDOW, new Action<String>() {
	    public void execute(final String chatTitle) {
		Window.setTitle(initialWindowTitle);
	    }
	});

	dispatcher.subscribe(ON_HIGHTLIGHTWINDOW, new Action<String>() {
	    public void execute(final String chatTitle) {
		Window.setTitle("(* " + chatTitle + ") " + initialWindowTitle);
	    }
	});

	final AvatarProvider avatarProvider = new AvatarProvider() {
	    public String getAvatarURL(final XmppURI userURI) {
		return "images/person-def.gif";
	    }
	};
	dispatcher.fire(CREATE_CHAT_DIALOG, new MultiChatCreationParam(EMITE_DEF_TITLE, new BoshOptions(httpBase),
		roomHost, new I18nTranslationServiceMocked(), avatarProvider, userChatOptions));
    }

}
