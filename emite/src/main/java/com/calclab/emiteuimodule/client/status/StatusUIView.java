package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;

public interface StatusUIView extends View {

    void addMenuButtonItem(View item);

    void addMenuItem(View item);

    void confirmCloseAll();

    void setCloseAllOptionEnabled(boolean enabled);

    void setLoadingVisible(boolean visible);

    void setOwnPresence(OwnPresence ownPresence);

    void setSubscriptionMode(SubscriptionMode subscriptionMode);

    void showAlert(String message);

}
