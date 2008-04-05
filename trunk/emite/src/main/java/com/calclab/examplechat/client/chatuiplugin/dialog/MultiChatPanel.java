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
package com.calclab.examplechat.client.chatuiplugin.dialog;

import java.util.HashMap;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.BottomTrayIcon;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.calclab.examplechat.client.chatuiplugin.ChatDialogPlugin;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserListPanel;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserListView;
import com.calclab.examplechat.client.chatuiplugin.users.UserGrid;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenu;
import com.calclab.examplechat.client.chatuiplugin.utils.ChatIcons;
import com.calclab.examplechat.client.chatuiplugin.utils.EmoticonPaletteListener;
import com.calclab.examplechat.client.chatuiplugin.utils.EmoticonPalettePanel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.ColorPalette;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Container;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarMenuButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.KeyListener;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.ColorMenu;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.TextItem;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;
import com.gwtext.client.widgets.menu.event.ColorMenuListener;

public class MultiChatPanel implements MultiChatView {
    private static final ChatIcons icons = ChatIcons.App.getInstance();
    private Window dialog;
    private Button sendBtn;
    private final MultiChatPresenter presenter;
    private TextArea subject;
    private DeckPanel groupChatUsersDeckPanel;
    private TextArea input;
    private final HashMap<GroupChatUserListView, Integer> userListToIndex;
    private final HashMap<String, AbstractChat> panelIdToChat;
    private EmoticonPalettePanel emoticonPalettePanel;
    private PopupPanel emoticonPopup;
    private Menu statusMenu;
    private CheckItem onlineMenuItem;
    private CheckItem offlineMenuItem;
    private CheckItem busyMenuItem;
    private CheckItem awayMenuItem;
    private ToolbarMenuButton statusButton;
    private ToolbarButton inviteUserToGroupChat;
    private TabPanel centerPanel;
    private Panel usersPanel;
    private final I18nTranslationService i18n;
    private Panel infoPanel;
    private Panel groupChatUsersPanel;
    private String infoPanelId;
    private Panel buddiesPanel;
    private Item closeAllOption;
    private BottomTrayIcon bottomIcon;
    private UserGrid buddiesGrid;
    private ToolbarButton emoticonButton;

    public MultiChatPanel(final I18nTranslationService i18n, final MultiChatPresenter presenter) {
        this.i18n = i18n;
        this.presenter = presenter;
        this.userListToIndex = new HashMap<GroupChatUserListView, Integer>();
        panelIdToChat = new HashMap<String, AbstractChat>();
        createLayout();
        reset();
    }

    public void addChat(final AbstractChat chat) {
        Panel chatPanel = (Panel) chat.getView();
        centerPanel.add(chatPanel);
        String panelId = chatPanel.getId();
        panelIdToChat.put(panelId, chat);
        activateChat(chat);
        // TODO put in presenter
        if (centerPanel.hasItem(infoPanelId)) {
            centerPanel.remove(infoPanelId);
        }
    }

    public void activateChat(final AbstractChat chat) {
        Panel chatPanel = (Panel) chat.getView();
        centerPanel.activate(chatPanel.getId());
        centerPanel.scrollToTab(chatPanel, true);
    }

    public void closeAllChats() {
        // TODO put in presenter
        Component[] items = centerPanel.getItems();
        for (int i = 0; i < items.length; i++) {
            centerPanel.remove(items[i].getId());
        }
    }

    public void highlightChat(final AbstractChat chat) {
        // TODO
        // centerPanel.getActiveTab().setIconCls("emite-icon");
    }

    public void unHighlightChat(final AbstractChat chat) {
        // TODO
    }

    public void show() {
        dialog.show();
        dialog.expand();
        if (bottomIcon == null) {
            bottomIcon = new BottomTrayIcon(i18n.t("Show/hide chat dialog"));
            bottomIcon.addMainButton(icons.chat(), new Command() {
                public void execute() {
                    if (dialog.isVisible()) {
                        dialog.hide();
                    } else {
                        dialog.show();
                    }
                }
            });
            presenter.attachIconToBottomBar(bottomIcon);
        }
    }

