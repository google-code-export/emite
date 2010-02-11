package com.calclab.emite.functionaltester.client.tests;

import com.calclab.emite.functionaltester.client.Context;
import com.calclab.emite.functionaltester.client.FunctionalTest;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.suco.client.Suco;

public class RosterTestSuite extends BasicTestSuite {

    private final FunctionalTest requestRoster = new FunctionalTest() {

	@Override
	public void run(Context ctx) {
	    Roster roster = Suco.get(Roster.class);
	    ctx.info("Roster retrieve test start");
	    ctx.info("Session:" + ctx.getSession().getState());
	    if (roster.isRosterReady()) {
		ctx.success("Roster retrieved");
	    } else {
		ctx.fail("Roster not retrieved");
	    }
	    ctx.getSession().logout();
	}
    };

    @Override
    public void registerTests() {
	add("Request roster", requestRoster);
    }

}
