package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.modular.client.signal.Slot;

public interface StatusUI {

    void addChatMenuItem(View item);

    void addButtonItem(View item);

    void addToolbarItem(View item);

    void addOptionsSubMenuItem(View item);

    void confirmCloseAll();

    View getView();

    void onAfterLogin(Slot<StatusUI> slot);

    void onAfterLogout(Slot<StatusUI> slot);

    void onCloseAllConfirmed(Slot<StatusUI> slot);

    void onUserColorChanged(Slot<String> slot);

    void onUserSubscriptionModeChanged(Slot<SubscriptionMode> slot);

    void setCloseAllOptionEnabled(boolean enabled);

    void setCurrentUserChatOptions(UserChatOptions userChatOptions);

    void setOwnPresence(OwnPresence ownPresence);

}