    public void destroy() {
        dialog.destroy();
    }

    public void setSendEnabled(final boolean enabled) {
        if (enabled) {
            sendBtn.enable();
        } else {
            sendBtn.disable();
        }
    }

    public void setCloseAllOptionEnabled(boolean enabled) {
        closeAllOption.setDisabled(!enabled);
    }

    public void setSubject(final String text) {
        subject.setValue(text);
    }

    public void setSubjectEditable(final boolean editable) {
        subject.setDisabled(!editable);
    }

    public void setInputEditable(final boolean editable) {
        input.setDisabled(!editable);
    }

    public void setEmoticonButton(final boolean enabled) {
        emoticonButton.setDisabled(!enabled);
    }

    public void showUserList(final GroupChatUserListView view) {
        Integer index = userListToIndex.get(view);
        groupChatUsersDeckPanel.showWidget(index.intValue());
        ((GroupChatUserListPanel) view).doLayout();
        usersPanel.setActiveItem(1);
    }

    public void addGroupChatUsersPanel(final GroupChatUserListView view) {
        groupChatUsersDeckPanel.add((Widget) view);
        userListToIndex.put(view, new Integer(groupChatUsersDeckPanel.getWidgetIndex((Widget) view)));
    }

    public void removeGroupChatUsersPanel(final GroupChatUserListView view) {
        groupChatUsersDeckPanel.remove((Widget) view);
        userListToIndex.remove(view);
    }

    public void clearInputText() {
        input.reset();
    }

    public void clearSubject() {
        subject.reset();
    }

    public void setInputText(final String text) {
        input.setRawValue(text);
    }

    public String getInputText() {
        return input.getValueAsString();
    }

    public void addPresenceBuddy(final PairChatUser user) {
        UserGridMenu menu = new UserGridMenu(presenter);
        menu.addMenuOption(i18n.t("Start a chat with this person"), "chat-icon", ChatDialogPlugin.ON_PAIR_CHAT_START,
                user.getUri());
        buddiesGrid.addUser(user, menu);
    }

    public void removePresenceBuddy(final PairChatUser user) {
        buddiesGrid.removeUser(user);
    }

    public void setStatus(final int status) {
        switch (status) {
        case STATUS_ONLINE:
            onlineMenuItem.setChecked(true);
            break;
        case STATUS_OFFLINE:
            offlineMenuItem.setChecked(true);
            break;
        case STATUS_BUSY:
            busyMenuItem.setChecked(true);
            break;
        case STATUS_AWAY:
            awayMenuItem.setChecked(true);
            break;
        default:
            break;
        }
        String icon = StatusUtil.getStatusIcon(status).getHTML();
        statusButton.setText(icon);
    }

    public void setGroupChatUsersPanelVisible(final boolean visible) {
        groupChatUsersPanel.setVisible(visible);
        if (visible == true) {
            usersPanel.setActiveItemID(groupChatUsersPanel.getId());
            // buddiesPanel.collapse();
            groupChatUsersPanel.expand();
        }
    }

    public void setInviteToGroupChatButtonEnabled(final boolean enable) {
        inviteUserToGroupChat.setVisible(enable);
    }

    public void confirmCloseAll() {
        MessageBox.confirm(i18n.t("Confirm"), i18n.t("Are you sure you want to exit all the chats?"),
                new MessageBox.ConfirmCallback() {
                    public void execute(final String btnID) {
                        if (btnID.equals("yes")) {
                            presenter.onCloseAllConfirmed();
                        } else {
                            presenter.onCloseAllNotConfirmed();
                        }
                    }
                });
    }

