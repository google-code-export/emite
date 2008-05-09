package com.calclab.emite.examples.echo;

import org.junit.Test;

import com.calclab.emite.testing.EmiteTestHelper;

public class EchoTest {

    @Test
    public void shouldAnswer() {
	final EmiteTestHelper emite = new EmiteTestHelper();
	new Echo(emite);
	emite.receives("<message from='contact@domain' to='me@domain'><body>Hello!</body></message>");
	emite.verifySent("<message from='me@domain' to='contact@domain'><body>Hello!</body></message>");
    }
}
