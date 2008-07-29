package com.calclab.emite.widgets.client.habla;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.emite.widgets.client.chat.ChatWidget;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConversationsWidget extends TabPanel implements EmiteWidget {

    public ConversationsWidget() {
    }

    public void add(final String label, final ChatWidget widget) {
	this.add((Widget) widget, label);
    }

    public String[] getParamNames() {
	return null;
    }

    public void setParam(final String name, final String value) {
    }

}
