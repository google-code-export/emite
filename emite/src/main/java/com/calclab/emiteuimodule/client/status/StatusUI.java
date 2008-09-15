package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.im.client.xold_roster.XRosterManager.SubscriptionMode;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.suco.client.listener.Listener;

public interface StatusUI {

    void addButtonItem(View item);

    void addChatMenuItem(View item);

    void addOptionsSubMenuItem(View item);

    void addToolbarItem(View item);

    void confirmCloseAll();

    View getView();

    void onAfterLogin(Listener<StatusUI> slot);

    void onAfterLogout(Listener<StatusUI> slot);

    void onCloseAllConfirmed(Listener<StatusUI> slot);

    void onUserColorChanged(Listener<String> slot);

    void onUserSubscriptionModeChanged(Listener<SubscriptionMode> slot);

    void setCloseAllOptionEnabled(boolean enabled);

    void setCurrentUserChatOptions(UserChatOptions userChatOptions);

    void setEnable(boolean enable);

    void setOwnPresence(OwnPresence ownPresence);

}
