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
package com.calclab.emiteui.client.emiteuiplugin.dialog;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.im.roster.Roster.SubscriptionMode;
import com.calclab.emiteui.client.emiteuiplugin.dialog.OwnPresence.OwnStatus;
import com.calclab.emiteui.client.emiteuiplugin.room.JoinRoomDialogPanel;
import com.calclab.emiteui.client.emiteuiplugin.roster.RosterItemDialog;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ColorPalette;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.ColorMenu;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.TextItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.ColorMenuListener;

public class MultiChatPanelTopBar extends Toolbar {
    private final Menu statusMenu;
    private CheckItem onlineMenuItem;
    private CheckItem offlineMenuItem;
    private CheckItem busyCustomMenuItem;
    private CheckItem onlineCustomMenuItem;
    private CheckItem busyMenuItem;
    private final ToolbarMenuButton statusButton;
    private final ToolbarButton inviteUserToGroupChat;
    private final I18nTranslationService i18n;
    private final MultiChatPresenter presenter;
    private final Item closeAllOption;
    private final ToolbarButton loading;
    private final ToolbarButton addRosterItem;
    private CheckItem manualSubsItem;
    private CheckItem autoRejectSubsItem;
    private CheckItem autoAcceptSubsItem;
    private final Item joinOption;

