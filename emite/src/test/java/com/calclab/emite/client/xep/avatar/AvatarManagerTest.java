package com.calclab.emite.client.xep.avatar;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.xep.avatar.AvatarManager;
import com.calclab.emite.testing.EmiteTestHelper;

public class AvatarManagerTest {
    private EmiteTestHelper emite;
    private AvatarManager manager;

    @Before
    public void aaaCreateManager() {
        emite = new EmiteTestHelper();
        manager = new AvatarManager(emite);
    }

    @Test
    public void managerShouldPublishAvatar() {
        manager.logIn(uri("luther@example.com/roundabout"));
        final String photo = "some base64 encoded photo";
        manager.setVCardAvatar(photo);
        emite.verifyIQSent("<iq from='luther@example.com/roundabout' type='set'>"
                + "<vCard prodid='-//HandGen//NONSGML vGen v1.0//EN' version='2.0' "
                + "xmlns='vcard-temp' xdbns='vcard-temp'>"
                + "<PHOTO><BINVAL>some base64 encoded photo</BINVAL></PHOTO></vCard></iq>");

        emite.answerSuccess();
        // User's Server Acknowledges Publish:
        // <iq to='juliet@capulet.com' type='result' id='vc1'/>
    }
}
