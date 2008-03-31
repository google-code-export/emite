package com.calclab.emite.client.im.roster;

import java.util.List;

public interface RosterListener {
	void onRosterChanged(List<RosterItem> roster);
}
