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

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.session.SessionComponent;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;

/**
 * XEP-0153 implementation
 * 
 */
public class AvatarManager extends SessionComponent {

    public AvatarManager(final Emite emite) {
	super(emite);
    }

    public void setVCardAvatar(final String photoBinary) {
	final IQ iq = new IQ(Type.set, userURI, null);
	final IPacket vcard = iq.addChild("vCard", "vcard-temp");
	vcard.With("xdbns", "vcard-temp").With("prodid", "-//HandGen//NONSGML vGen v1.0//EN");
	vcard.setAttribute("xdbns", "vcard-temp");
	vcard.setAttribute("prodid", "-//HandGen//NONSGML vGen v1.0//EN");
	vcard.setAttribute("version", "2.0");
	vcard.addChild("PHOTO", null).addChild("BINVAL", null).setText(photoBinary);
	emite.sendIQ("avatar", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (IQ.isSuccess(received)) {

		}
	    }
	});
    }

}
