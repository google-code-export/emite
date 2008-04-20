package com.calclab.emite.j2se.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.calclab.emite.client.extra.muc.Occupant;

@SuppressWarnings("serial")
public class RoomPanel extends ChatPanel {

    public static interface RoomPanelListener extends ChatPanelListener {
        void onInviteUser(String user, String reason);

        void onModifySubject(String newSubject);
    }

    private final DefaultListModel users;
    private final JList usersList;
    private final JTextField fieldSubjRoom;
    private final JTextField fieldInvitedUser;

    public RoomPanel(final RoomPanelListener listener) {
        super(listener);
        users = new DefaultListModel();
        usersList = new JList(users);
        add(usersList, BorderLayout.EAST);

        final JButton btnModSubject = new JButton("modify subject");
        btnModSubject.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                listener.onModifySubject(fieldSubjRoom.getText());
            }
        });
        final JButton btnInvite = new JButton("invite user");
        btnInvite.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                listener.onInviteUser(fieldInvitedUser.getText(), "This is the reason");
            }
        });
        fieldSubjRoom = new JTextField();
        fieldSubjRoom.setText("Write here the room subject");
        fieldInvitedUser = new JTextField();
        fieldInvitedUser.setText("testuser1@locahost");

        final JPanel northPanel = new JPanel();
        northPanel.add(fieldSubjRoom);
        northPanel.add(btnModSubject);
        northPanel.add(fieldInvitedUser);
        northPanel.add(btnInvite);
        add(northPanel, BorderLayout.NORTH);
    }

    public void setUsers(final Collection<Occupant> users) {
        this.users.clear();
        for (final Occupant user : users) {
            this.users.addElement(user);
        }
    }

}
