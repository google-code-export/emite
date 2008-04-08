package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.examplechat.client.chatuiplugin.UserChatOptions;

public class MultiChatCreationParam {

    private final BoshOptions boshOptions;
    private final String userJid;
    private final String userPassword;
    private final UserChatOptions userChatOptions;

    public MultiChatCreationParam(final BoshOptions boshOptions, final String userJid, final String userPasswd,
            final UserChatOptions userChatOptions) {
        this.boshOptions = boshOptions;
        this.userJid = userJid;
        this.userPassword = userPasswd;
        this.userChatOptions = userChatOptions;
    }

    public String getUserJid() {
        return userJid;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public BoshOptions getBoshOptions() {
        return boshOptions;
    }

    public UserChatOptions getUserChatOptions() {
        return userChatOptions;
    }

}
