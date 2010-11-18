/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuimodule.client.dialog;

import java.util.HashMap;

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.chat.ChatNotification;
import com.calclab.emiteuimodule.client.chat.ChatUI;
import com.calclab.emiteuimodule.client.roster.RosterItemPanel;
import com.calclab.emiteuimodule.client.roster.RosterUIPanel;
import com.calclab.emiteuimodule.client.status.OwnPresence;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.emiteuimodule.client.users.DropGridConfiguration;
import com.calclab.emiteuimodule.client.users.UserGridDropListener;
import com.calclab.emiteuimodule.client.users.UserGridPanel;
import com.calclab.emiteuimodule.client.utils.emoticons.EmoticonPaletteListener;
import com.calclab.emiteuimodule.client.utils.emoticons.EmoticonPalettePanel;
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
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;
import com.gwtext.client.widgets.layout.FormLayout;

public class MultiChatPanel {

    private static final int TIMEVISIBLE = 3000;
    private static final String INPUT_ID = "MultiChatPanel-InputArea";
    private static final String EMOTICON_BUTTON_ID = "MultiChatPanel-emoticonButton";
    private final RosterUIPanel rosterUIPanel;
    private Window dialog;
    private final MultiChatPresenter presenter;
    private TextArea input;
    private final HashMap<String, ChatUI> panelIdToChat;
    private EmoticonPalettePanel emoticonPalettePanel;
    private TabPanel centerPanel;
    private final I18nTranslationService i18n;
    private ToolbarButton emoticonButton;
    private final StatusUI statusUI;
    private MultiChatPanelInfoTab infoPanel;
    private Panel southPanel;
    private FormPanel inputForm;
    private Panel eastPanel;
    private final String chatDialogTitle;
    private ToolbarButton addRosterItem;
    private Label bottomInfoMessage;
    private Timer bottomInfoTimer;
    private BasicDialog emoticonDialog;
    private ToolbarButton showUnavailableItems;
    private Label bottomChatNotification;
    private boolean inputFocused;

