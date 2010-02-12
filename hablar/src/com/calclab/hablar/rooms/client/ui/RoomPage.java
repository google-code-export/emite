package com.calclab.hablar.rooms.client.ui;

import static com.calclab.hablar.core.client.i18n.Translator.i18n;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.hablar.chat.client.ui.ChatDisplay;
import com.calclab.hablar.chat.client.ui.ChatMessageFormatter;
import com.calclab.hablar.core.client.mvp.HablarEventBus;
import com.calclab.hablar.core.client.page.Page;
import com.calclab.hablar.core.client.page.PagePresenter;
import com.calclab.hablar.core.client.ui.icon.HablarIcons;
import com.calclab.hablar.core.client.ui.icon.HablarIcons.IconType;
import com.calclab.hablar.core.client.ui.menu.Action;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

public class RoomPage extends PagePresenter<RoomDisplay> {
    private static final String TYPE = "Room";
    private static int id = 0;

    public static RoomPage asRoom(final Page<?> page) {
	if (RoomPage.TYPE.equals(page.getType())) {
	    return (RoomPage) page;
	}
	return null;
    }

    private final Room room;

    public RoomPage(final HablarEventBus eventBus, final Room chat, final RoomDisplay display) {
	super(TYPE, "" + ++id, eventBus, display);
	room = chat;
	display.setId(getId());

	final Session session = Suco.get(Session.class);
	final String me = session.getCurrentUser().getNode();
	final String name = chat.getURI().getNode();
	model.init(HablarIcons.get(IconType.roster), name);
	setVisibility(Visibility.notFocused);
	model.setCloseable(true);

	chat.onMessageReceived(new Listener<Message>() {
	    @Override
	    public void onEvent(final Message message) {
		final String body = ChatMessageFormatter.format(message.getBody());
		if (body != null) {
		    final String from = message.getFrom().getResource();
		    if (me.equals(from)) {
			display.showMessage("me", body, ChatDisplay.MessageType.sent);
		    } else {
			display.showMessage(from, body, ChatDisplay.MessageType.incoming);
			getState().setUserMessage(i18n().newChatFrom(name, ChatMessageFormatter.ellipsis(body, 25)));
		    }
		}
	    }
	});
	chat.onStateChanged(new Listener<State>() {
	    @Override
	    public void onEvent(final State state) {
		setState(state);
	    }
	});
	setState(chat.getState());

	display.getAction().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(final ClickEvent event) {
		sendMessage(chat, display);
	    }

	});
	display.getTextBox().addKeyDownHandler(new KeyDownHandler() {
	    @Override
	    public void onKeyDown(final KeyDownEvent event) {
		if (event.getNativeKeyCode() == 13) {
		    event.stopPropagation();
		    event.preventDefault();
		    sendMessage(chat, display);
		}
	    }
	});
    }

    public void addAction(final Action<RoomPage> action) {
	display.createAction(action).addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(final ClickEvent event) {
		action.execute(RoomPage.this);
	    }
	});
    }

    public Room getRoom() {
	return room;
    }

    public void showMessage(final String text) {
	display.showMessage(null, text, ChatDisplay.MessageType.info);
    }

    private void sendMessage(final Chat chat, final ChatDisplay display) {
	final String text = display.getBody().getText().trim();
	if (!text.isEmpty()) {
	    final String body = ChatMessageFormatter.format(text);
	    // display.showMessage("me", body, ChatDisplay.MessageType.sent);
	    chat.send(new Message(text));
	    display.clearAndFocus();
	}
    }

    private void setState(final State state) {
	final boolean visible = state == State.ready;
	display.setControlsVisible(visible);
    }

}
