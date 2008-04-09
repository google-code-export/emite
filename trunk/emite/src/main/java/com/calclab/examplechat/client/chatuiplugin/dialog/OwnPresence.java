package com.calclab.examplechat.client.chatuiplugin.dialog;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;

public class OwnPresence {
    public enum OwnStatus {
        offline, online, onlinecustom, busy, busycustom
    }

    OwnStatus ownStatus;
    String statusText;

    public OwnPresence(final OwnStatus ownStatus) {
        this.ownStatus = ownStatus;
    }

    public OwnPresence(final OwnStatus ownStatus, final String statusText) {
        this(ownStatus);
        this.statusText = statusText;
        if (statusText.length() > 0 && !(ownStatus == OwnStatus.busycustom || ownStatus == OwnStatus.onlinecustom)) {
            Log.error("Code error: statusText only can be set in onlinecustom and busycustom states");
        }
    }

    public OwnPresence(final Presence currentPresence) {
        this.statusText = currentPresence.getStatus();
        Show show = currentPresence.getShow();
        boolean hastStatusText = statusText != null;
        switch (show) {
        case dnd:
            ownStatus = hastStatusText ? OwnStatus.busycustom : OwnStatus.busy;
            break;
        case available:
        case chat:
            ownStatus = hastStatusText ? OwnStatus.onlinecustom : OwnStatus.online;
            break;
        case away:
        case xa:
            Log.error("Code error: Show : " + show + " is not support in current UI");
        }
    }

    public OwnStatus getStatus() {
        return ownStatus;
    }

    public String getStatusText() {
        return statusText;
    }

}
