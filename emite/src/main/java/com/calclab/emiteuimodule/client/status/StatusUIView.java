package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;

public interface StatusUIView extends View {

    void addButtonItem(View item);

    void addChatMenuItem(View item);

    void addOptionsSubMenuItem(View item);

    void addToolbarItem(View item);

    void confirmCloseAll();

    void setCloseAllOptionEnabled(boolean enabled);

    void setEnable(boolean enable);

    void setLoadingVisible(boolean visible);

    void setOwnPresence(OwnPresence ownPresence);

    void setSubscriptionMode(SubscriptionMode subscriptionMode);

    void showAlert(String message);

}
