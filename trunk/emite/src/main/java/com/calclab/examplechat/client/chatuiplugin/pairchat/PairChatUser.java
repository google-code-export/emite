package com.calclab.examplechat.client.chatuiplugin.pairchat;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatUser;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;

public class PairChatUser extends AbstractChatUser {

    private int status;
    private String statusText;

    public PairChatUser(final String iconUrl, final String jid, final String alias, final String color) {
        super(iconUrl, jid, alias, color);
        status = MultiChatView.STATUS_OFFLINE;
        statusText = "";
    }

    public PairChatUser(final String iconUrl, final String jid, final String alias, final String color,
            final int status, final String statusText) {
        super(iconUrl, jid, alias, color);
        this.status = status;
        this.statusText = statusText;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatus(final int status) {
        this.status = status;
    }

    public void setStatusText(final String statusText) {
        this.statusText = statusText;
    }

}
