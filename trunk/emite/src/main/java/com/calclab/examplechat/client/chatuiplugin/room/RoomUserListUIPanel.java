package com.calclab.examplechat.client.chatuiplugin.room;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.examplechat.client.chatuiplugin.users.RoomUserUI;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenu;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenuItemList;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridPanel;
import com.calclab.examplechat.client.chatuiplugin.users.RoomUserUI.RoomUserType;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.FitLayout;

public class RoomUserListUIPanel extends Panel implements RoomUserListUIView {

    private final UserGridPanel userGridPanel;
    private final String moderatorLabel;
    private final String visitorLabel;
    private final String participantLabel;
    private final RoomUserListUIPresenter presenter;

    public RoomUserListUIPanel(final I18nTranslationService i18n, final RoomUserListUIPresenter presenter) {
        this.presenter = presenter;
        moderatorLabel = i18n.t("Moderator");
        participantLabel = i18n.t("Participant");
        visitorLabel = i18n.t("Visitor");
        userGridPanel = new UserGridPanel();
        setTitle(i18n.t("Now in this room"));
        setLayout(new FitLayout());
        setAutoScroll(true);
        setIconCls("group-icon");
        add(userGridPanel);
    }

    public void addUser(final RoomUserUI roomUser, final UserGridMenuItemList menuItemList) {
        UserGridMenu menu = new UserGridMenu(presenter);
        menu.setMenuItemList(menuItemList);
        userGridPanel.addUser(roomUser, menu, formatUserType(roomUser.getUserType()));
    }

    public void removeAllUsers() {
        userGridPanel.removeAllUsers();
    }

    public void removeUser(final RoomUserUI roomUser) {
        userGridPanel.removeUser(roomUser);

    }

    public void updateUser(final RoomUserUI roomUser, final UserGridMenuItemList menuItemList) {
        UserGridMenu menu = new UserGridMenu(presenter);
        menu.setMenuItemList(menuItemList);
        userGridPanel.removeUser(roomUser);
    }

    private String formatUserType(final RoomUserType type) {
        switch (type) {
        case moderator:
            return moderatorLabel;
        case participant:
            return participantLabel;
        case visitor:
            return visitorLabel;
        default:
            return "";
        }
    }

}
