package com.calclab.emite.client.core.bosh2;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.testing.SignalTester;

public class B2BoshManagerTest {

    private B2Stream stream;
    private B2Bosh bosh;

    @Before
    public void beforeTests() {
	bosh = mock(B2Bosh.class);
	stream = mock(B2Stream.class);
	new B2BoshManager(bosh, stream);
    }

    @Test
    public void shouldSendSessionRequestWhenBoshStarts() {
	final SignalTester<B2BoshOptions> signal = new SignalTester<B2BoshOptions>();
	verify(bosh).onStart(argThat(signal));
	signal.fire(new B2BoshOptions("base", "domain"));
    }
}
