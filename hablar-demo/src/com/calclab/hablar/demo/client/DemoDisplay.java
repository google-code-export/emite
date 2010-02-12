package com.calclab.hablar.demo.client;

import com.calclab.hablar.core.client.HablarWidget;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public interface DemoDisplay {

    Widget asWidget();

    HablarWidget getHablarWidget();

    HasClickHandlers getLogin();

    HasClickHandlers getLogout();

    HasText getPassword();

    HasText getState();

    HasText getUserName();

    void setLoginEnabled(boolean enabled);

    void setLogoutEnabled(boolean enabled);

}
