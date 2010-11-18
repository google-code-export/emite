package com.calclab.examples.emite.echo.client;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.testing.MockedSession;

public class EchoTests {
    private MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession();
	new Echo(session);
    }

    @Test
    public void shouldEcho() {
	// simulates a reception
	session.receives("<message from='someone@domain'><body>Hello!</body></message>");
	// verifies the expected output stanza
	session.verifySent("<message to='someone@domain'><body>Hello!</body></message>");
    }
}
