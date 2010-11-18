package com.calclab.emite.functionaltester.client.ui;

import com.calclab.emite.functionaltester.client.ui.TestRunnerView.Level;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OutputMessage extends Composite {

    interface OutputMessageUiBinder extends UiBinder<Widget, OutputMessage> {
    }

    @UiField
    Label body;

    private static OutputMessageUiBinder uiBinder = GWT.create(OutputMessageUiBinder.class);

    private final Level level;

    public OutputMessage(Level level, String body) {
	this.level = level;
	initWidget(uiBinder.createAndBindUi(this));
	addStyleName("ef-OutputMessage");
	addStyleName(level.toString());
	this.body.setText(body);
    }

    public Level getLevel() {
	return level;
    }

}
