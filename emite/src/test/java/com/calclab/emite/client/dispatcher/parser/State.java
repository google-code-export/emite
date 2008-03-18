package com.calclab.emite.client.dispatcher.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class State {
	public static interface BuilderAction {
		public void execute(String consumed, Builder builder);
	}

	public static class Rule {
		public final BuilderAction action;
		public final String nextState;
		public final Pattern pattern;

		public Rule(final String regex, final BuilderAction action, final String nextState) {
			this.action = action;
			this.nextState = nextState;
			this.pattern = Pattern.compile(regex);
		}
	}

	public static class StateResponse {
		public final int newOffset;
		public final Rule rule;

		public StateResponse(final int newOffset, final Rule rule) {
			this.newOffset = newOffset;
			this.rule = rule;
		}
	}

	private final String name;

	private final ArrayList<Rule> rules;

	public State(final String name) {
		this.name = name;
		this.rules = new ArrayList<Rule>();
	}

	public void addRule(final Rule rule) {
		rules.add(rule);
	}

	public StateResponse apply(final String xml, final int offset) throws ParserException {
		int newOffset = 0;

		for (final Rule rule : rules) {
			final Matcher m = rule.pattern.matcher(xml);
			if (m.find(offset) && m.start() == 0) {
				if (m.start() == 0) {
					newOffset = m.group(1).length();
					return new StateResponse(newOffset, rule);
				} else {
					System.out.println("hostia!" + m.start());
				}
			}
		}
		throw new ParserException("no rule for {0} in state {1}", xml.substring(offset), name);
	}

	public String getName() {
		return name;
	}

	public Rule newRule(final String regex, final String nextState, final BuilderAction action) {
		final Rule rule = new Rule(regex, action, nextState);
		rules.add(rule);
		return rule;
	}
}
