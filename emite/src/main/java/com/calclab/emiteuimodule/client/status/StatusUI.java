package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.View;

import com.calclab.emiteuimodule.client.SubscriptionMode;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.suco.client.listener.Listener;

public interface StatusUI {

    void addButtonItem(View item);

    void addChatMenuItem(View item);

    void addOptionsSubMenuItem(View item);

    void addToolbarItem(View item);

    void confirmCloseAll();

    View getView();

    void onAfterLogin(Listener<StatusUI> listener);

    void onAfterLogout(Listener<StatusUI> listener);

    void onCloseAllConfirmed(Listener<StatusUI> listener);

    void onUserColorChanged(Listener<String> listener);

    void onUserSubscriptionModeChanged(Listener<SubscriptionMode> listener);

    void setCloseAllOptionEnabled(boolean enabled);

    void setCurrentUserChatOptions(UserChatOptions userChatOptions);

    void setEnable(boolean enable);

    void setOwnPresence(OwnPresence ownPresence);

}
