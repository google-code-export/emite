/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.xep.avatar;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import java.util.List;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.xmpp.session.SessionComponent;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.modular.client.signal.Signal;
import com.calclab.modular.client.signal.Slot;

/**
 * XEP-0153: vCard-Based Avatars (Version 1.0)
 */
public class AvatarManager extends SessionComponent {

    private static final String VCARD = "vCard";
    private static final String BINVAL = "BINVAL";
    private static final String PHOTO = "PHOTO";
    private static final String TYPE = "TYPE";
    private static final String XMLNS = "vcard-temp";
    private final Signal<Presence> onAvatarHashPresenceReceived;
    private final Signal<AvatarVCard> onAvatarVCardReceived;
    private final PresenceManager presenceManager;
    private final Emite emite;

    public AvatarManager(final Emite emite, PresenceManager presenceManager) {
	super(emite);
	this.emite = emite;
	this.presenceManager = presenceManager;
	this.onAvatarHashPresenceReceived = new Signal<Presence>("onAvatarHashPresenceReceived");
	this.onAvatarVCardReceived = new Signal<AvatarVCard>("onAvatarVCardReceived");
	install();
    }

    public void avatarHashPresenceReceived(final Slot<Presence> slot) {
	onAvatarHashPresenceReceived.add(slot);
    }

    public void onAvatarVCardReceived(final Slot<AvatarVCard> slot) {
	onAvatarVCardReceived.add(slot);
    }

    /**
     * When the recipient's client receives the hash of the avatar image, it
     * SHOULD check the hash to determine if it already has a cached copy of
     * that avatar image. If not, it retrieves the sender's full vCard in
     * accordance with the protocol flow described in XEP-0054 (note that this
     * request is sent to the user's bare JID, not full JID):
     * 
     * @param otherJID
     */
    public void requestVCard(XmppURI otherJID) {
	final IQ iq = new IQ(Type.get, userURI, otherJID);
	iq.setAttribute("id", "vc2");
	iq.addChild(VCARD, XMLNS);
	emite.send(iq);
    }

    public void setVCardAvatar(final String photoBinary) {
	final IQ iq = new IQ(Type.set, userURI, null);
	final IPacket vcard = iq.addChild(VCARD, XMLNS);
	vcard.With("xdbns", XMLNS).With("prodid", "-//HandGen//NONSGML vGen v1.0//EN");
	vcard.setAttribute("xdbns", XMLNS);
	vcard.setAttribute("prodid", "-//HandGen//NONSGML vGen v1.0//EN");
	vcard.setAttribute("version", "2.0");
	vcard.addChild(PHOTO, null).addChild(BINVAL, null).setText(photoBinary);
	emite.sendIQ("avatar", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (IQ.isSuccess(received)) {

		}
	    }
	});
    }

    private void install() {
	presenceManager.onPresenceReceived(new Slot<Presence>() {
	    public void onEvent(Presence presence) {
		List<? extends IPacket> children = presence.getChildren("x");
		for (IPacket child : children) {
		    if (child.hasAttribute("xmlns", XMLNS + ":x:update")) {
			onAvatarHashPresenceReceived.fire(presence);
		    }
		}
	    }
	});
	emite.subscribe(when("iq"), new PacketListener() {
	    public void handle(IPacket received) {
		if (received.hasAttribute("type", "result") && received.hasAttribute("id", "vc2")
			&& received.hasChild(VCARD) && received.hasAttribute("to", userURI.toString())) {
		    XmppURI from = XmppURI.jid(received.getAttribute("from"));
		    IPacket photo = received.getFirstChild(VCARD).getFirstChild(PHOTO);
		    String photoType = photo.getFirstChild(TYPE).getText();
		    String photoBinval = photo.getFirstChild(BINVAL).getText();
		    AvatarVCard avatar = new AvatarVCard(from, null, photoType, photoBinval);
		    onAvatarVCardReceived.fire(avatar);
		}
	    }
	});
    }

}
