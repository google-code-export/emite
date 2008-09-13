package com.calclab.emite.widgets.client.room;

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.emite.xep.muc.client.Occupant;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RoomPresenceWidget extends DockPanel implements EmiteWidget {

    static class OccupantPanel extends HorizontalPanel {
	private final Label nick;

	// private Occupant occupant;

	OccupantPanel(final Occupant occupant) {
	    this.nick = new Label();
	    add(nick);
	    setOccupant(occupant);
	}

	public void setOccupant(final Occupant occupant) {
	    // this.occupant = occupant;
	    nick.setText(occupant.getNick());
	}

    }

    public static final String PARAM_ROOM = "room";

    private RoomPresenceController controller;
    private final VerticalPanel occupants;

    public RoomPresenceWidget() {
	setStylePrimaryName("emite-RoomPresenceWidget");
	this.occupants = new VerticalPanel();
	add(occupants, DockPanel.CENTER);
    }

    public void addOccupant(final OccupantPanel occupantUI) {
	occupants.add(occupantUI);
    }

    public void clearOccupants() {
	occupants.clear();
    }

    public OccupantPanel createOccupantWidget(final Occupant occupant) {
	return new OccupantPanel(occupant);
    }

    public String[] getParamNames() {
	return new String[] { PARAM_ROOM };
    }

    public void setParam(final String name, final String value) {
	if (PARAM_ROOM.equals(name)) {
	    controller.setRoomName(value);
	}
    }

    void setController(final RoomPresenceController controller) {
	this.controller = controller;

    }

}
