package com.calclab.emite.client.core.bosh2;

import com.calclab.suco.client.signal.Slot;

public class B2BoshManager {

    public B2BoshManager(final B2Bosh bosh, final B2Stream stream) {

	bosh.onStart(new Slot<B2BoshOptions>() {
	    public void onEvent(final B2BoshOptions options) {
		stream.init(options.domain, options.version, options.wait, options.hold);
		bosh.send(stream.getBody());
	    }
	});
    }
}
