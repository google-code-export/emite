package com.calclab.examplechat.client.chatuiplugin.users;

public class UserGridMenuItem<T> {

    final String iconCls;
    final String title;
    final String eventName;
    final T param;

    public UserGridMenuItem(final String iconCls, final String title, final String eventName, final T param) {
        this.iconCls = iconCls;
        this.title = title;
        this.eventName = eventName;
        this.param = param;
    }

    public String getIconCls() {
        return iconCls;
    }

    public String getTitle() {
        return title;
    }

    public String getEventName() {
        return eventName;
    }

    public T getParam() {
        return param;
    }

}
