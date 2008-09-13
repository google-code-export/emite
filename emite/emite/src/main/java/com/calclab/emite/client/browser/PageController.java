package com.calclab.emite.client.browser;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.Bosh3Settings;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.bosh.StreamSettings;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

public class PageController {

    private final DomAssist assist;
    private final Connection connection;
    private final Session session;

    public PageController(final Connection connection, final Session session, final DomAssist assist) {
	this.connection = connection;
	this.session = session;
	this.assist = assist;
    }

    public void configureCloseAction() {
	final boolean shouldPause = "pause".equals(assist.getMeta("emite.onClose", "pause"));
	Window.addWindowCloseListener(new WindowCloseListener() {
	    public void onWindowClosed() {
		Log.debug("CLOSE!! Should pause? " + shouldPause);
		if (shouldPause) {
		    final StreamSettings stream = session.pause();
		    if (stream != null) {
			final String user = session.getCurrentUser().toString();
			saveSettings(stream, user);
		    }
		} else {
		    session.logout();
		}
	    }

	    public String onWindowClosing() {
		return null;
	    }
	});
    }

    public void configureConnection() {
	final String httpBase = assist.getMeta("emite.httpBase", true);
	final String host = assist.getMeta("emite.host", true);
	Log.debug("CONNECTION PARAMS: " + httpBase + ", " + host);
	connection.setSettings(new Bosh3Settings(httpBase, host));
    }

    public void resumeSession() {
	final boolean shouldResume = "pause".equals(assist.getMeta("emite.onClose", "pause"));
	final String pause = Cookies.getCookie("emite.pause");
	if (shouldResume && pause != null) {
	    Log.debug("Resume session: " + pause);
	    Cookies.removeCookie("emite.pause");
	    final SerializableMap map = SerializableMap.restore(pause);
	    resume(map);
	} else if (false) {
	    final XmppURI jid = uri(assist.getMeta("emite.jid", false));
	    final String password = assist.getMeta("emite.password", false);
	    if (jid != null) {
		session.login(jid, password);
	    }
	}
    }

    private void resume(final SerializableMap map) {
	final StreamSettings stream = new StreamSettings();
	stream.rid = Integer.parseInt(map.get("rid"));
	stream.sid = map.get("sid");
	stream.wait = map.get("wait");
	stream.inactivity = map.get("inactivity");
	stream.maxPause = map.get("maxPause");
	final XmppURI user = uri(map.get("user"));
	session.resume(user, stream);
    }

    private void saveSettings(final StreamSettings stream, final String user) {
	final SerializableMap map = new SerializableMap();
	map.put("rid", "" + stream.rid);
	map.put("sid", stream.sid);
	map.put("wait", stream.wait);
	map.put("inactivity", stream.inactivity);
	map.put("maxPause", stream.maxPause);
	map.put("user", user);
	final String serialized = map.serialize();
	Log.debug("Pausing session: " + serialized);
	Cookies.setCookie("emite.pause", serialized);
    }
}
