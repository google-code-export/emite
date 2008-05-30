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

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.calclab.emite.client.core.packet.TextUtils;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.chat.ChatNotification;
import com.calclab.emiteuiplugin.client.chat.ChatUI;
import com.calclab.emiteuiplugin.client.roster.RosterItemPanel;
import com.calclab.emiteuiplugin.client.roster.RosterPanel;
import com.calclab.emiteuiplugin.client.status.OwnPresence;
import com.calclab.emiteuiplugin.client.status.StatusPanel;
import com.calclab.emiteuiplugin.client.users.DropGridConfiguration;
import com.calclab.emiteuiplugin.client.users.UserGridDropListener;
import com.calclab.emiteuiplugin.client.users.UserGridPanel;
import com.calclab.emiteuiplugin.client.utils.emoticons.EmoticonPaletteListener;
import com.calclab.emiteuiplugin.client.utils.emoticons.EmoticonPalettePanel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.core.EventCallback;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.BoxComponent;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.Component;
import com.gwtext.client.widgets.Container;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.QuickTip;
import com.gwtext.client.widgets.QuickTips;
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
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.FormLayout;

public class MultiChatPanel {

    private static final int TIMEVISIBLE = 3000;

    private final RosterPanel rosterPanel;
    private Window dialog;
    private Button sendBtn;
    private final MultiChatPresenter presenter;
    private TextArea input;
    private final HashMap<String, ChatUI> panelIdToChat;
    private EmoticonPalettePanel emoticonPalettePanel;
    private TabPanel centerPanel;
    private final I18nTranslationService i18n;
    private ToolbarButton emoticonButton;
    private final StatusPanel statusPanel;
    private MultiChatPanelInfoTab infoPanel;
    private Panel southPanel;
    private FormPanel inputForm;
    private Panel eastPanel;
    private final String chatDialogTitle;
    private ToolbarButton addRosterItem;
    private SoundController soundController;
    private Sound sound;
    private Label bottomInfoMessage;
    private Timer bottomInfoTimer;
    private BasicDialog emoticonDialog;
    private ToolbarButton showUnavailableItems;
    private Label bottomChatNotification;
    private boolean inputFocused;

    public MultiChatPanel(final String chatDialogTitle, final RosterPanel rosterPanel, final StatusPanel statusPanel,
            final I18nTranslationService i18n, final MultiChatPresenter presenter) {
        this.statusPanel = statusPanel;
        quickTipsInit();
        this.chatDialogTitle = chatDialogTitle;
        this.rosterPanel = rosterPanel;
        this.i18n = i18n;
        this.presenter = presenter;
        panelIdToChat = new HashMap<String, ChatUI>();
        this.inputFocused = false;
        createLayout(statusPanel);
        configureSound();
        configureBottomInfoTimer();
    }

    public void addChat(final ChatUI chat) {
        final Panel chatPanel = (Panel) chat.getView();
        centerPanel.add(chatPanel);
        final String panelId = chatPanel.getId();
        panelIdToChat.put(panelId, chat);
        centerPanel.activate(chatPanel.getId());
        centerPanel.scrollToTab(chatPanel, true);
    }

    public void center() {
        dialog.center();
    }

