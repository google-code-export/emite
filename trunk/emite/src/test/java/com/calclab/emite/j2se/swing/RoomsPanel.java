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

import com.calclab.emite.client.extra.muc.Room;
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
