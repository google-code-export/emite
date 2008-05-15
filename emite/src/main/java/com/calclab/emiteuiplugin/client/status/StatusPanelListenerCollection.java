/**
 * 
 */
package com.calclab.emiteuiplugin.client.status;

import java.util.ArrayList;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;

@SuppressWarnings("serial") class StatusPanelListenerCollection extends ArrayList<StatusPanelListener> implements
        StatusPanelListener {

    public void onCloseAllConfirmed() {
        for (final StatusPanelListener listener : this) {
    	listener.onCloseAllConfirmed();
        }
    }

    public void onJoinRoom() {
        for (final StatusPanelListener listener : this) {
    	listener.onJoinRoom();
        }
    }

    public void onUserColorChanged(final String color) {
        for (final StatusPanelListener listener : this) {
    	listener.onUserColorChanged(color);
        }
    }

    public void onUserSubscriptionModeChanged(final SubscriptionMode mode) {
        for (final StatusPanelListener listener : this) {
    	listener.onUserSubscriptionModeChanged(mode);
        }
    }

    public void setOwnPresence(final OwnPresence ownPresence) {
        for (final StatusPanelListener listener : this) {
    	listener.setOwnPresence(ownPresence);
        }
    }

}