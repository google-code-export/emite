package com.calclab.emite.widgets.client.base;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class ComposedWidget extends StackPanel implements EmiteWidget {
    private int currentWidget;
    protected final EmiteWidget disconnectedWidget;
    protected final EmiteWidget connectedWidget;

    public ComposedWidget(final EmiteWidget disconnectedWidget, final EmiteWidget connectedWidget) {
	this.disconnectedWidget = disconnectedWidget;
	this.connectedWidget = connectedWidget;
	currentWidget = -1;
	add((Widget) disconnectedWidget);
	add((Widget) connectedWidget);
	showDisconnectedWiget();
    }

    public String[] getParamNames() {
	final ArrayList<String> params = new ArrayList<String>();
	collectParams(params, disconnectedWidget.getParamNames());
	collectParams(params, connectedWidget.getParamNames());
	collectParams(params, getExtraParamNames());
	return params.toArray(EmiteWidget.EMPTY_PARAMS);
    }

    public void setParam(final String name, final String value) {
	disconnectedWidget.setParam(name, value);
	connectedWidget.setParam(name, value);
	setExtraParam(name, value);
    }

    public void showConnectedWidget() {
	showWidget(1);
    }

    public void showDisconnectedWiget() {
	showWidget(0);
    }

    protected abstract String[] getExtraParamNames();

    protected abstract void setExtraParam(String name, String value);

    private void collectParams(final ArrayList<String> params, final String[] toCollect) {
	if (toCollect != null) {
	    for (final String name : toCollect) {
		params.add(name);
	    }
	}
    }

    private void showWidget(final int index) {
	assert index == 0 || index == 1;

	if (currentWidget != index) {
	    currentWidget = index;
	    showStack(index);
	}
    }

}
