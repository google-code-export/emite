package com.calclab.emite.xep.muc.client.events;

import com.calclab.emite.core.client.events.ChangedEvent;
import com.calclab.emite.core.client.events.GwtEmiteEventBus;
import com.calclab.emite.xep.muc.client.Occupant;
import com.google.gwt.event.shared.HandlerRegistration;

public class OccupantChangedEvent extends ChangedEvent<OccupantChangedHandler> {

    private static final Type<OccupantChangedHandler> TYPE = new Type<OccupantChangedHandler>();

    public static HandlerRegistration bind(GwtEmiteEventBus eventBus, OccupantChangedHandler handler) {
	return eventBus.addHandler(TYPE, handler);
    }

    private final Occupant occupant;

    public OccupantChangedEvent(String changeType, Occupant occupant) {
	super(TYPE, changeType);
	this.occupant = occupant;
    }

    @Override
    protected void dispatch(OccupantChangedHandler handler) {
	handler.onOccupantChanged(this);
    }

    public Occupant getOccupant() {
	return occupant;
    }

}