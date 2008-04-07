package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.examplechat.client.chatuiplugin.UserChatOptions;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class MultiChatCreationParam {

    private final PairChatUser sessionUser;
    private final String userPassword;
    private final BoshOptions boshOptions;
    private final UserChatOptions userChatOptions;

    public MultiChatCreationParam(final BoshOptions boshOptions, final PairChatUser sessionUser,
            final String userPassword, final UserChatOptions userOptions) {
        this.boshOptions = boshOptions;
        this.sessionUser = sessionUser;
        this.userPassword = userPassword;
        this.userChatOptions = userOptions;
    }

    public PairChatUser getSessionUser() {
        return sessionUser;
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
