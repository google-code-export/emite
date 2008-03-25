/*
 *
 * Copyright (C) 2007 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * Kune is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kune is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.calclab.examplechat.client.chatuiplugin.dialog;

import java.util.HashMap;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.examplechat.client.chatuiplugin.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.AbstractChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUserListView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.utils.ChatIcons;
import com.calclab.examplechat.client.chatuiplugin.utils.EmoticonPaletteListener;
import com.calclab.examplechat.client.chatuiplugin.utils.EmoticonPalettePanel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
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
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
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

    public MultiChatPanel(final I18nTranslationService i18n, final MultiChatPresenter presenter) {
        this.i18n = i18n;
        this.presenter = presenter;
        this.userListToIndex = new HashMap<GroupChatUserListView, Integer>();
        panelIdToChat = new HashMap<String, AbstractChat>();
        createLayout();
        setStatus(STATUS_OFFLINE);
        setGroupChatUsersPanelVisible(false);
        setInviteToGroupChatButtonEnabled(false);
    }

    public void addChat(final AbstractChat chat) {
        Panel chatPanel = (Panel) chat.getView();
        centerPanel.add(chatPanel);
        String panelId = chatPanel.getId();
        panelIdToChat.put(panelId, chat);
        centerPanel.activate(panelId);
        chatPanel.show();
        if (centerPanel.hasItem(infoPanelId)) {
            centerPanel.remove(infoPanelId);
        }

    }

    public void closeAllChats() {
        Component[] items = centerPanel.getItems();
        for (int i = 0; i < items.length; i++) {
            centerPanel.remove(items[i]);
        }
    }

    public void highlightChat(final AbstractChat chat) {
        // TODO
    }

    public void unHighlightChat(final AbstractChat chat) {
        // TODO
    }

    public void show() {
        dialog.show();
        dialog.expand();
        // if (bottomIcon == null) {
        // bottomIcon = new BottomTrayIcon(i18n.t("Show/hide chat
        // dialog"));
        // bottomIcon.addMainButton(Images.App.getInstance().chat(), new
        // Command() {
        // public void execute() {
        // if (dialog.isVisible()) {
        // dialog.hide();
        // } else {
        // dialog.show();
        // }
        // }
        // });
        // presenter.attachIconToBottomBar(bottomIcon);
        // }
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

    public void setSubject(final String text) {
        subject.setValue(text);
    }

    public void setSubjectEditable(final boolean editable) {
        subject.setDisabled(editable);
    }

    public void setInputEditable(final boolean editable) {
        input.setDisabled(editable);
    }

    public void showUserList(final GroupChatUserListView view) {
        Integer index = userListToIndex.get(view);
        groupChatUsersDeckPanel.showWidget(index.intValue());
        usersPanel.setActiveItem(1);
    }

    public void addGroupChatUsersPanel(final GroupChatUserListView view) {
        groupChatUsersDeckPanel.add((Widget) view);
        userListToIndex.put(view, new Integer(groupChatUsersDeckPanel.getWidgetIndex((Widget) view)));
    }

    public void removeGroupChatUsersPanel(final GroupChatUserListView view) {
        Integer index = userListToIndex.get(view);
        groupChatUsersDeckPanel.remove(index.intValue());
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

    public void addPresenceBuddy(final String name, final String title, final int status) {
        // StackSubItemAction[] actions = {
        // new StackSubItemAction(Images.App.getInstance().chat(),
        // i18n.t("Start a chat with this person"),
        // WorkspaceEvents.GOTO), StackSubItemAction.DEFAULT_VISIT_GROUP };
        // usersStack.addStackSubItem(MYBUDDIES, getStatusIcon(status), name,
        // title, actions, presenter);
    }

    public void removePresenceBuddy(final String name) {
        // usersStack.removeStackSubItem(MYBUDDIES, name);
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
        String icon = getStatusIcon(status).getHTML();
        statusButton.setText(icon);
    }

    public void setGroupChatUsersPanelVisible(final boolean visible) {
        groupChatUsersPanel.setVisible(visible);
        if (visible == true) {
            usersPanel.setActiveItemID(groupChatUsersPanel.getId());
        }

    }

    public void setInviteToGroupChatButtonEnabled(final boolean enable) {
        inviteUserToGroupChat.setVisible(enable);
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
        centerPanel.setActiveTab(0);
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
        dialog.addListener(new WindowListenerAdapter() {

            // public boolean doBeforeHide(final Component component) {
            // if (centralLayout.getNumPanels() > 0) {
            // if (presenter.isCloseAllConfirmed()) {
            // return true;
            // } else {
            // MessageBox.confirm(i18n.t("Confirm"), i18n
            // .t("Are you sure you want to exit all the rooms?"), new
            // MessageBox.ConfirmCallback() {
            // public void execute(final String btnID) {
            // if (btnID.equals("yes")) {
            // presenter.closeAllRooms();
            // } else {
            // presenter.onCloseAllNotConfirmed();
            // }
            // }
            // });
            // return false;
            // }
            // }
            // return true;
            // }

            public void onCollapse(final Panel panel) {
                // dialog.hide();
            }

            public void onClose(final Panel panel) {
                Log.debug("Close chat dialog");
            }

        });

        centerPanel.addListener(new PanelListenerAdapter() {

            public boolean doBeforeRemove(final Container self, final Component component) {
                final String panelId = component.getId();
                if (component.getId().equals(infoPanel.getId())) {
                    // Closing empty chats info
                    return true;
                }
                final AbstractChatPresenter chatPresenter = (AbstractChatPresenter) panelIdToChat.get(panelId);
                if (presenter.isCloseAllConfirmed() || chatPresenter.isCloseConfirmed()) {
                    panelIdToChat.remove(panelId);
                    if (chatPresenter.getType() == AbstractChat.TYPE_PAIR_CHAT) {
                        presenter.closePairChat((PairChatPresenter) chatPresenter);
                    } else {
                        removeGroupChatUsersPanel(((GroupChatPresenter) chatPresenter).getUsersListView());
                        presenter.closeGroupChat((GroupChatPresenter) chatPresenter);
                    }
                    if (centerPanel.getComponents().length == 1) {
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
                    DOM.setStyleAttribute(dialog.getElement(), "zIndex", "9000");
                    // DOM.setStyleAttribute(MessageBox.getDialog().getElement(),
                    // "zIndex", "10000");
                }
                return false;
            }

        });
    }

    private Panel createUsersPanel() {
        usersPanel = new Panel();
        usersPanel.setLayout(new AccordionLayout(true));
        usersPanel.setAutoScroll(true);
        usersPanel.setBorder(false);
        groupChatUsersDeckPanel = new DeckPanel();
        groupChatUsersDeckPanel.addStyleName("emite-MultiChatPanel-User");
        Panel buddiesPanel = new Panel(i18n.t("My buddies"));
        buddiesPanel.setIconCls("userf-icon");
        groupChatUsersPanel = new Panel(i18n.t("Now in this room"));
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
        Item closeAllOption = new Item();
        closeAllOption.setText(i18n.t("Close all chats"));
        ColorMenu colorMenu = new ColorMenu();
        colorMenu.addListener(new ColorMenuListener() {
            public void onSelect(final ColorPalette colorPalette, final String color) {
                presenter.onUserColorChanged(color);
            }
        });
        MenuItem colorMenuItem = new MenuItem("Choose your color", colorMenu);
        optionsMenu.addItem(joinOption);
        optionsMenu.addItem(colorMenuItem);
        optionsMenu.addItem(closeAllOption);
        ToolbarMenuButton optionsMenuButton = new ToolbarMenuButton(i18n.t("Options"));
        optionsMenuButton.setMenu(optionsMenu);
        topToolbar.addButton(optionsMenuButton);
        topToolbar.addSeparator();
        closeAllOption.addListener(new BaseItemListenerAdapter() {
            public void onClick(final BaseItem item, final EventObject e) {
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
        checkItem.setText(getStatusText(status));
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
        // As height 100% doesn't works
        subject.setHeight(27);
        // TODO: Fixed in gwt-ext 2.0.3 TextArea.setEnterIsSpecial
        subject.addListener(new TextFieldListenerAdapter() {
            public void onSpecialKey(final Field field, final EventObject e) {
                Log.debug("Special key: " + e.getKey());
                if (e.getKey() == 13) {
                    presenter.changeGroupChatSubject(field.getValueAsString());
                    e.stopEvent();
                }
            }
        });
        subject.addListener(new FieldListenerAdapter() {
            public void onSpecialKey(final Field field, final EventObject e) {
                Log.debug("Special key: " + e.getKey());
                if (e.getKey() == 13) {
                    presenter.changeGroupChatSubject(field.getValueAsString());
                    e.stopEvent();
                }
            }
        });

        subject.addKeyListener(13, new KeyListener() {

            public void onKey(final int key, final EventObject e) {
                presenter.changeGroupChatSubject(subject.getValueAsString());
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
            }
        });

        inputForm.add(input, new AnchorLayoutData("100% 100%"));

        /* Input toolbar */
        final Toolbar inputToolbar = new Toolbar();
        ToolbarButton emoticonIcon = new ToolbarButton();
        emoticonIcon.setIcon("images/smile.png");
        emoticonIcon.setCls("x-btn-icon x-btn-focus");
        emoticonIcon.setTooltip(i18n.t("Insert a emoticon"));

        emoticonIcon.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                showEmoticonPalette(e.getXY()[0], e.getXY()[1]);
            }
        });
        inputToolbar.addButton(emoticonIcon);
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

    private AbstractImagePrototype getStatusIcon(final int status) {
        switch (status) {
        case STATUS_ONLINE:
            return icons.online();
        case STATUS_OFFLINE:
            return icons.offline();
        case STATUS_BUSY:
            return icons.busy();
        case STATUS_INVISIBLE:
            return icons.invisible();
        case STATUS_XA:
            return icons.extendedAway();
        case STATUS_AWAY:
            return icons.away();
        case STATUS_MESSAGE:
            return icons.message();
        default:
            throw new IndexOutOfBoundsException("Xmpp status unknown");

        }
    }

    private String getStatusText(final int status) {
        String textLabel;

        switch (status) {
        case STATUS_ONLINE:
            textLabel = i18n.t("online");
            break;
        case STATUS_OFFLINE:
            textLabel = i18n.t("offline");
            break;
        case STATUS_BUSY:
            textLabel = i18n.t("busy");
            break;
        case STATUS_INVISIBLE:
            textLabel = i18n.t("invisible");
            break;
        case STATUS_XA:
            textLabel = i18n.t("extended away");
            break;
        case STATUS_AWAY:
            textLabel = i18n.t("away");
            break;
        default:
            throw new IndexOutOfBoundsException("Xmpp status unknown");
        }
        return getStatusIcon(status).getHTML() + "&nbsp;" + textLabel;
    }

    private void createInfoPanel() {
        infoPanel = new Panel();
        infoPanel.setTitle(i18n.t("Info"));
        infoPanel.setClosable(false);
        infoPanel.add(new Label(i18n.t("To start a chat, select a buddy or join to a chat room")));
        infoPanel.setPaddings(7);
        infoPanelId = infoPanel.getId();
        addInfoPanel();
    }

    private void addInfoPanel() {
        centerPanel.add(infoPanel);
        infoPanel.show();
        centerPanel.activate(infoPanelId);
    }

    private void doSend(final EventObject e) {
        presenter.onSend();
        e.stopEvent();
        input.focus();
    }

}
