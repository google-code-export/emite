package com.calclab.hablar.demo.client;

import com.calclab.hablar.basic.client.ui.HablarWidget;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public interface DemoView {

    Widget asWidget();

    HablarWidget getHablar();

    HasClickHandlers getLogin();

    HasClickHandlers getLogout();

    HasText getPassword();

    HasText getState();

    HasText getUserName();

}
