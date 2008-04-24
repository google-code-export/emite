package com.calclab.emite.client.extra.avatar;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteStub;

public class AvatarModuleTest {
    private EmiteStub emite;
    private AvatarManager manager;

    @Before
    public void aaaCreateManager() {
        emite = new EmiteStub();
        manager = new AvatarManager(emite);
    }

    @Test
    public void managerShouldPublishAvatar() {
        XmppURI to = XmppURI.uri("luther@example.com/roundabout");
        String photo = new String("some base64 encoded photo");
        manager.setvCardAvatar(to, photo);
        emite.verifySent("<iq from='luther@example.com/roundabout' type='set' id='vc1'>"
                + "<vCard prodid='-//HandGen//NONSGML vGen v1.0//EN' version='2.0' "
                + "xmlns='vcard-temp' xdbns='vcard-temp'>"
                + "<PHOTO><BINVAL>some base64 encoded photo</BINVAL></PHOTO></vCard></iq>");
        // User's Server Acknowledges Publish:
        // <iq to='juliet@capulet.com' type='result' id='vc1'/>
    }
}
