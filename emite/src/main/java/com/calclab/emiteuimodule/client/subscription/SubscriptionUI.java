package com.calclab.emiteuimodule.client.subscription;

import com.calclab.emiteuimodule.client.SubscriptionMode;
import com.calclab.suco.client.listener.Listener0;

public interface SubscriptionUI {

    void onUserAlert(Listener0 listener0);

    void setSubscriptionMode(SubscriptionMode subscriptionMode);

}