    public MultiChatPanelTopBar(final I18nTranslationService i18n, final MultiChatPresenter presenter) {
        this.i18n = i18n;
        this.presenter = presenter;

        Menu chatMenu = new Menu();
        chatMenu.setShadow(true);
        joinOption = new Item();
        joinOption.setText(i18n.t("Join a chat room"));
        joinOption.setIcon("images/group-chat.gif");
        joinOption.addListener(new BaseItemListenerAdapter() {
            private JoinRoomDialogPanel joinRoomDialogPanel;

            public void onClick(final BaseItem item, final EventObject e) {
                if (joinRoomDialogPanel == null) {
                    joinRoomDialogPanel = new JoinRoomDialogPanel(i18n, presenter);
                }
                joinRoomDialogPanel.show();
            }
        });
        closeAllOption = createCloseAllMenuItem(i18n);

        MenuItem optionsItem = new MenuItem(i18n.t("Options"), createOptionsMenu());
        chatMenu.addItem(joinOption);
        chatMenu.addItem(closeAllOption);
        chatMenu.addItem(optionsItem);

        ToolbarMenuButton chatMenuButton = new ToolbarMenuButton(i18n.t("Chat"));
        chatMenuButton.setMenu(chatMenu);
        this.addButton(chatMenuButton);
        this.addSeparator();

        statusMenu = createStatusMenu();
        statusButton = new ToolbarMenuButton("Set status", statusMenu);
        statusButton.setTooltip(i18n.t("Set status"));
        this.addButton(statusButton);
        statusButton.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                statusMenu.show("chat-menu-button");
            }
        });

        loading = new ToolbarButton();
        loading.setIcon("images/ext-load.gif");
        loading.setCls("x-btn-icon");
        this.addButton(loading);
        setLoadingVisible(false);

        this.addFill();

        inviteUserToGroupChat = new ToolbarButton();
        inviteUserToGroupChat.setIcon("images/group_add.gif");
        inviteUserToGroupChat.setCls("x-btn-icon");
        inviteUserToGroupChat.setTooltip(i18n.t("Invite another user to this chat room"));

        addRosterItem = new ToolbarButton();
        addRosterItem.setIcon("images/user_add.gif");
        addRosterItem.setCls("x-btn-icon");
        addRosterItem.setTooltip(i18n.t("Add a new buddy"));

        // final EntityLiveSearchListener inviteUserToRoomListener = new
        // EntityLiveSearchListener() {
        // public void onSelection(String shortName, String longName) {
        // presenter.inviteUserToRoom(shortName, longName);
        // }
        // };

        // final EntityLiveSearchListener addBuddyListener = new
        // EntityLiveSearchListener() {
        // public void onSelection(String shortName, String longName) {
        // presenter.addBuddy(shortName, longName);
        // }
        // };

        addRosterItem.addListener(new ButtonListenerAdapter() {
            private RosterItemDialog rosterItemDialog;

            public void onClick(final Button button, final EventObject e) {
                if (rosterItemDialog == null) {
                    rosterItemDialog = new RosterItemDialog(i18n, presenter);
                }
                rosterItemDialog.show();
                // DefaultDispatcher.getInstance().fire(PlatformEvents.ADD_USERLIVESEARCH,
                // addBuddyListener, null);
            }
        });

        inviteUserToGroupChat.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                MessageBox.alert("In development");
                // DefaultDispatcher.getInstance().fire(PlatformEvents.ADD_USERLIVESEARCH,
                // inviteUserToRoomListener, null);
            }
        });

        this.addButton(addRosterItem);

        this.addButton(inviteUserToGroupChat);

        setSubscritionMode(presenter.getUserChatOptions().getSubscriptionMode());
    }

    public void confirmCloseAll() {
        MessageBox.confirm(i18n.t("Confirm"), i18n.t("Are you sure you want to exit all the chats?"),
                new MessageBox.ConfirmCallback() {
                    public void execute(final String btnID) {
                        if (btnID.equals("yes")) {
                            presenter.onCloseAllConfirmed();
                        }
                    }
                });
    }

    public void setAddRosterItemButtonVisible(final boolean visible) {
        addRosterItem.setVisible(visible);
    }

    public void setCloseAllOptionEnabled(boolean enabled) {
        closeAllOption.setDisabled(!enabled);
    }

    public void setInviteToGroupChatButtonVisible(final boolean enable) {
        inviteUserToGroupChat.setVisible(enable);
    }

    public void setJoinRoomEnabled(final boolean enabled) {
        joinOption.setDisabled(!enabled);
    }

    public void setLoadingVisible(final boolean visible) {
        loading.setVisible(visible);
    }

    public void setOwnPresence(final OwnPresence ownPresence) {
        switch (ownPresence.getStatus()) {
        case online:
            onlineMenuItem.setChecked(true);
            break;
        case onlinecustom:
            onlineCustomMenuItem.setChecked(true);
            break;
        case offline:
            offlineMenuItem.setChecked(true);
            break;
        case busy:
            busyMenuItem.setChecked(true);
            break;
        case busycustom:
            busyCustomMenuItem.setChecked(true);
            break;
        }

        String icon = StatusUtil.getStatusIcon(ownPresence.getStatus()).getHTML();
        statusButton.setText(icon);
    }

    public void setSubscritionMode(final SubscriptionMode mode) {
        switch (mode) {
        case auto_accept_all:
            autoAcceptSubsItem.setChecked(true);
            break;
        case auto_reject_all:
            autoRejectSubsItem.setChecked(true);
            break;
        default:
            manualSubsItem.setChecked(true);
            break;
        }
    }

    private Item createCloseAllMenuItem(final I18nTranslationService i18n) {
        Item closeAllOption = new Item();
        closeAllOption.setText(i18n.t("Close all chats"));
        closeAllOption.setIconCls("exit-icon");
        closeAllOption.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                confirmCloseAll();
            }
        });
        return closeAllOption;
    }

    private Menu createOptionsMenu() {
        Menu submenu = new Menu();

        ColorMenu colorMenu = new ColorMenu();
        colorMenu.addListener(new ColorMenuListener() {
            public void onSelect(final ColorPalette colorPalette, final String color) {
                presenter.onUserColorChanged(color);
            }
        });

        MenuItem colorMenuItem = new MenuItem(i18n.t("Choose your color"), colorMenu);
        colorMenuItem.setIconCls("colors-icon");
        colorMenuItem.setIcon("css/img/colors.gif");

        MenuItem subsItem = new MenuItem(i18n.t("New buddies options"), createUserSubscriptionMenu());

        submenu.addItem(colorMenuItem);
        submenu.addItem(subsItem);

        return submenu;
    }

    private CheckItem createStatusCheckItem(final OwnStatus ownStatus) {

        CheckItem checkItem = new CheckItem();
        checkItem.setText(StatusUtil.getStatusIconAndText(i18n, ownStatus));
        checkItem.setGroup("chatstatus");
        switch (ownStatus) {
        case offline:
        case online:
        case busy:
            checkItem.addListener(new BaseItemListenerAdapter() {
                public void onClick(final BaseItem item, final EventObject e) {
                    presenter.onStatusSelected(new OwnPresence(ownStatus));
                }
            });
            break;
        case busycustom:
        case onlinecustom:
            checkItem.addListener(new BaseItemListenerAdapter() {
                public void onClick(final BaseItem item, final EventObject e) {
                    MessageBox.prompt(i18n.t("Set your status message"), i18n
                            .t("Set your status text (something like 'Out for dinner' or 'Working')"),
                            new PromptCallback() {
                                public void execute(final String btnID, final String text) {
                                    presenter.onStatusSelected(new OwnPresence(ownStatus, text));
                                }
                            });
                }
            });
            break;
        }
        return checkItem;
    }

    private Menu createStatusMenu() {
        Menu statusMenu = new Menu();
        statusMenu.setShadow(true);
        statusMenu.addItem(new TextItem("<b class=\"menu-title\">" + i18n.t("Change your status") + "</b>"));
        onlineMenuItem = createStatusCheckItem(OwnStatus.online);
        onlineCustomMenuItem = createStatusCheckItem(OwnStatus.onlinecustom);
        busyMenuItem = createStatusCheckItem(OwnStatus.busy);
        busyCustomMenuItem = createStatusCheckItem(OwnStatus.busycustom);
        offlineMenuItem = createStatusCheckItem(OwnStatus.offline);
        statusMenu.addItem(onlineMenuItem);
        statusMenu.addItem(onlineCustomMenuItem);
        statusMenu.addItem(busyMenuItem);
        statusMenu.addItem(busyCustomMenuItem);
        statusMenu.addSeparator();
        statusMenu.addItem(offlineMenuItem);
        return statusMenu;
    }

    private CheckItem createSubscritionItem(final String text, final Menu submenu, final SubscriptionMode mode) {
        final CheckItemListenerAdapter listener = new CheckItemListenerAdapter() {
            public void onCheckChange(CheckItem item, boolean checked) {
                if (checked) {
                    presenter.onUserSubscriptionModeChanged(mode);
                }
            }
        };
        CheckItem item = new CheckItem();
        item.setText(text);
        item.setGroup("subscription");
        item.addListener(listener);
        submenu.addItem(item);
        return item;
    }

    private Menu createUserSubscriptionMenu() {
        Menu submenu = new Menu();
        submenu.setShadow(true);
        submenu.setMinWidth(10);
        autoAcceptSubsItem = createSubscritionItem(i18n
                .t("Automatically accept users as buddies when a user request it"), submenu,
                SubscriptionMode.auto_accept_all);
        autoRejectSubsItem = createSubscritionItem(i18n.t("Automatically reject new buddies inclusion requests"),
                submenu, SubscriptionMode.auto_reject_all);
        manualSubsItem = createSubscritionItem(i18n.t("Manual accept or reject new buddies inclusion requests"),
                submenu, SubscriptionMode.manual);
        return submenu;
    }
}
