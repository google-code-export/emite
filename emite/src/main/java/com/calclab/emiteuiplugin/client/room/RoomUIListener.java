package com.calclab.emiteuiplugin.client.room;

import com.calclab.emiteuiplugin.client.chat.ChatUI;
import com.calclab.emiteuiplugin.client.chat.ChatUIListener;

public interface RoomUIListener extends ChatUIListener {

    public void onCreated(ChatUI chatUI);

    public void onInviteUserRequested(String userJid, String reasonText);

    public void onModifySubjectRequested(String newSubject);

    public void setSubjectEditable(boolean editable);

}
