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
package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.calclab.emite.client.xep.muc.Room;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

@SuppressWarnings("serial")
public class RoomsPanel extends JPanel {

    private static class RoomWrapper {
        private final Room room;

        public RoomWrapper(final Room room) {
            this.room = room;
        }

        @Override
        public String toString() {
            String name = room.getName();
            name = name != null ? name : "(no name)";
            final XmppURI uri = room.getOtherURI();
            return name + " - " + uri;
        }
    }

    private final JTextField fieldRoom;

    private final JList list;

    private final DefaultListModel listModel;

    public RoomsPanel(final RoomsPanelListener listener) {
        super(new BorderLayout());

        final JButton btnEnter = new JButton("enter room");
        btnEnter.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                listener.onRoomEnterd(fieldRoom.getText());
            }
        });
        listModel = new DefaultListModel();
        list = new JList(listModel);
        add(list, BorderLayout.CENTER);

        final JPanel buttons = new JPanel(new GridLayout(2, 1));
        fieldRoom = new JTextField();
        fieldRoom.setText("testroom1@rooms.localhost/nick");
        buttons.add(fieldRoom);
        buttons.add(btnEnter);
        add(buttons, BorderLayout.SOUTH);
    }

    public void setRooms(final Collection<Room> rooms) {
        listModel.clear();
        for (final Room room : rooms) {
            listModel.addElement(new RoomWrapper(room));
        }
    }

}
