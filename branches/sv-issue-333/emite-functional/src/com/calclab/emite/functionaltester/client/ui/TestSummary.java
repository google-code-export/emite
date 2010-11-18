package com.calclab.emite.functionaltester.client.ui;

import com.calclab.emite.functionaltester.client.TestResult;
import com.calclab.emite.functionaltester.client.TestRunner;
import com.calclab.emite.functionaltester.client.TestResult.State;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TestSummary extends Composite {

    interface TestSummaryUiBinder extends UiBinder<Widget, TestSummary> {
    }

    private static TestSummaryUiBinder uiBinder = GWT.create(TestSummaryUiBinder.class);

    @UiField
    Label name;

    private final TestResult test;
    private final TestRunner runner;

    public TestSummary(TestResult testResult, TestRunner runner) {
	this.test = testResult;
	this.runner = runner;
	initWidget(uiBinder.createAndBindUi(this));
	name.setText(testResult.getName());
	addStyleName("ef-TestSummary");
    }

    @UiHandler("name")
    public void onExecute(ClickEvent event) {
	runner.run(test);
    }

    public void setState(State state) {
	name.addStyleName(state.toString());
    }

}
