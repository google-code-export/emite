package com.calclab.xmpptest.bosh.client.script;

import java.util.ArrayList;

public class Script {
	public static final String STATE_FINISHED = "finished";
	public static final String STATE_READY = "ready";
	public static final String STATE_WAITING = "waiting";

	private int currentStep;
	private boolean isWaiting;

	private final ArrayList<Instruction> list;

	public Script() {
		this.list = new ArrayList<Instruction>();
		this.currentStep = -1;
		this.isWaiting = false;
	}

	public void add(final Instruction instruction) {
		list.add(instruction);
	}

	public Instruction get(final int index) {
		return list.get(index);
	}

	public Instruction getNext() {
		currentStep++;
		return list.get(currentStep);
	}

	public String getState() {
		if (isWaiting) {
			return STATE_WAITING;
		} else if (currentStep + 1 == list.size()) {
			return STATE_FINISHED;
		} else {
			return STATE_READY;
		}
	}

	public void setWaiting(final boolean isWaiting) {
		this.isWaiting = isWaiting;
	}

	public int size() {
		return list.size();
	}
}
