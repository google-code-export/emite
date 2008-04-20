package com.calclab.emiteui.client.emiteuiplugin.room;

import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUI;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUIListener;

public interface RoomUIListener extends ChatUIListener {

    public void onCreated(ChatUI chatUI);

    public void onModifySubjectRequested(String newSubject);

    public void setSubjectEditable(boolean editable);

}