    public void clearBottomChatNotification() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                bottomChatNotification.setText("");
                bottomChatNotification.setVisible(false);
                renderSouthPanelIfNeeded();
            }
        });
    }

    public void clearBottomInfoMessage() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                bottomInfoMessage.setText("");
                bottomInfoMessage.setVisible(false);
                renderSouthPanelIfNeeded();
            }
        });
    }

    public void clearInputText() {
        if (dialog.isRendered()) {
            // (gwt-ext bug) if not redered the input listener are removed
            input.reset();
        }
    }

    public void click() {
        // sound.setVolume(50);
        sound.play();
    }

    public void confirmCloseAll() {
        statusPanel.confirmCloseAll();
    }

    public void destroy() {
        dialog.destroy();
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
        dialog.setIconCls("e-icon-a");
        renderDialogIfNeeded();
        click();
    }

    public boolean isEmoticonDialogVisible() {
        if (emoticonDialog == null) {
            return false;
        }
        return emoticonDialog.isVisible();
    }

    public boolean isVisible() {
        return dialog.isVisible();
    }

    public void removeChat(final ChatUI chatUI) {
        centerPanel.remove(((Panel) chatUI.getView()).getId());
    }

    public void roomJoinConfirm(final XmppURI invitor, final XmppURI roomURI, final String reason) {
        MessageBox.confirm(i18n.t("Join to chat room [%s]?", roomURI.getJID().toString()), i18n.t(
                "[%s] are inviting you to join this room: ", invitor.getJID().toString())
                + TextUtils.escape(reason), new MessageBox.ConfirmCallback() {
            public void execute(final String btnID) {
                if (btnID.equals("yes")) {
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            presenter.joinRoom(roomURI.getNode(), roomURI.getHost());
                        }
                    });
                }
            }
        });
    }

    public void setAddRosterItemButtonVisible(final boolean visible) {
        addRosterItem.setVisible(visible);
    }

    public void setBottomChatNotification(final ChatNotification chatNotification) {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                bottomChatNotification.setText(chatNotification.getNotification());
                bottomChatNotification.setStyleName(chatNotification.getStyle());
                bottomChatNotification.setVisible(true);
                renderSouthPanelIfNeeded();
            }
        });
    }

    public void setBottomInfoMessage(final String message) {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                bottomInfoMessage.setText(message);
                bottomInfoMessage.setVisible(true);
                renderSouthPanelIfNeeded();
            }
        });
    }

    public void setCloseAllOptionEnabled(final boolean enabled) {
        statusPanel.setCloseAllOptionEnabled(enabled);
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

    public void setJoinRoomEnabled(final boolean enabled) {
        statusPanel.setJoinRoomEnabled(enabled);
    }

    public void setLoadingVisible(final boolean visible) {
        statusPanel.setLoadingVisible(visible);
    }

    public void setOfflineInfo() {
        final String info = i18n.t("To start a chat you need to be 'online'.");
        infoPanel.setText(info);
    }

    public void setOfflineTitle() {
        dialog.setTitle(chatDialogTitle);
        ifRenderedDoLayout();
    }

    public void setOnlineInfo() {
        final String info = i18n.t("To start a chat, select a buddy or join to a chat room. "
                + "If you don't have buddies you can add them. ");
        infoPanel.setText(info);
    }

    public void setOwnPresence(final OwnPresence ownPresence) {
        statusPanel.setOwnPresence(ownPresence);
    }

    public void setRosterVisible(final boolean visible) {
        rosterPanel.setVisible(visible);
        if (visible) {
            eastPanel.expand();
        } else {
            eastPanel.collapse();
        }
        if (eastPanel.isRendered()) {
            eastPanel.doLayout(false);
        }
    }

    public void setSendEnabled(final boolean enabled) {
        if (enabled) {
            sendBtn.enable();
        } else {
            sendBtn.disable();
        }
    }

    public void setShowUnavailableItemsButtonPressed(final boolean pressed) {
        showUnavailableItems.setPressed(pressed);
    }

    public void setShowUnavailableItemsButtonVisible(final boolean visible) {
        showUnavailableItems.setVisible(visible);
    }

    public void setTitleConectedAs(final XmppURI currentUserJid) {
        // If we do this with a deferred command, east/roster panel start to
        // give collapse/expand problems ... a gwt-ext "extravaganza"
        // (issue #89)
        dialog.setTitle(chatDialogTitle + " (" + currentUserJid + ")");
        ifRenderedDoLayout();
    }

    public void show() {
        dialog.show();
        dialog.expand();
    }

    public void showAlert(final String message) {
        MessageBox.alert(i18n.t("Alert"), message);
    }

    public void unHighLight() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                dialog.setIconCls("e-icon");
                renderDialogIfNeeded();
            }
        });
    }

    private void configureBottomInfoTimer() {
        bottomInfoTimer = new Timer() {
            @Override
            public void run() {
                setBottomInfoMessage("");
            }
        };
    }

    private void configureDrop() {
        rosterPanel.confDropInPanel(centerPanel, new DropGridConfiguration(UserGridPanel.USER_GROUP_DD,
                new UserGridDropListener() {
                    public void onDrop(final XmppURI userURI) {
                        DeferredCommand.addCommand(new Command() {
                            public void execute() {
                                presenter.onUserDropped(userURI);
                            }
                        });
                    }
                }));
    }

    private void configureSound() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                soundController = new SoundController();
                // soundController.setPrioritizeFlashSound(false);
                // soundController.setDefaultVolume(0);
                sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_X_WAV, "click.wav");
            }
        });
    }

    private FormPanel createInputPanel() {
        inputForm = new FormPanel();
        inputForm.setLayout(new FormLayout());
        inputForm.setHideLabels(true);
        inputForm.setBorder(false);
        input = new TextArea("Input", "input");
        input.setHeight(47);
        input.setEnterIsSpecial(true);

        EventCallback inputKeyPressListener = new EventCallback() {
            public void execute(final EventObject e) {
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        presenter.onComposing();
                    }
                });
            }
        };
        input.addKeyPressListener(inputKeyPressListener);
        FieldListenerAdapter inputMainListener = new FieldListenerAdapter() {
            private Timer stillFocusedTimer = new Timer() {
                @Override
                public void run() {
                    if (inputFocused) {
                        presenter.onInputFocus();
                    }
                }
            };

            private Timer stillUnfocusedTimer = new Timer() {
                @Override
                public void run() {
                    if (!inputFocused) {
                        presenter.onInputUnFocus();
                    }
                }
            };

            @Override
            public void onBlur(final Field field) {
                inputFocused = false;
                stillFocusedTimer.cancel();
                stillUnfocusedTimer.schedule(1000);
            }

            @Override
            public void onFocus(final Field field) {
                inputFocused = true;
                stillUnfocusedTimer.cancel();
                stillFocusedTimer.schedule(1000);
            }

            @Override
            public void onSpecialKey(final Field field, final EventObject e) {
                if (e.getKey() == 13) {
                    doSendWithEnter(e);
                }
            }
        };
        input.addListener(inputMainListener);
        inputForm.add(input);
        return inputForm;
    }

    private Toolbar createInputToolBar() {
        final Toolbar inputToolbar = new Toolbar();
        emoticonButton = new ToolbarButton();
        emoticonButton.setIcon("images/smile.gif");
        emoticonButton.setCls("x-btn-icon x-btn-focus");
        emoticonButton.setTooltip(i18n.t("Insert a emoticon"));
        emoticonButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(final Button button, final EventObject e) {
                showEmoticonPalette(e.getXY()[0], e.getXY()[1]);
            }
        });
        inputToolbar.addButton(emoticonButton);
        inputToolbar.addSeparator();
        bottomChatNotification = new Label();
        inputToolbar.addElement(bottomChatNotification.getElement());
        inputToolbar.addFill();
        bottomInfoMessage = new Label();
        bottomInfoMessage.setStyleName("emite-bottom-message");
        inputToolbar.addElement(bottomInfoMessage.getElement());
        bottomInfoMessage.setVisible(false);
        return inputToolbar;
    }

    private void createLayout(final StatusPanel statusPanel) {
        dialog = new BasicDialog(chatDialogTitle, false, false, 600, 415, 300, 300);
        dialog.setButtonAlign(Position.LEFT);
        dialog.setBorder(false);
        dialog.setCollapsible(true);
        dialog.setIconCls("e-icon");
        sendBtn = new Button(i18n.tWithNT("Send", "used in button"));
        sendBtn.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(final Button button, final EventObject e) {
                doSendWithButton(e);
            }
        });
        dialog.addButton(sendBtn);
        dialog.setLayout(new BorderLayout());

        final Panel northPanel = new Panel();
        // northPanel.setHeight(27);
        northPanel.setTopToolbar(statusPanel);
        northPanel.setBorder(false);
        final BorderLayoutData northData = new BorderLayoutData(RegionPosition.NORTH);
        dialog.add(northPanel, northData);

        southPanel = new Panel();
        southPanel.setHeight(75);
        southPanel.setTopToolbar(createInputToolBar());
        southPanel.add(createInputPanel());
        southPanel.setBorder(false);
        final BorderLayoutData southData = new BorderLayoutData(RegionPosition.SOUTH);
        southData.setSplit(true);
        dialog.add(southPanel, southData);

        eastPanel = new Panel(i18n.t("My buddies"));
        eastPanel.setLayout(new FitLayout());
        eastPanel.setBorder(true);
        eastPanel.setAutoScroll(true);
        eastPanel.setCollapsible(true);
        eastPanel.setWidth(150);
        eastPanel.setIconCls("userf-icon");
        addRosterItem = new ToolbarButton();
        addRosterItem.setIcon("images/user_add.gif");
        addRosterItem.setCls("x-btn-icon");
        addRosterItem.setTooltip(i18n.t("Add a new buddy"));
        showUnavailableItems = new ToolbarButton();
        showUnavailableItems.setEnableToggle(true);
        showUnavailableItems.setPressed(false);
        showUnavailableItems.setIcon("images/user-unavail.gif");
        showUnavailableItems.setCls("x-btn-icon");
        showUnavailableItems.setTooltip(i18n.t("Show/hide unavailable buddies"));
        final Toolbar bottomToolbar = new Toolbar();
        bottomToolbar.addButton(addRosterItem);
        bottomToolbar.addSeparator();
        bottomToolbar.addButton(showUnavailableItems);
        bottomToolbar.setHeight(26);
        eastPanel.setBottomToolbar(bottomToolbar);
        addRosterItem.addListener(new ButtonListenerAdapter() {
            private RosterItemPanel rosterItemPanel;

            @Override
            public void onClick(final Button button, final EventObject e) {
                if (rosterItemPanel == null) {
                    rosterItemPanel = new RosterItemPanel(i18n, presenter);
                }
                rosterItemPanel.show();
            }
        });
        showUnavailableItems.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(final Button button, final EventObject e) {
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        presenter.showUnavailableRosterItems(button.isPressed());
                    }
                });
            }
        });
        eastPanel.add(rosterPanel);
        final BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
        // This set the min and max width of the east panel (roster panel) when
        // resizing
        eastData.setMinSize(100);
        eastData.setMaxSize(250);
        // When manual calc of size, set this to true
        eastData.setSplit(false);
        dialog.add(eastPanel, eastData);

        centerPanel = new TabPanel() {
            {
                setAttribute("enableDragDrop", true, true);
            }
        };
        centerPanel.setBorder(true);
        centerPanel.setEnableTabScroll(true);
        centerPanel.setAutoScroll(false);
        final BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
        infoPanel = new MultiChatPanelInfoTab(i18n.t("Info"));
        centerPanel.add(infoPanel);
        dialog.add(centerPanel, centerData);

        createListeners();

        configureDrop();
    }

    private void createListeners() {
        dialog.addListener(new WindowListenerAdapter() {
            @Override
            public void onShow(final Component component) {
                focusInput();
            }
        });

        dialog.addListener(new ContainerListenerAdapter() {
            @Override
            public void onResize(final BoxComponent component, final int adjWidth, final int adjHeight,
                    final int rawWidth, final int rawHeight) {
                final int newWidth = adjWidth - 14;
                input.setWidth(newWidth);
                inputForm.setWidth(newWidth);
            }
        });

        southPanel.addListener(new ContainerListenerAdapter() {
            @Override
            public void onResize(final BoxComponent component, final int adjWidth, final int adjHeight,
                    final int rawWidth, final int rawHeight) {
                input.setHeight(adjHeight - 27);
                inputForm.setHeight(adjHeight);
            }
        });

        eastPanel.addListener(new ContainerListenerAdapter() {
            @Override
            public void onResize(final BoxComponent component, final int adjWidth, final int adjHeight,
                    final int rawWidth, final int rawHeight) {
                // TODO
            }
        });

        centerPanel.addListener(new PanelListenerAdapter() {
            @Override
            public boolean doBeforeRemove(final Container self, final Component component) {
                final String panelId = component.getId();
                final ChatUI chatUI = panelIdToChat.get(panelId);
                if (component.getId().equals(infoPanel.getId())) {
                    // Closing empty chats info
                    return true;
                } else {
                    DeferredCommand.addCommand(new Command() {
                        public void execute() {
                            presenter.closeChatUI(chatUI);
                            panelIdToChat.remove(panelId);
                        }
                    });
                    return true;
                }
            }
        });
    }

    private void doSendWithButton(final EventObject e) {
        final String inputText = getInputText();
        e.stopEvent();
        presenter.onCurrentUserSendWithButton(inputText);
    }

    private void doSendWithEnter(final EventObject e) {
        final String inputText = getInputText();
        e.stopEvent();
        presenter.onCurrentUserSendWithEnter(inputText);
    }

    private void ifRenderedDoLayout() {
        if (dialog.isRendered()) {
            dialog.doLayout();
        }
    }

    private void quickTipsInit() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                if (!QuickTips.isEnabled()) {
                    // If not enabled before by another UI component
                    QuickTips.init();
                    final QuickTip quickTipInstance = QuickTips.getQuickTip();
                    quickTipInstance.setInterceptTitles(true);
                    quickTipInstance.setDismissDelay(7000);
                    quickTipInstance.setHideDelay(400);
                    quickTipInstance.setMinWidth(100);
                }
            }
        });
    }

    private void renderDialogIfNeeded() {
        if (dialog.isRendered()) {
            dialog.doLayout();
        }
    }

    private void renderSouthPanelIfNeeded() {
        if (southPanel.isRendered()) {
            southPanel.doLayout(false);
        }
        bottomInfoTimer.schedule(TIMEVISIBLE);
    }

    private void showEmoticonPalette(final int x, final int y) {
        if (emoticonPalettePanel == null) {
            emoticonPalettePanel = new EmoticonPalettePanel(new EmoticonPaletteListener() {
                public void onEmoticonSelected(final String emoticonText) {
                    setInputText(getInputText() + " " + emoticonText + " ");
                    emoticonDialog.hide();
                    input.focus();
                }
            });
            emoticonDialog = new BasicDialog(i18n.t("Select a emoticon"), false, false, 234, 192);
            emoticonDialog.add(emoticonPalettePanel);
        }
        emoticonDialog.show();
        emoticonDialog.setPosition(x - 10, y - 150);
    }

}
