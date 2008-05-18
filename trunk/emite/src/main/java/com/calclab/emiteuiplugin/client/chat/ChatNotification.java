package com.calclab.emiteuiplugin.client.chat;

public class ChatNotification {

    private String notification;
    private String style;

    public ChatNotification() {
        this("", null);
    }

    public ChatNotification(final String notification, final String style) {
        this.notification = notification;
        this.style = style;
    }

    public String getNotification() {
        return notification;
    }

    public String getStyle() {
        return style;
    }

    public void setNotification(final String notification) {
        this.notification = notification;
    }

    public void setStyle(final String style) {
        this.style = style;
    }

}
