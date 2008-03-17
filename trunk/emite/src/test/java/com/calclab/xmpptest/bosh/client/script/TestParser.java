package com.calclab.xmpptest.bosh.client.script;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.xmpptest.bosh.client.script.Parser;
import com.calclab.xmpptest.bosh.client.script.Script;

public class TestParser {

	@Test
	public void testComplex() {
		final Script script = Parser.getScript("## RESPONSE\n<xml>algo\n</xml>\n## STOP");
		assertEquals("RESPONSE", script.get(0).getName());
		assertEquals("<xml>algo\n</xml>\n", script.get(0).getContent());

	}

	@Test
	public void testSimple() {
		final Script script = Parser.getScript("## START\n## STOP");
		assertEquals(2, script.size());
		assertEquals("START", script.get(0).getName());
		assertEquals("STOP", script.get(1).getName());
	}
}
