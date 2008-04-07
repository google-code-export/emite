package com.calclab.examplechat.client.chatuiplugin;

import com.calclab.emite.client.im.roster.Roster.SubscriptionMode;

public class UserChatOptions {

    String color;
    SubscriptionMode subscriptionMode;

    public UserChatOptions(final String color, final SubscriptionMode subscriptionMode) {
        this.color = color;
        this.subscriptionMode = subscriptionMode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public SubscriptionMode getSubscriptionMode() {
        return subscriptionMode;
    }

    public void setSubscriptionMode(final SubscriptionMode subscriptionMode) {
        this.subscriptionMode = subscriptionMode;
    }

}