    public MultiChatPanel(final String chatDialogTitle, final RosterUIPanel rosterUIPanel, final StatusUI statusUI,
	    final I18nTranslationService i18n, final MultiChatPresenter presenter) {
	this.statusUI = statusUI;
	this.chatDialogTitle = chatDialogTitle;
	this.rosterUIPanel = rosterUIPanel;
	this.i18n = i18n;
	this.presenter = presenter;
	panelIdToChat = new HashMap<String, ChatUI>();
	this.inputFocused = false;
	createLayout(statusUI);
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

    public void collapse() {
	dialog.collapse();
    }

    public void destroy() {
	dialog.destroy();
    }

    public void expand() {
	dialog.expand();
    }

    public void focusInput() {
	new Timer() {
	    @Override
	    public void run() {
		input.focus();
	    }
	}.schedule(50);
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
	bottomInfoMessage.setText(message);
	bottomInfoMessage.setVisible(true);
	renderSouthPanelIfNeeded();
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
	statusUI.setOwnPresence(ownPresence);
    }

    public void setRosterVisible(final boolean visible) {
	rosterUIPanel.setVisible(visible);
	if (!dialog.isCollapsed()) {
	    if (visible) {
		eastPanel.expand();
	    } else {
		eastPanel.collapse();
	    }
	    if (eastPanel.isRendered()) {
		eastPanel.doLayout(false);
	    }
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
    }

    public void showAlert(final String message) {
	MessageDialog.alert(i18n.t("Alert"), message);
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
	rosterUIPanel.confDropInPanel(centerPanel, new DropGridConfiguration(UserGridPanel.USER_GROUP_DD,
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

    private FormPanel createInputPanel() {
	inputForm = new FormPanel();
	inputForm.setLayout(new FormLayout());
	inputForm.setHideLabels(true);
	inputForm.setBorder(false);
	input = new TextArea("Input", "input");
	input.setHeight(47);
	input.setEnterIsSpecial(true);

	final EventCallback inputKeyPressListener = new EventCallback() {
	    public void execute(final EventObject e) {
		DeferredCommand.addCommand(new Command() {
		    public void execute() {
			presenter.onComposing();
		    }
		});
	    }
	};
	input.addKeyPressListener(inputKeyPressListener);
	final FieldListenerAdapter inputMainListener = new FieldListenerAdapter() {
	    private final Timer stillFocusedTimer = new Timer() {
		@Override
		public void run() {
		    if (inputFocused) {
			presenter.onInputFocus();
		    }
		}
	    };

	    private final Timer stillUnfocusedTimer = new Timer() {
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
		    doSend(e);
		}
	    }
	};
	input.addListener(inputMainListener);
	input.setId(INPUT_ID);
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
	emoticonButton.setId(EMOTICON_BUTTON_ID);
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

    private void createLayout(final StatusUI statusUI) {
	dialog = new BasicDialog(chatDialogTitle, false, false, 470, 315, 200, 200);
	dialog.setButtonAlign(Position.LEFT);
	dialog.setBorder(false);
	dialog.setCollapsible(false);
	dialog.setMinimizable(true);
	dialog.setIconCls("e-icon");
	dialog.setLayout(new BorderLayout());

	final Panel northPanel = new Panel();
	// northPanel.setHeight(27);
	northPanel.setTopToolbar((Toolbar) statusUI.getView());
	northPanel.setBorder(false);
	final BorderLayoutData northData = new BorderLayoutData(RegionPosition.NORTH);
	dialog.add(northPanel, northData);

	southPanel = new Panel();
	southPanel.setTopToolbar(createInputToolBar());
	southPanel.add(createInputPanel());
	southPanel.setHeight(75);
	southPanel.setBorder(false);
	final BorderLayoutData southData = new BorderLayoutData(RegionPosition.SOUTH);
	southData.setSplit(true);
	dialog.add(southPanel, southData);

	eastPanel = new Panel(i18n.t("My buddies"));
	eastPanel.setLayout(new FitLayout());
	eastPanel.setBorder(true);
	eastPanel.setAutoScroll(true);
	eastPanel.setCollapsible(true);
	eastPanel.setWidth(UserGridPanel.DEFAULT_INITIAL_WIDTH);
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
	eastPanel.add(rosterUIPanel);
	final BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
	// This set the min and max width of the east panel (roster panel) when
	// resizing
	eastData.setMinSize(100);
	eastData.setMaxSize(250);
	eastData.setSplit(true);
	dialog.add(eastPanel, eastData);

	centerPanel = new TabPanel() {
	    {
		setAttribute("enableDragDrop", true, true);
	    }
	};
	centerPanel.setHeader(true);
	centerPanel.setBorder(false);
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
	    public void onMaximize(final Window source) {
		Log.info("onMax");
	    }

	    @Override
	    public void onMinimize(final Window source) {
		dialog.hide();
	    }

	    @Override
	    public void onMove(final BoxComponent component, final int x, final int y) {
		checkPosition(component, x, y);
	    }

	    @Override
	    public void onShow(final Component component) {
		focusInput();
		checkPosition(dialog, component.getAbsoluteLeft(), component.getAbsoluteTop());
	    }

	    private void checkPosition(final BoxComponent component, final int x, final int y) {
		if (y < 0) {
		    component.setPagePosition(x, 0);
		}
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

	eastPanel.addListener(new PanelListenerAdapter() {
	    @Override
	    public void onExpand(final Panel panel) {
		// Log.debug("Expand roster");
		if (eastPanel.isRendered()) {
		    eastPanel.doLayout(false);
		}
	    }
	});

	eastPanel.addListener(new ContainerListenerAdapter() {
	    @Override
	    public void onResize(final BoxComponent component, final int adjWidth, final int adjHeight,
		    final int rawWidth, final int rawHeight) {
		// Log.debug("Resize roster");
		rosterUIPanel.setWidth(adjWidth);
		if (eastPanel.isRendered() && rosterUIPanel.isRendered()) {
		    eastPanel.doLayout(false);
		}
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

    private void doSend(final EventObject e) {
	final String inputText = getInputText();
	e.stopEvent();
	presenter.onCurrentUserSend(inputText);
    }

    private void ifRenderedDoLayout() {
	if (dialog.isRendered()) {
	    dialog.doLayout();
	}
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
