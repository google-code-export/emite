package com.calclab.examplechat.client.chatuiplugin.roster;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.examplechat.client.chatuiplugin.EmiteUiPlugin;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.UserGrid;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenu;
import com.gwtext.client.widgets.MessageBox;

public class RosterUIPanel extends UserGrid implements RosterUIView {

    private final RosterUIPresenter presenter;
    private final I18nTranslationService i18n;

    public RosterUIPanel(final I18nTranslationService i18n, final RosterUIPresenter presenter) {
        this.i18n = i18n;
        this.presenter = presenter;
    }

    public void addRosterItem(final PairChatUser user) {
        UserGridMenu menu = new UserGridMenu(presenter);
        menu.addMenuOption(i18n.t("Start a chat with this person"), "chat-icon", EmiteUiPlugin.ON_PAIR_CHAT_START, user
                .getUri());
        super.addUser(user, menu);
    }

    public void updateRosterItem(final PairChatUser user) {
        super.udpateRosterItem(user);
    }

    public void removeRosterItem(final PairChatUser user) {
        super.removeUser(user);
        MessageBox.alert(i18n.t("[%s] has removed you from his/her buddie list", user.getUri().toString()));
    }

    public void confirmSusbscriptionRequest(final Presence presence) {
        MessageBox.confirm(i18n.t("Confirm"), i18n.t("[%s] want to add you as a buddy. Do you want to permit?",
                presence.getFrom()), new MessageBox.ConfirmCallback() {
            public void execute(final String btnID) {
                if (btnID.equals("yes")) {
                    presenter.onPresenceAccepted(presence);
                } else {
                    presenter.onPresenceNotAccepted(presence);
                }
            }
        });
    }

    public void clearRoster() {
        super.removeAllUsers();
    }
}
