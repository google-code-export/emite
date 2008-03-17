package com.calclab.xmpptest.bosh.client.script;

import java.util.HashMap;

import com.calclab.emite.client.bosh.Bosh;
import com.calclab.emite.client.bosh.BoshListener;
import com.calclab.emite.client.bosh.BoshOptions;

public class Interpreter {
	private final Bosh bosh;
	private final HashMap<String, ScriptComponent> commands;
	private final ScriptComponent def;
	private final Output output;
	private Script script;

	public Interpreter(final Output output) {
		this.output = output;
		this.script = null;

		this.bosh = createBosh();
		this.commands = new HashMap<String, ScriptComponent>();
		this.def = new ScriptComponent() {
			public void execute(final Script script, final Instruction step, final Bosh bosh) {
				throw new RuntimeException("command not found: " + step.getName());
			}
		};
		commands.put("START", new ScriptComponent() {
			public void execute(final Script script, final Instruction step, final Bosh bosh) {
				bosh.start();
			}
		});
		commands.put("STOP", new ScriptComponent() {
			public void execute(final Script script, final Instruction step, final Bosh bosh) {
				bosh.stop();
			}
		});
		commands.put("WAIT", new ScriptComponent() {
			public void execute(final Script script, final Instruction step, final Bosh bosh) {
				script.setWaiting(true);
			}
		});
		commands.put("RESPOND", new ScriptComponent() {
			public void execute(final Script script, final Instruction step, final Bosh bosh) {
				bosh.send(step.getContent());
			}
		});
		commands.put("RESTART", new ScriptComponent() {
			public void execute(final Script script, final Instruction step, final Bosh bosh) {
				bosh.restart();
			}
		});
	}

	public void run(final Script script) {
		this.script = script;
		run();
	}

	private Bosh createBosh() {
		return new Bosh(new BoshOptions("http-bind", "localhost"), new BoshListener() {
			public void onError(final Throwable error) {
				output.print("ERROR!");
				output.print(error.getMessage());
			}

			public void onRequest(final String request) {
				output.print("REQUEST");
				output.print(request);
			}

			public void onResponse(final String response) {
				output.print("RESPONSE");
				output.print(response);
				resume();
			}

		});
	}

	private ScriptComponent getCommand(final Instruction step) {
		ScriptComponent cmd = commands.get(step.getName());
		if (cmd == null) {
			cmd = def;
		}
		return cmd;
	}

	private void resume() {
		if (script != null) {
			output.print("Script state: " + script.getState());
			if (script != null && script.getState() == Script.STATE_WAITING) {
				output.print("reanimating");
				script.setWaiting(false);
				run();
			}
		}
	}

	private void run() {
		while (script.getState() == Script.STATE_READY) {
			try {
				final Instruction i = script.getNext();
				output.print("EXECUTING STEP: " + i.getName());
				getCommand(i).execute(script, i, bosh);
				output.print("STEP DONE!");
			} catch (final Exception e) {
				output.print(e.getMessage());
			}
		}
	}
}