    private void createLayout() {
        dialog = new BasicDialog(i18n.t("Chats"), false, false, 600, 415, 300, 300);
        dialog.setBorder(false);
        dialog.setCollapsible(true);
        dialog.setIconCls("chat-icon");
        sendBtn = new Button(i18n.t("Send"));
        sendBtn.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                doSend(e);
            }
        });
        dialog.addButton(sendBtn);
        dialog.setLayout(new BorderLayout());

        Panel northPanel = new Panel();
        northPanel.setHeight(54);
        northPanel.add(createSubjectPanel());
        northPanel.setBorder(false);
        BorderLayoutData northData = new BorderLayoutData(RegionPosition.NORTH);
        dialog.add(northPanel, northData);

        Panel southPanel = new Panel();
        southPanel.setHeight(75);
        southPanel.add(createInputPanel());
        southPanel.setBorder(false);
        BorderLayoutData southData = new BorderLayoutData(RegionPosition.SOUTH);
        southData.setSplit(true);
        dialog.add(southPanel, southData);

        Panel eastPanel = new Panel(i18n.t("Users"));
        eastPanel.setWidth(150);
        eastPanel.setCollapsible(true);
        eastPanel.setBorder(false);
        eastPanel.setLayout(new FitLayout());
        eastPanel.add(createUsersPanel());
        BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);

        eastData.setMinSize(100);
        eastData.setMaxSize(250);
        eastData.setSplit(true);
        eastData.setMargins(3, 3, 0, 3);
        eastData.setCMargins(3, 3, 3, 3);
        eastData.setSplit(true);
        dialog.add(eastPanel, eastData);

        centerPanel = new TabPanel();
        // centerPanel.setActiveTab(0);
        centerPanel.setBorder(true);
        centerPanel.setEnableTabScroll(true);
        centerPanel.setAutoScroll(false);
        BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        createInfoPanel();
        centerPanel.add(infoPanel);
        dialog.add(centerPanel, centerData);

        createListeners();

    }

    private void createListeners() {

        centerPanel.addListener(new PanelListenerAdapter() {

            public boolean doBeforeRemove(final Container self, final Component component) {
                final String panelId = component.getId();
                if (component.getId().equals(infoPanel.getId())) {
                    // Closing empty chats info
                    return true;
                }
                // FIXME: Move to presenter...
                final AbstractChatPresenter chatPresenter = (AbstractChatPresenter) panelIdToChat.get(panelId);
                if (presenter.isCloseAllConfirmed() || chatPresenter.isCloseConfirmed()) {
                    panelIdToChat.remove(panelId);
                    if (chatPresenter.getType() == AbstractChat.TYPE_PAIR_CHAT) {
                        presenter.closePairChat((PairChatPresenter) chatPresenter);
                    } else {
                        removeGroupChatUsersPanel(((GroupChatPresenter) chatPresenter).getUsersListView());
                        presenter.closeGroupChat((GroupChatPresenter) chatPresenter);
                    }
                    if (centerPanel.getComponents() == null || centerPanel.getComponents().length == 1) {
                        addInfoPanel();
                    }
                    return true;
                } else {
                    MessageBox.confirm(i18n.t("Confirm"), i18n.t("Are you sure you want to exit from this chat?"),
                            new MessageBox.ConfirmCallback() {
                                public void execute(final String btnID) {
                                    if (btnID.equals("yes")) {
                                        chatPresenter.onCloseConfirmed();
                                        self.remove(panelId);
                                    } else {
                                        chatPresenter.onCloseNotConfirmed();
                                    }
                                }
                            });
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            DOM.setStyleAttribute(dialog.getElement(), "zIndex", "9000");
                            DOM.setStyleAttribute(MessageBox.getDialog().getElement(), "zIndex", "10000");
                        }
                    });
                }
                return false;
            }

        });
    }

    private Panel createUsersPanel() {
        usersPanel = new Panel();
        usersPanel.setLayout(new AccordionLayout(true));
        usersPanel.setAutoScroll(false);
        usersPanel.setBorder(false);
        // Maybe use Accordion playing with visibility instead of gwt's
        // deckpanel
        groupChatUsersDeckPanel = new DeckPanel();
        groupChatUsersDeckPanel.addStyleName("emite-MultiChatPanel-User");
        buddiesPanel = new Panel(i18n.t("My buddies"));
        buddiesPanel.setLayout(new FitLayout());
        buddiesPanel.setAutoScroll(true);
        buddiesPanel.setIconCls("userf-icon");
        buddiesPanel.setBorder(false);
        buddiesGrid = new UserGrid();
        buddiesPanel.add(buddiesGrid);
        groupChatUsersPanel = new Panel(i18n.t("Now in this room"));
        groupChatUsersPanel.setLayout(new FitLayout());
        groupChatUsersPanel.setAutoScroll(true);
        groupChatUsersPanel.setIconCls("group-icon");
        groupChatUsersPanel.add(groupChatUsersDeckPanel);
        usersPanel.add(buddiesPanel);
        usersPanel.add(groupChatUsersPanel);
        return usersPanel;
    }

    private Toolbar createTopToolbar() {
        final Toolbar topToolbar = new Toolbar();

        Menu optionsMenu = new Menu();
        optionsMenu.setShadow(true);
        Item joinOption = new Item();
        joinOption.setText(i18n.t("Join a chat room"));
        closeAllOption = new Item();
        closeAllOption.setText(i18n.t("Close all chats"));
        ColorMenu colorMenu = new ColorMenu();
        colorMenu.addListener(new ColorMenuListener() {
            public void onSelect(final ColorPalette colorPalette, final String color) {
                presenter.onUserColorChanged(color);
            }
        });
        MenuItem colorMenuItem = new MenuItem("Choose your color", colorMenu);
        colorMenuItem.setIconCls("colors-icon");
        colorMenuItem.setIcon("css/img/colors.gif");
        optionsMenu.addItem(joinOption);
        optionsMenu.addItem(colorMenuItem);
        optionsMenu.addItem(closeAllOption);
        ToolbarMenuButton optionsMenuButton = new ToolbarMenuButton(i18n.t("Options"));
        optionsMenuButton.setMenu(optionsMenu);
        topToolbar.addButton(optionsMenuButton);
        topToolbar.addSeparator();
        closeAllOption.setIconCls("exit-icon");
        closeAllOption.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                confirmCloseAll();
            }
        });

        statusMenu = new Menu();
        statusMenu.setShadow(true);

        statusMenu.addItem(new TextItem("<b class=\"menu-title\">" + i18n.t("Change your status") + "</b>"));

        onlineMenuItem = createStatusCheckItem(STATUS_ONLINE);
        offlineMenuItem = createStatusCheckItem(STATUS_OFFLINE);
        busyMenuItem = createStatusCheckItem(STATUS_BUSY);
        awayMenuItem = createStatusCheckItem(STATUS_AWAY);

        statusMenu.addItem(onlineMenuItem);
        statusMenu.addItem(offlineMenuItem);
        statusMenu.addItem(busyMenuItem);
        statusMenu.addItem(awayMenuItem);

        statusButton = new ToolbarMenuButton("Set status", statusMenu);
        statusButton.setTooltip(i18n.t("Set status"));

        topToolbar.addButton(statusButton);

        statusButton.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                statusMenu.show("chat-menu-button");
            }
        });

        topToolbar.addSeparator();

        inviteUserToGroupChat = new ToolbarButton();
        inviteUserToGroupChat.setIcon("images/group_add.png");
        inviteUserToGroupChat.setCls("x-btn-icon");
        inviteUserToGroupChat.setTooltip(i18n.t("Invite another user to this chat room"));

        ToolbarButton buddyAdd = new ToolbarButton();
        buddyAdd.setIcon("images/user_add.png");
        buddyAdd.setCls("x-btn-icon");
        buddyAdd.setTooltip(i18n.t("Add a new buddy"));

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

        buddyAdd.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                // DefaultDispatcher.getInstance().fire(PlatformEvents.ADD_USERLIVESEARCH,
                // addBuddyListener, null);
            }
        });

        inviteUserToGroupChat.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                // DefaultDispatcher.getInstance().fire(PlatformEvents.ADD_USERLIVESEARCH,
                // inviteUserToRoomListener, null);
            }
        });

        topToolbar.addButton(buddyAdd);

        topToolbar.addButton(inviteUserToGroupChat);

        return topToolbar;
    }

    private CheckItem createStatusCheckItem(final int status) {
        CheckItem checkItem = new CheckItem();
        checkItem.setText(StatusUtil.getStatusIconAndText(i18n, status));
        checkItem.setGroup("chatstatus");

        checkItem.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
                presenter.onStatusSelected(status);
            }
        });
        return checkItem;
    }

    private Panel createSubjectPanel() {
        FormPanel subjectForm = createGenericInputForm();

        subject = new TextArea();
        subject.setTitle(i18n.t("Subject of the room"));
        // As height 100% doesn't works
        subject.setHeight(27);
        subject.addKeyListener(13, new KeyListener() {
            public void onKey(final int key, final EventObject e) {
                presenter.onSubjectChangedByCurrentUser(subject.getText());
                e.stopEvent();
            }
        });

        final Toolbar topToolbar = createTopToolbar();

        subjectForm.add(subject, new AnchorLayoutData("100% 100%"));

        Panel northPanel = createInputFormWithToolBar(subjectForm, topToolbar);
        northPanel.addStyleName("emite-MultiChatPanel-Subject");

        return northPanel;
    }

    private Panel createInputPanel() {
        final FormPanel inputForm = createGenericInputForm();
        input = new TextArea();
        // As height 100% doesn't works
        input.setHeight(46);
        input.addKeyListener(13, new KeyListener() {
            public void onKey(final int key, final EventObject e) {
                doSend(e);
                e.stopEvent();
            }
        });

        inputForm.add(input, new AnchorLayoutData("100% 100%"));

        /* Input toolbar */
        final Toolbar inputToolbar = new Toolbar();
        emoticonButton = new ToolbarButton();
        emoticonButton.setIcon("images/smile.png");
        emoticonButton.setCls("x-btn-icon x-btn-focus");
        emoticonButton.setTooltip(i18n.t("Insert a emoticon"));

        emoticonButton.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                showEmoticonPalette(e.getXY()[0], e.getXY()[1]);
            }
        });
        inputToolbar.addButton(emoticonButton);
        inputToolbar.addSeparator();

        Panel southPanel = createInputFormWithToolBar(inputForm, inputToolbar);

        return southPanel;
    }

    private FormPanel createGenericInputForm() {
        FormPanel form = new FormPanel();
        form.setLayout(new FitLayout());
        form.setHideLabels(true);
        form.setBorder(false);
        return form;
    }

    private Panel createInputFormWithToolBar(final FormPanel inputForm, final Toolbar topToolbar) {
        Panel panel = new Panel();
        panel.setLayout(new FitLayout());
        panel.setTopToolbar(topToolbar);
        panel.add(inputForm, new AnchorLayoutData("100% 100%"));
        inputForm.setWidth("100%");
        return panel;
    }

    private void showEmoticonPalette(final int x, final int y) {
        if (emoticonPalettePanel == null) {
            emoticonPalettePanel = new EmoticonPalettePanel(new EmoticonPaletteListener() {
                public void onEmoticonSelected(final String emoticonText) {
                    input.setRawValue(input.getText() + " " + emoticonText + " ");
                    emoticonPopup.hide();
                    input.focus();
                }
            });
        }
        emoticonPopup = new PopupPanel(true);
        emoticonPopup.setVisible(false);
        emoticonPopup.show();
        emoticonPopup.setPopupPosition(x + 2, y - 160);
        emoticonPopup.setWidget(emoticonPalettePanel);
        emoticonPopup.setVisible(true);
    }

    private void createInfoPanel() {
        infoPanel = new Panel();
        infoPanel.setTitle(i18n.t("Info"));
        infoPanel.setClosable(false);
        infoPanel.add(new Label(i18n.t("To start a chat, select a buddy or join to a chat room. "
                + "If you don't have buddies you can add them.")));
        infoPanel.setPaddings(7);
        infoPanelId = infoPanel.getId();
        addInfoPanel();
    }

    private void addInfoPanel() {
        reset();
        centerPanel.add(infoPanel);
        infoPanel.show();
        centerPanel.activate(infoPanelId);
    }

    private void doSend(final EventObject e) {
        presenter.onCurrentUserSend(getInputText());
        e.stopEvent();
        input.focus();
    }

    private void reset() {
        subject.reset();
        setGroupChatUsersPanelVisible(false);
        setInviteToGroupChatButtonEnabled(false);
    }
}
