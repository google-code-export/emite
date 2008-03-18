package com.calclab.emite.client.x.im.roster;

import java.util.List;

public interface RosterListener {
	void onRosterChanged(List<RosterItem> roster);
}
