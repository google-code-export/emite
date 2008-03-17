package com.calclab.xmpptest.bosh.client.script;

import com.calclab.emite.client.bosh.Bosh;

public interface ScriptComponent {
	void execute(Script script, Instruction step, Bosh bosh);
}
