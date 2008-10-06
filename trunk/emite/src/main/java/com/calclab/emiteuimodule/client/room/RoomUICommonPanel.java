package com.calclab.emiteuimodule.client.room;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.core.client.packet.TextUtils;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;

public class RoomUICommonPanel implements RoomUICommonPanelView {
    public class JoinRoomToolbarButton extends ToolbarButton implements View {
	public JoinRoomToolbarButton(final String title) {
	    super(title);
	}
    }

    private static final String JRB_ID = "emite-ruicp-jrb";

    private final JoinRoomToolbarButton joinRoomButton;
    private final I18nTranslationService i18n;
    private final RoomUIManager presenter;

    public RoomUICommonPanel(final RoomUIManager presenter, StatusUI statusUI, final I18nTranslationService i18n) {
	this.presenter = presenter;
	this.i18n = i18n;
	joinRoomButton = new JoinRoomToolbarButton(i18n.t("Join a chat room"));
	joinRoomButton.setIcon("images/group-chat.gif");
	joinRoomButton.addListener(new ButtonListenerAdapter() {
	    @Override
	    public void onClick(final Button button, final EventObject e) {
		presenter.onJoinRoom();
	    }
	});
	joinRoomButton.setId(JRB_ID);
	statusUI.addButtonItem(joinRoomButton);
    }

    public void roomJoinConfirm(final XmppURI invitor, final XmppURI roomURI, final String reason) {
	MessageBox.confirm(i18n.t("Join to chat room [%s]?", roomURI.getJID().toString()), i18n.t(
		"[%s] are inviting you to join this room: ", invitor.getJID().toString())
		+ TextUtils.escape(reason), new MessageBox.ConfirmCallback() {
	    public void execute(final String btnID) {
		if (btnID.equals("yes")) {
		    DeferredCommand.addCommand(new Command() {
			public void execute() {
			    presenter.joinRoom(roomURI.getNode(), roomURI.getHost());
			}
		    });
		}
	    }
	});
    }

    public void setJoinRoomEnabled(final boolean enabled) {
	joinRoomButton.setDisabled(!enabled);
    }

}
