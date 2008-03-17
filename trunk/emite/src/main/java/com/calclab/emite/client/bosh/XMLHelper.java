package com.calclab.emite.client.bosh;

import com.calclab.emite.client.utils.TextHelper;

class XMLHelper {
	private static final String BODY = "<body rid=\"{0}\" sid=\"{1}\" xmlns=\"http://jabber.org/protocol/httpbind\" >";
	private static final String CREATION = "<body content=\"text/xml; charset=utf-8\" " + " rid=\"{0}\" "
			+ " to=\"{1}\" " + "secure=\"true\" " + "ver=\"1.6\" " + "wait=\"60\" " + "ack=\"1\" " + "hold=\"1\" "
			+ "xml:lang=\"en\" " + "xmlns=\"http://jabber.org/protocol/httpbind\" />";

	private static final String EMPTY = "<body rid=\"{0}\" sid=\"{1}\" pause=\"{2}\" xmlns=\"http://jabber.org/protocol/httpbind\" />";

	private static final String END = "</body>";

	private static final String RESTART = "<body rid=\"{0}\" sid=\"{1}\" to=\"{2}\" "
			+ "xml:lang=\"en\" xmpp:restart=\"true\" xmlns=\"http://jabber.org/protocol/httpbind\" xmlns:xmpp=\"urn:xmpp:xbosh\" />";

	public static String buildSessionCreationRequest(final BoshOptions options, final long rid) {
		return TextHelper.temple(CREATION, rid, options.getDomain());
	}

	public static String empty(final long rid, final String sid, final int inactivity) {
		return TextHelper.temple(EMPTY, rid, sid, inactivity);
	}

	public static String extractAttribute(final String attName, final String responseText) {
		final String before = attName + "=";
		final int begin = responseText.indexOf(before) + before.length();
		final char sep = responseText.charAt(begin);
		final int end = responseText.indexOf(sep, begin + 1);
		return responseText.substring(begin + 1, end);
	}

	public static int extractIntegerAttribute(final String attName, final String responseText) {
		try {
			return Integer.parseInt(extractAttribute(attName, responseText));
		} catch (final Exception e) {
			return -1;
		}
	}

	public static String restart(final long rid, final String sid, final String domain) {
		return TextHelper.temple(RESTART, rid, sid, domain);
	}

	public static String wrap(final String stanza, final long rid, final String sid) {
		String request = TextHelper.temple(BODY, rid, sid);
		// if (stanza != null) {
		request += stanza;
		// }
		return request + END;
	}

}
