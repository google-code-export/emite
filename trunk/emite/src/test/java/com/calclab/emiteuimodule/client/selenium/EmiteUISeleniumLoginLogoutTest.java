package com.calclab.emiteuimodule.client.selenium;

import junit.framework.TestCase;

import org.junit.Test;

import com.thoughtworks.selenium.DefaultSelenium;

public class EmiteUISeleniumLoginLogoutTest {

    @Test
    public void testOnlineAndOffline() throws Exception {
	final DefaultSelenium selenium = createSeleniumClient("http://localhost:4444/");
	selenium.start();

	try {
	    selenium.open("/gwt/com.calclab.emiteui.EmiteUI/EmiteUI.html");
	    selenium.click("EmiteDemoLoginPanel-online-button");
	    for (int second = 0;; second++) {
		if (second >= 60) {
		    TestCase.fail("timeout");
		}
		try {
		    if (selenium.getText("gwt-debug-MultiChatPanel-InfoLabel").matches(
			    "^To start a chat, select a buddy or join to a chat room[\\s\\S]*$")) {
			break;
		    }
		} catch (final Exception e) {
		}
		Thread.sleep(1000);
	    }

	    selenium.click("EmiteDemoLoginPanel-offline-button");
	    for (int second = 0;; second++) {
		if (second >= 60) {
		    TestCase.fail("timeout");
		}
		try {
		    if ("To start a chat you need to be 'online'.".equals(selenium
			    .getText("gwt-debug-MultiChatPanel-InfoLabel"))) {
			break;
		    }
		} catch (final Exception e) {
		}
		Thread.sleep(1000);
	    }
	    selenium.stop();
	} catch (final UnsupportedOperationException e) {
	    System.err.println("Seems that selenium server is not running; run before: 'mvn selenium:start-server' ");
	}

    }

    protected DefaultSelenium createSeleniumClient(final String url) throws Exception {
	// ff3 hangs: http://jira.openqa.org/browse/SRC-225
	// as a workarount use ff2:
	//
	// return new DefaultSelenium("localhost", 4441,
	// "*firefox /usr/lib/firefox-3.0.3/firefox", url);

	// this is a problem... platform dependence ...
	return new DefaultSelenium("localhost", 4441, "*chrome /usr/lib/firefox/firefox-2-bin", url);
    }
}
