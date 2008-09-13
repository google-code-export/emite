package com.calclab.emiteuimodule.client.room;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public interface RoomUICommonPanelView extends View {

    void roomJoinConfirm(XmppURI invitor, XmppURI roomURI, String reason);

    void setJoinRoomEnabled(boolean enabled);

}
