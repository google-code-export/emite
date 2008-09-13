package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.im.client.roster.RosterManager.SubscriptionMode;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.suco.client.signal.Slot;

public interface StatusUI {

    void addButtonItem(View item);

    void addChatMenuItem(View item);

    void addOptionsSubMenuItem(View item);

    void addToolbarItem(View item);

    void confirmCloseAll();

    View getView();

    void onAfterLogin(Slot<StatusUI> slot);

    void onAfterLogout(Slot<StatusUI> slot);

    void onCloseAllConfirmed(Slot<StatusUI> slot);

    void onUserColorChanged(Slot<String> slot);

    void onUserSubscriptionModeChanged(Slot<SubscriptionMode> slot);

    void setCloseAllOptionEnabled(boolean enabled);

    void setCurrentUserChatOptions(UserChatOptions userChatOptions);

    void setEnable(boolean enable);

    void setOwnPresence(OwnPresence ownPresence);

}
