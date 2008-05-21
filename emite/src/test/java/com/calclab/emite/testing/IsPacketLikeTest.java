package com.calclab.emite.testing;

import static com.calclab.emite.testing.EmiteAsserts.assertNotPacketLike;
import static com.calclab.emite.testing.EmiteAsserts.assertPacketLike;

import org.junit.Test;

public class IsPacketLikeTest {

    @Test
    public void testAttributes() {
	assertPacketLike("<iq type='result' />", "<iq type='result' />");
	assertNotPacketLike("<iq type='result' />", "<iq type='result2' />");
	assertPacketLike("<iq type='result' />", "<iq type='result' value='something here' />");
    }

    @Test
    public void testChildren() {
	assertNotPacketLike("<iq><child att='value' /></iq>", "<iq></iq>");
	assertNotPacketLike("<iq><child att='value' /></iq>", "<iq><child/></iq>");
	assertPacketLike("<iq><child att='value' /></iq>", "<iq><child att='value' /><child2 /></iq>");
    }

    @Test
    public void testChildrenText() {
	assertNotPacketLike("<presence><show>chat</show></presence>", "<presence><show>dnd</show></presence>");
    }

    @Test
    public void testNestedChildren() {
	assertPacketLike("<iq><child/></iq>", "<iq><child><subchild/></child></iq>");
    }

}
