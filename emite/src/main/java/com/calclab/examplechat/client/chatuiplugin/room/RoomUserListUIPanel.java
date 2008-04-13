package com.calclab.examplechat.client.chatuiplugin.room;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.examplechat.client.chatuiplugin.AbstractPresenter;
import com.calclab.examplechat.client.chatuiplugin.users.RoomUserUI;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenu;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenuItemList;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridPanel;
import com.calclab.examplechat.client.chatuiplugin.users.RoomUserUI.RoomUserType;

public class RoomUserListUIPanel extends UserGridPanel implements View {

    private final String moderatorLabel;
    private final String visitorLabel;
    private final String participantLabel;
    private final AbstractPresenter presenter;

    public RoomUserListUIPanel(final I18nTranslationService i18n, final AbstractPresenter presenter) {
        this.presenter = presenter;
        moderatorLabel = i18n.t("Moderator");
        participantLabel = i18n.t("Participant");
        visitorLabel = i18n.t("Visitor");
    }

    public void addUser(final RoomUserUI roomUser, final UserGridMenuItemList menuItemList) {
        UserGridMenu menu = new UserGridMenu(presenter);
        menu.setMenuItemList(menuItemList);
        super.addUser(roomUser, menu, formatUserType(roomUser.getUserType()));
    }

    public View getView() {
        return this;
    }

    public void removeAllUsers() {
        super.removeAllUsers();
    }

    public void removeUser(final RoomUserUI roomUser) {
        super.removeUser(roomUser);

    }

    public void updateUser(final RoomUserUI roomUser, final UserGridMenuItemList menuItemList) {
        UserGridMenu menu = new UserGridMenu(presenter);
        menu.setMenuItemList(menuItemList);
        super.removeUser(roomUser);
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
