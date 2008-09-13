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
package com.calclab.emiteuimodule.client.room;

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emiteuimodule.client.chat.ChatUI;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot2;

public interface RoomUI extends ChatUI {

    void askInvitation(XmppURI userURI);

    boolean isSubjectEditable();

    void onInviteUserRequested(Slot2<XmppURI, String> param);

    void onModifySubjectRequested(Slot<String> newSubject);

    void onModifySubjectRequested(String newSubject);

    void onOccupantModified(Occupant occupant);

    void onOccupantsChanged(Collection<Occupant> users);

    void setSubject(String newSubject);

}
