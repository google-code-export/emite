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
package com.calclab.emiteuiplugin.client.dialog;

import java.util.HashMap;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.BottomTrayIcon;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emiteuiplugin.client.chat.ChatUI;
import com.calclab.emiteuiplugin.client.roster.RosterUI;
import com.calclab.emiteuiplugin.client.users.UserGridPanel;
import com.calclab.emiteuiplugin.client.utils.ChatIcons;
import com.calclab.emiteuiplugin.client.utils.emoticons.EmoticonPaletteListener;
import com.calclab.emiteuiplugin.client.utils.emoticons.EmoticonPalettePanel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.PopupPanel;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Container;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.TabPanel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Window;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.ContainerListenerAdapter;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.event.WindowListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextArea;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.layout.AccordionLayout;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class MultiChatPanel implements MultiChatView {
    private static final ChatIcons icons = ChatIcons.App.getInstance();
    private Window dialog;
    private Button sendBtn;
    private final MultiChatPresenter presenter;
    private TextField subject;
    private TextArea input;
    private final HashMap<String, ChatUI> panelIdToChat;
    private EmoticonPalettePanel emoticonPalettePanel;
    private PopupPanel emoticonPopup;
    private TabPanel centerPanel;
    private Panel usersPanel;
    private final I18nTranslationService i18n;
    private Panel rosterPanel;
    private BottomTrayIcon bottomIcon;
    private ToolbarButton emoticonButton;
    private MultiChatPanelTopBar topToolbar;
    private MultiChatPanelInfoTab infoPanel;
    private Panel roomUsersPanel;
    private Panel southPanel;
    private FormPanel inputForm;
    private FormPanel subjectForm;
    private final RosterUI rosterUI;
    private Panel eastPanel;
    private final String chatDialogTitle;

    public MultiChatPanel(final String chatDialogTitle, final RosterUI rosterUI, final I18nTranslationService i18n,
            final MultiChatPresenter presenter) {
        this.chatDialogTitle = chatDialogTitle;
        this.rosterUI = rosterUI;
        this.i18n = i18n;
        this.presenter = presenter;
        panelIdToChat = new HashMap<String, ChatUI>();
        createLayout();
        attachRoster();
    }

    public void addChat(final ChatUI chat) {
        Panel chatPanel = (Panel) chat.getView();
        centerPanel.add(chatPanel);
        String panelId = chatPanel.getId();
        panelIdToChat.put(panelId, chat);
        centerPanel.activate(chatPanel.getId());
        centerPanel.scrollToTab(chatPanel, true);
    }

    public void attachRoomUserList(final View userListView) {
        UserGridPanel panel = (UserGridPanel) userListView;
        roomUsersPanel.add(panel);
        // if (roomUsersPanel.isRendered()) {
        // roomUsersPanel.doLayout();
        // }
        if (usersPanel.isRendered()) {
            usersPanel.doLayout();
        }
        roomUsersPanel.expand();
    }

    public void center() {
        dialog.center();
    }

    public void clearInputText() {
        input.reset();
    }

    public void clearRoster() {
        rosterUI.clearRoster();
    }

    public void clearSubject() {
        subject.reset();
    }

    public void confirmCloseAll() {
        topToolbar.confirmCloseAll();
    }

    public void destroy() {
        dialog.destroy();
    }

    public void expandRoster() {
        rosterPanel.expand();
    }

    public void focusInput() {
        input.focus();
    }

    public String getInputText() {
        return input.getValueAsString();
    }

    public void hide() {
        dialog.hide();
    }

    public void highLight() {
        // TODO Auto-generated method stub
    }

    public void removeChat(final ChatUI chatUI) {
        centerPanel.remove(((Panel) chatUI.getView()).getId());
    }

    public void setAddRosterItemButtonVisible(final boolean visible) {
        topToolbar.setAddRosterItemButtonVisible(visible);
    }

    public void setCloseAllOptionEnabled(final boolean enabled) {
        topToolbar.setCloseAllOptionEnabled(enabled);
    }

    public void setEmoticonButtonEnabled(final boolean enabled) {
        emoticonButton.setDisabled(!enabled);
    }

    public void setInfoPanelVisible(final boolean visible) {
        if (visible) {
            centerPanel.add(infoPanel);
            infoPanel.show();
            centerPanel.activate(infoPanel.getId());
        } else {
            centerPanel.remove(infoPanel);
        }
    }

    public void setInputEditable(final boolean editable) {
        input.setDisabled(!editable);
    }

    public void setInputText(final String text) {
        input.setRawValue(text);
    }

    public void setInviteToGroupChatButtonVisible(final boolean visible) {
        topToolbar.setInviteToGroupChatButtonVisible(visible);
    }

    public void setJoinRoomEnabled(final boolean enabled) {
        topToolbar.setJoinRoomEnabled(enabled);
    }

    public void setLoadingVisible(final boolean visible) {
        topToolbar.setLoadingVisible(visible);
    }

    public void setOfflineInfo() {
        infoPanel.setOfflineInfo();
    }

    public void setOnlineInfo() {
        infoPanel.setOnlineInfo();
    }

    public void setOwnPresence(final OwnPresence ownPresence) {
        topToolbar.setOwnPresence(ownPresence);
    }

    public void setRoomUserListVisible(final boolean visible) {
        roomUsersPanel.setVisible(visible);
        if (visible == true) {
            usersPanel.setActiveItemID(roomUsersPanel.getId());
            roomUsersPanel.expand();
        }
    }

    public void setRosterVisible(final boolean visible) {
        rosterPanel.setVisible(visible);
        if (visible) {
            if (usersPanel.isRendered()) {
                usersPanel.doLayout();
            }
            // rosterPanel.doLayout();
            rosterPanel.expand();
        }
    }

    public void setSendEnabled(final boolean enabled) {
        if (enabled) {
            sendBtn.enable();
        } else {
            sendBtn.disable();
        }
    }

    public void setSubject(final String text) {
        subject.setRawValue(text);
    }

    public void setSubjectEditable(final boolean editable) {
        subject.setDisabled(!editable);
        if (editable) {
            if (getSubject() == null || getSubject().equals("")) {
                // if works put this in presenter
                subject.setEmptyText(i18n.t("Write here a subject or topic for this room"));
                if (subjectForm.isRendered()) {
                    subjectForm.doLayout();
                }
            }
        }
    }

    public void setSubjectVisible(final boolean visible) {
        subject.setVisible(visible);
    }

    public void show() {
        dialog.show();
        dialog.expand();
    }

    public void showAlert(final String message) {
        MessageBox.alert(i18n.t("Alert"), message);
    }

    public void unHighLight() {
        // TODO Auto-generated method stub
    }

    private void attachRoster() {
        rosterPanel.add((Panel) rosterUI.getView());
        if (usersPanel.isRendered()) {
            usersPanel.doLayout();
        }
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

    private Panel createInputPanel() {
        inputForm = createGenericInputForm();
        input = new TextArea();
        input.setHeight(47);
        input.setEnterIsSpecial(true);
        input.addListener(new FieldListenerAdapter() {
            public void onSpecialKey(final Field field, final EventObject e) {
                if (e.getKey() == 13) {
                    doSend(e);
                    e.stopEvent();
                }
            }
        });
        inputForm.add(input);

        /* Input toolbar */
        final Toolbar inputToolbar = new Toolbar();
        emoticonButton = new ToolbarButton();
        emoticonButton.setIcon("images/smile.gif");
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

    private void createKuneIconBottomBar() {
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

    private void createLayout() {
        dialog = new BasicDialog(chatDialogTitle, false, false, 600, 415, 300, 300);
        dialog.setBorder(false);
        dialog.setCollapsible(true);
        dialog.setIconCls("chat-icon");
        sendBtn = new Button(i18n.tWithNT("Send", "used in button"));
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

        southPanel = new Panel();
        southPanel.setHeight(75);
        southPanel.add(createInputPanel());
        southPanel.setBorder(false);
        BorderLayoutData southData = new BorderLayoutData(RegionPosition.SOUTH);
        southData.setSplit(true);
        dialog.add(southPanel, southData);

        eastPanel = new Panel(i18n.t("Users"));
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
        infoPanel = new MultiChatPanelInfoTab(i18n);
        centerPanel.add(infoPanel);
        dialog.add(centerPanel, centerData);

        createListeners();

        createKuneIconBottomBar();
    }

    private void createListeners() {
        dialog.addListener(new WindowListenerAdapter() {
            public void onShow(final Component component) {
                focusInput();
            }
        });

        dialog.addListener(new ContainerListenerAdapter() {
            public void onResize(final BoxComponent component, final int adjWidth, final int adjHeight,
                    final int rawWidth, final int rawHeight) {
                int newWidth = adjWidth - 14;
                input.setWidth(newWidth);
                subject.setWidth(newWidth);
                inputForm.setWidth(newWidth);
                subjectForm.setWidth(newWidth);
            }
        });

        southPanel.addListener(new ContainerListenerAdapter() {
            public void onResize(final BoxComponent component, final int adjWidth, final int adjHeight,
                    final int rawWidth, final int rawHeight) {
                input.setHeight(adjHeight - 27);
                inputForm.setHeight(adjHeight);
            }
        });

        eastPanel.addListener(new ContainerListenerAdapter() {
            public void onResize(final BoxComponent component, final int adjWidth, final int adjHeight,
                    final int rawWidth, final int rawHeight) {
                // FIXME
            }
        });

        centerPanel.addListener(new PanelListenerAdapter() {
            public boolean doBeforeRemove(final Container self, final Component component) {
                final String panelId = component.getId();
                final ChatUI chatUI = panelIdToChat.get(panelId);
                if (component.getId().equals(infoPanel.getId())) {
                    // Closing empty chats info
                    return true;
                }
                if (chatUI.getCloseConfirmed()) {
                    panelIdToChat.remove(panelId);
                    return true;
                } else {
                    MessageBox.confirm(i18n.t("Confirm"), i18n.t("Are you sure you want to exit from this chat?"),
                            new MessageBox.ConfirmCallback() {
                                public void execute(final String btnID) {
                                    if (btnID.equals("yes")) {
                                        presenter.closeChatUI(chatUI);
                                        self.remove(panelId);
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

    private Panel createSubjectPanel() {
        subjectForm = createGenericInputForm();

        // i18n
        subject = new TextField();
        // subject.setTitle(i18n.t("Subject of this room"));
        subject.setHeight(27);
        subject.addListener(new FieldListenerAdapter() {
            public void onSpecialKey(final Field field, final EventObject e) {
                // FIXME dont works?
                Log.info("Subject special key");
                if (e.getKey() == 13) {
                    Log.info("Subject enter fired");
                    presenter.onModifySubjectRequested(getSubject());
                    e.stopEvent();
                }
            }
        });

        // changeSubject = new Button(i18n.t("Change asdfasfasdfas"));
        // changeSubject.setTitle(i18n.tWithNT("Change the subject of this
        // room", "used in button"));
        // changeSubject.addListener(new ButtonListenerAdapter() {
        // public void onClick(final Button button, final EventObject e) {
        // presenter.onModifySubjectRequested(getSubject());
        // e.stopEvent();
        // }
        // });
        subjectForm.add(subject);
        topToolbar = new MultiChatPanelTopBar(i18n, presenter);
        Panel northPanel = createInputFormWithToolBar(subjectForm, topToolbar);
        return northPanel;
    }

    private Panel createUsersPanel() {
        usersPanel = new Panel();
        usersPanel.setLayout(new AccordionLayout(true));
        usersPanel.setAutoScroll(false);
        usersPanel.setBorder(false);
        rosterPanel = new Panel(i18n.t("My buddies"));
        rosterPanel.setLayout(new FitLayout());
        rosterPanel.setAutoScroll(true);
        rosterPanel.setIconCls("userf-icon");
        // rosterPanel.setAutoHeight(true);
        rosterPanel.setBorder(false);
        roomUsersPanel = new Panel(i18n.t("Now in this room"));
        roomUsersPanel.setLayout(new FitLayout());
        roomUsersPanel.setAutoScroll(true);
        roomUsersPanel.setIconCls("group-icon");
        // roomUsersPanel.setAutoHeight(true);
        usersPanel.add(rosterPanel);
        usersPanel.add(roomUsersPanel);

        // usersPanel.addListener(new ContainerListenerAdapter() {
        // public void onResize(final BoxComponent component, final int
        // adjWidth, final int adjHeight,
        // final int rawWidth, final int rawHeight) {
        // resizeUserPanelElements(rawWidth, rawHeight);
        // }
        // });
        return usersPanel;
    }

    private void doSend(final EventObject e) {
        presenter.onCurrentUserSend(getInputText());
        e.stopEvent();
        input.focus();
    }

    private String getSubject() {
        return subject.getValueAsString();
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
}
