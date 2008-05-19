package com.calclab.emite.client;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.services.gwt.GWTXMLService;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.google.gwt.junit.client.GWTTestCase;

public class EmiteGWTTest extends GWTTestCase {

    @Override
    public String getModuleName() {
	return "com.calclab.emite.Emite";
    }

    public void testPacket() {
	final IPacket packet = GWTXMLService
		.toXML("<message to='to@domain' type='chat' xmlns='jabber:client' from='from@domain'><body>the body</body>"
			+ "<simple attName='attValue' xmlns='simple:simple' /></message>");
	assertNotNull(packet);
	assertEquals("message", packet.getName());
	final IPacket child = packet.getFirstChild("body");
	assertNotNull(child);
	assertEquals("body", child.getName());

	final Message message = new Message(packet);
	assertEquals("the body", message.getBody());
	final IPacket simple = message.getFirstChild("simple");
	assertEquals("attValue", simple.getAttribute("attName"));
	assertEquals("simple:simple", simple.getAttribute("xmlns"));
    }

    public void testRealIQ() {
	final String xmlIQ = "<iq type='get' to='vjrj@emitedemo.ourproject.org/emiteui-1211196706576-r534-1' id='aacca' from='test@gmail.com/pb'>"
		+ "<query xmlns='jabber:iq:version'/></iq>";

	final IPacket parsedIQ = GWTXMLService.toXML(xmlIQ);
	assertNotNull(parsedIQ);
	final IQ iq = new IQ(parsedIQ);

	final String xmlIQ2 = "<iq type='get' to='test@emitedemo.ourproject.org/emiteui-1211196706576-r534-1' id='aacda' "
		+ "from='test@gmail.com/pb'><query xmlns='http://jabber.org/protocol/disco#info'/></iq>";

    }

    public void testRealPresence() {
	final String xmlPresence = "<presence from='test@ourproject.org/pb' to='test@emitedemo.ourproject.org/emiteui-1211196706576-r534-1'>"
		+ "<priority>3</priority><c xmlns='http://jabber.org/protocol/caps' node='http://kopete.kde.org/jabber/caps' ver='0.12.7'/>"
		+ "<x xmlns='jabber:x:delay' from='test@ourproject.org/pb' stamp='20080519T07:59:28'/><x xmlns='jabber:x:delay' "
		+ "from='vjrj@ourproject.org/pb' stamp='20080519T08:00:06'/></presence>";

	final IPacket parsed = GWTXMLService.toXML(xmlPresence);
	assertNotNull("should parse", parsed);
	final Presence presence = new Presence(parsed);
	assertSame("should not have show", Presence.Show.notSpecified, presence.getShow());
	assertEquals("priority test", 3, presence.getPriority());

	final String xmlPresence2 = "<presence from='test@gmail.com/pb' to='test@emitedemo.ourproject.org/emiteui-1211196706576-r534-1'><priority>5</priority>"
		+ "<c xmlns='http://jabber.org/protocol/caps' node='http://kopete.kde.org/jabber/caps' ver='0.12.7'/>"
		+ "<x xmlns='vcard-temp:x:update'><photo>af70fe6519d6a27a910c427c3bc551dcd36073e7</photo></x></presence>";
    }
}
