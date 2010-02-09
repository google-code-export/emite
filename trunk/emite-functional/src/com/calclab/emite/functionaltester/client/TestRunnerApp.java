package com.calclab.emite.functionaltester.client;

import com.calclab.emite.functionaltester.client.tests.ConnectionTestSuite;
import com.calclab.emite.functionaltester.client.tests.SearchTestSuite;
import com.calclab.emite.functionaltester.client.tests.DiscoveryTestSuite;
import com.calclab.emite.functionaltester.client.tests.VCardTestSuite;
import com.calclab.emite.functionaltester.client.ui.TestRunnerPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class TestRunnerApp implements EntryPoint {

    @Override
    public void onModuleLoad() {
	GWT.log("TestRunnerApp loaded!", null);
	final TestRunnerPanel runner = new TestRunnerPanel();

	// add tests here
	runner.addTestSuite("Connection", new ConnectionTestSuite());
	runner.addTestSuite("Search", new SearchTestSuite());
	runner.addTestSuite("Disco", new DiscoveryTestSuite());
	runner.addTestSuite("VCard", new VCardTestSuite());
	RootLayoutPanel.get().add(runner);
    }

}
