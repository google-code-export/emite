/**
 *
 */
package com.calclab.emiteuiplugin.client.status;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;

public interface StatusPanelListener {

    void onCloseAllConfirmed();

    void onJoinRoom();

    void onUserColorChanged(String color);

    void onUserSubscriptionModeChanged(SubscriptionMode mode);

    void setOwnPresence(OwnPresence ownPresence);

}
