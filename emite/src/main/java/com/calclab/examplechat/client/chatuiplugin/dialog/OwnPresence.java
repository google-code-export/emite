package com.calclab.examplechat.client.chatuiplugin.dialog;

import com.allen_sauer.gwt.log.client.Log;

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

    public OwnStatus getStatus() {
        return ownStatus;
    }

    public String getStatusText() {
        return statusText;
    }

}
