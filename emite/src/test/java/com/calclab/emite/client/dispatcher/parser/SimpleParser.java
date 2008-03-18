package com.calclab.emite.client.dispatcher.parser;

import java.util.HashMap;

import com.calclab.emite.client.dispatcher.parser.State.BuilderAction;
import com.calclab.emite.client.dispatcher.parser.State.Rule;
import com.calclab.emite.client.dispatcher.parser.State.StateResponse;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.packet.Packet;

public class SimpleParser {
	static class RegexRules {
		public static String ATTRIB = "^(\\w+=\"\\w+\"\\s*)";
		public static String CLOSE_ELEMENT = "(</\\w+\\s*>)";
		public static final String CLOSE_ELEMENT_SHORT = "(/>)";
		public static final String CLOSE_HEAD = "(>)";
		public static final String OPEN_HEAD = "(<\\w+\\s?)";
	}

	static class States {
		public static final String ELEMENT_CLOSED = "element closed";
		public static final String ELEMENT_OPENED = "element opened";
		public static final String HEAD_OPENED = "head opened";
	}

	private final Builder builder;
	private State currentState;

	private final Logger logger;

	private final HashMap<String, State> states;

	public SimpleParser(final Builder builder, final Logger logger) {
		this.builder = builder;
		this.logger = logger;
		this.states = new HashMap<String, State>();
		buildParser();

	}

	public Packet parse(final String xml) throws ParserException {
		int offset = 0;
		setCurrentState(States.ELEMENT_CLOSED);
		builder.startDocument();
		while (offset < xml.length()) {
			logger.debug("find rule for {0}", xml.substring(offset));
			final StateResponse response = currentState.apply(xml, offset);
			logger.debug("Matched rule {0}", response.rule.pattern);
			final int newOffset = offset + response.newOffset;
			response.rule.action.execute(xml.substring(offset, newOffset), builder);
			offset = newOffset;
			setCurrentState(response.rule.nextState);
		}
		return builder.endDocument();
	}

	private State add(final State state) {
		states.put(state.getName(), state);
		return state;
	}

	private void buildParser() {
		final State closed = add(new State(States.ELEMENT_CLOSED));
		final Rule openHead = closed.newRule(RegexRules.OPEN_HEAD, States.HEAD_OPENED, new BuilderAction() {
			public void execute(String consumed, Builder builder) {
				String name = consumed.substring(1).trim();
				builder.openElement(name);
			}
		});

		final State head = add(new State(States.HEAD_OPENED));
		head.newRule(RegexRules.ATTRIB, States.HEAD_OPENED, new BuilderAction() {
			public void execute(final String consumed, final Builder builder) {
				final int equalsIndex = consumed.indexOf('=');
				builder
						.addAttribute(consumed.substring(0, equalsIndex - 1), consumed.substring(equalsIndex + 1)
								.trim());
			}
		});
		head.newRule(RegexRules.CLOSE_ELEMENT_SHORT, States.ELEMENT_CLOSED, new BuilderAction() {
			public void execute(final String consumed, final Builder builder) {
			}
		});
		head.newRule(RegexRules.CLOSE_HEAD, States.ELEMENT_OPENED, new BuilderAction() {
			public void execute(final String consumed, final Builder builder) {
			}
		});

		final State element = add(new State(States.ELEMENT_OPENED));
		element.addRule(openHead);
		element.newRule(RegexRules.CLOSE_ELEMENT, States.ELEMENT_CLOSED, new BuilderAction() {
			public void execute(final String consumed, final Builder builder) {
				String name = consumed.substring(2);
				name = name.substring(0, name.length() - 1);
				builder.closeElement(name);
			}
		});
	}

	private void setCurrentState(final String stateName) {
		logger.debug("Parser - current state: {0}", stateName);
		currentState = states.get(stateName);
	}

}
