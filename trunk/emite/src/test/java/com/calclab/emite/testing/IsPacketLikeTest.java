package com.calclab.emite.testing;

import static com.calclab.emite.testing.MockitoEmiteHelper.toXML;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IsPacketLikeTest {

    @Test
    public void testAttributes() {
	assertMatching("<iq type='result' />", "<iq type='result' />");
	assertNotMatching("<iq type='result' />", "<iq type='result2' />");
	assertMatching("<iq type='result' />", "<iq type='result' value='something here' />");
    }

    @Test
    public void testChildren() {
	assertNotMatching("<iq><child att='value' /></iq>", "<iq></iq>");
	assertNotMatching("<iq><child att='value' /></iq>", "<iq><child/></iq>");
	assertMatching("<iq><child att='value' /></iq>", "<iq><child att='value' /><child2 /></iq>");
    }

    @Test
    public void testChildrenText() {
	assertNotMatching("<presence><show>chat</show></presence>", "<presence><show>dnd</show></presence>");
    }

    private void assertMatching(final String matcher, final String test) {
	final IsPacketLike m = new IsPacketLike(toXML(matcher));
	assertTrue(matcher + " should not match " + test, m.matches(toXML(test), System.out));
    }

    private void assertNotMatching(final String matcher, final String test) {
	final IsPacketLike m = new IsPacketLike(toXML(matcher));
	assertFalse(matcher + " should not match " + test, m.matches(toXML(test), System.out));
    }

}
