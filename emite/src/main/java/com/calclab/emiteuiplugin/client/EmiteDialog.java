package com.calclab.emiteuiplugin.client;

import static com.calclab.emiteuiplugin.client.EmiteEvents.CHATOPEN;
import static com.calclab.emiteuiplugin.client.EmiteEvents.ON_HIGHTLIGHTWINDOW;
import static com.calclab.emiteuiplugin.client.EmiteEvents.ON_UNHIGHTLIGHTWINDOW;

import java.util.Date;

import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.extend.ExtensibleWidgetsManager;
import org.ourproject.kune.platf.client.extend.PluginManager;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.im.roster.RosterManager;
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
    private final DefaultDispatcher dispatcher;
    private MultiChat multiChatDialog;
    private Xmpp xmpp;

    public EmiteDialog(final DefaultDispatcher dispatcher) {
	this.dispatcher = dispatcher;
    }

    public void chat(final XmppURI otherUserURI) {
	dispatcher.fire(CHATOPEN, otherUserURI);
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
	final String initialWindowTitle = Window.getTitle();
	Window.getTitle();
	final PluginManager kunePluginManager = new PluginManager(dispatcher, new ExtensibleWidgetsManager(),
		new I18nTranslationServiceMocked());
	kunePluginManager.install(new EmiteUIPlugin());

	final AvatarProvider avatarProvider = new AvatarProvider() {
	    public String getAvatarURL(final XmppURI userURI) {
		return "images/person-def.gif";
	    }
	};
	getChatDialog(new MultiChatCreationParam(EMITE_DEF_TITLE, new BoshOptions(httpBase), roomHost,
		new I18nTranslationServiceMocked(), avatarProvider, userChatOptions));

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

    }

    private MultiChat createChatDialog(final MultiChatCreationParam param) {
	xmpp = Xmpp.create();
	xmpp.setBoshOptions(param.getBoshOptions());
	EmiteEvents.subscribe(xmpp, dispatcher);
	final MultiChatListener listener = EmiteEvents.createMultiChatListener(dispatcher);
	final MultiChat dialog = ChatDialogFactoryImpl.App.getInstance().createMultiChat(xmpp, param, listener);
	EmiteEvents.subscribeTo(dispatcher, dialog);
	return dialog;
    }

}
