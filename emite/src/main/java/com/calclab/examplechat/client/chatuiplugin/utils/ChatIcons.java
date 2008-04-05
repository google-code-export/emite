package com.calclab.examplechat.client.chatuiplugin.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface ChatIcons extends ImageBundle {

    public static class App {
        private static ChatIcons ourInstance = null;

        public static synchronized ChatIcons getInstance() {
            if (ourInstance == null) {
                ourInstance = (ChatIcons) GWT.create(ChatIcons.class);
            }
            return ourInstance;
        }
    }

    /**
     * @gwt.resource chat.png
     */
    AbstractImagePrototype chat();

    /**
     * @gwt.resource away.png
     */
    AbstractImagePrototype away();

    /**
     * @gwt.resource busy.png
     */
    AbstractImagePrototype busy();

    /**
     * @gwt.resource message.png
     */
    AbstractImagePrototype message();

    /**
     * @gwt.resource invisible.png
     */
    AbstractImagePrototype invisible();

    /**
     * @gwt.resource xa.png
     */
    AbstractImagePrototype extendedAway();

    /**
     * @gwt.resource offline.png
     */
    AbstractImagePrototype offline();

    /**
     * @gwt.resource online.png
     */
    AbstractImagePrototype online();

    /**
     * @gwt.resource user_add.png
     */
    AbstractImagePrototype userAdd();

}
