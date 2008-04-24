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
package com.calclab.emite.client.extra.avatar;

import com.calclab.emite.client.components.Installable;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;

/**
 * XEP-0153 implementation
 * 
 */
public class AvatarManager implements Installable {

    private final Emite emite;

    public AvatarManager(final Emite emite) {
        this.emite = emite;
    }

    public void install() {
    }

    public void setvCardAvatar(final XmppURI to, final String photoBin) {
        final BasicStanza message = new BasicStanza("iq", null);
        message.setType(Type.set.toString());
        message.setFrom(to.toString());
        message.setAttribute("id", "vc1");
        final IPacket vcard = message.add("vCard", "vcard-temp");
        vcard.setAttribute("xdbns", "vcard-temp");
        vcard.setAttribute("prodid", "-//HandGen//NONSGML vGen v1.0//EN");
        vcard.setAttribute("version", "2.0");
        IPacket photo = vcard.add("PHOTO", null);
        IPacket binval = photo.add("BINVAL", null);
        binval.setText(photoBin);
        emite.send(message);
    }

}
