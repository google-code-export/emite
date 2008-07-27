package com.calclab.emite.widgets.client.room;

import com.calclab.emite.client.xep.muc.Occupant;
import com.calclab.emite.widgets.client.EmiteWidget;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RoomPresenceWidget extends DockPanel implements EmiteWidget {

    static class OccupantPanel extends HorizontalPanel {
	private final Label nick;
	private final Label affiliation;
	private final Label role;
	private final Label uri;

	OccupantPanel(final Occupant occupant) {
	    this.nick = new Label();
	    this.affiliation = new Label();
	    this.role = new Label();
	    this.uri = new Label();
	    add(nick);
	    add(affiliation);
	    add(role);
	    add(uri);
	    setOccupant(occupant);
	}

	public void setOccupant(final Occupant occupant) {
	    nick.setText(occupant.getNick());
	    affiliation.setText(occupant.getAffiliation().toString());
	    role.setText(occupant.getRole().toString());
	    uri.setText(occupant.getUri().toString());
	}

    }

    private RoomPresenceController controller;
    private final VerticalPanel occupants;
    private final Label status;

    public RoomPresenceWidget() {
	this.occupants = new VerticalPanel();
	this.status = new Label();
	add(occupants, DockPanel.CENTER);
	add(status, DockPanel.SOUTH);
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
	return new String[] { "room" };
    }

    public void setParam(final String name, final String value) {
	if ("room".equals(name)) {
	    controller.setRoomName(value);
	}
    }

    public void setStatus(final String message) {
	status.setText(message);
    }

    void setController(final RoomPresenceController controller) {
	this.controller = controller;

    }

}
