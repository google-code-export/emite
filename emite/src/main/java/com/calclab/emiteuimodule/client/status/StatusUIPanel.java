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
package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emiteuimodule.client.dialog.MessageDialog;
import com.calclab.emiteuimodule.client.status.OwnPresence.OwnStatus;
import com.calclab.emiteuimodule.client.utils.ChatUIUtils;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.ColorPalette;
import com.gwtext.client.widgets.MessageBox;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.ToolbarItem;
import com.gwtext.client.widgets.MessageBox.PromptCallback;
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

public class StatusUIPanel extends Toolbar implements StatusUIView {

    private final Menu statusMenu;
    private CheckItem onlineMenuItem;
    private CheckItem offlineMenuItem;
    private CheckItem busyCustomMenuItem;
    private CheckItem onlineCustomMenuItem;
    private CheckItem busyMenuItem;
    private final ToolbarButton statusButton;
    private final I18nTranslationService i18n;
    private final Item closeAllOption;
    private final ToolbarButton loading;
    private CheckItem manualSubsItem;
    private CheckItem autoRejectSubsItem;
    private CheckItem autoAcceptSubsItem;

    private final StatusUIPresenter presenter;
    private final Menu chatMenu;
    private Menu optionsSubMenu;
    private final String ownStatusFootText;
    private final String ownStatusHeadText;

    public StatusUIPanel(final StatusUIPresenter presenter, final I18nTranslationService i18n) {
	this.presenter = presenter;
	this.i18n = i18n;

	chatMenu = new Menu();
	chatMenu.setShadow(true);

	closeAllOption = createCloseAllMenuItem(i18n);

	final MenuItem optionsItem = new MenuItem(i18n.t("Options"), createOptionsMenu());
	chatMenu.addItem(closeAllOption);
	chatMenu.addItem(optionsItem);

	final ToolbarButton chatMenuButton = new ToolbarButton(i18n.t("Chat"));
	chatMenuButton.setMenu(chatMenu);
	this.addButton(chatMenuButton);
	this.addSeparator();

	statusMenu = createStatusMenu();
	statusButton = new ToolbarButton("Set status");
	statusButton.setMenu(statusMenu);
	statusButton.setTooltip(i18n.t("Set status"));
	this.addButton(statusButton);

	loading = new ToolbarButton();
	loading.setIcon("images/ext-load.gif");
	loading.setCls("x-btn-icon");
	this.addButton(loading);
	setLoadingVisible(false);

	this.addFill();
	ownStatusHeadText = "<b>";
	ownStatusFootText = "</b><br/>" + i18n.t("(Click to change your status)");
    }

    public void addButtonItem(final View item) {
	super.addButton((ToolbarButton) item);
    }

    public void addChatMenuItem(final View item) {
	chatMenu.addItem((BaseItem) item);
    }

    public void addToolbarItem(final View item) {
	super.addItem((ToolbarItem) item);
    }

    public void addOptionsSubMenuItem(final View item) {
	optionsSubMenu.addItem((BaseItem) item);
    }

    public void confirmCloseAll() {
	MessageBox.confirm(i18n.t("Confirm"), i18n.t("Are you sure you want to exit all the chats?"),
		new MessageBox.ConfirmCallback() {
		    public void execute(final String btnID) {
			if (btnID.equals("yes")) {
			    DeferredCommand.addCommand(new Command() {
				public void execute() {
				    presenter.onCloseAllConfirmed();
				}
			    });
			}
		    }
		});
    }

    public void setCloseAllOptionEnabled(boolean enabled) {
	closeAllOption.setDisabled(!enabled);
    }

    public void setLoadingVisible(final boolean visible) {
	loading.setVisible(visible);
    }

    public void setOwnPresence(final OwnPresence ownPresence) {
	switch (ownPresence.getStatus()) {
	case online:
	    onlineMenuItem.setChecked(true);
	    setOwnStatusText(ownStatusHeadText + i18n.t("Online") + ownStatusFootText);
	    break;
	case onlinecustom:
	    setOwnStatusText(ownStatusHeadText + ownPresence.getStatusText() + ownStatusFootText);
	    onlineCustomMenuItem.setChecked(true);
	    break;
	case offline:
	    setOwnStatusText(ownStatusHeadText + i18n.t("Offline") + ownStatusFootText);
	    offlineMenuItem.setChecked(true);
	    break;
	case busy:
	    setOwnStatusText(ownStatusHeadText + i18n.t("Don't disturb") + ownStatusFootText);
	    busyMenuItem.setChecked(true);
	    break;
	case busycustom:
	    setOwnStatusText(ownStatusHeadText + ownPresence.getStatusText() + ownStatusFootText);
	    busyCustomMenuItem.setChecked(true);
	    break;
	}

	final String icon = ChatUIUtils.getOwnStatusIcon(ownPresence.getStatus()).getHTML();
	statusButton.setText(icon);
    }

    public void setSubscriptionMode(final SubscriptionMode mode) {
	switch (mode) {
	case autoAcceptAll:
	    autoAcceptSubsItem.setChecked(true);
	    break;
	case autoRejectAll:
	    autoRejectSubsItem.setChecked(true);
	    break;
	default:
	    manualSubsItem.setChecked(true);
	    break;
	}
    }

    public void showAlert(final String message) {
	MessageDialog.alert(i18n.t("Alert"), message);
    }

    private Item createCloseAllMenuItem(final I18nTranslationService i18n) {
	final Item closeAllOption = new Item();
	closeAllOption.setText(i18n.t("Close all chats"));
	closeAllOption.setIconCls("exit-icon");
	closeAllOption.addListener(new BaseItemListenerAdapter() {
	    @Override
	    public void onClick(final BaseItem item, final EventObject e) {
		confirmCloseAll();
	    }
	});
	return closeAllOption;
    }

    private Menu createOptionsMenu() {
	optionsSubMenu = new Menu();
	final ColorMenu colorMenu = new ColorMenu();
	colorMenu.addListener(new ColorMenuListener() {
	    public void onSelect(final ColorPalette colorPalette, final String color) {
		DeferredCommand.addCommand(new Command() {
		    public void execute() {
			presenter.onUserColorChanged(color);
		    }
		});
	    }
	});

	final MenuItem colorMenuItem = new MenuItem(i18n.t("Choose your color"), colorMenu);
	colorMenuItem.setIconCls("colors-icon");
	colorMenuItem.setIcon("images/colors.gif");

	final MenuItem subsItem = new MenuItem(i18n.t("New buddies options"), createUserSubscriptionMenu());

	optionsSubMenu.addItem(colorMenuItem);
	optionsSubMenu.addItem(subsItem);

	return optionsSubMenu;
    }

    private CheckItem createStatusCheckItem(final OwnStatus ownStatus) {
	final CheckItem checkItem = new CheckItem();
	checkItem.setText(ChatUIUtils.getOwnStatusIconAndText(i18n, ownStatus));
	checkItem.setGroup("chatstatus");
	switch (ownStatus) {
	case offline:
	case online:
	case busy:
	    checkItem.addListener(new BaseItemListenerAdapter() {
		@Override
		public void onClick(final BaseItem item, final EventObject e) {
		    DeferredCommand.addCommand(new Command() {
			public void execute() {
			    presenter.setOwnPresence(new OwnPresence(ownStatus));
			}
		    });
		}
	    });
	    break;
	case busycustom:
	case onlinecustom:
	    checkItem.addListener(new BaseItemListenerAdapter() {
		@Override
		public void onClick(final BaseItem item, final EventObject e) {
		    MessageBox.prompt(i18n.t("Set your status message"), i18n
			    .t("Set your status text (something like 'Out for dinner' or 'Working')"),
			    new PromptCallback() {
				public void execute(final String btnID, final String text) {
				    DeferredCommand.addCommand(new Command() {
					public void execute() {
					    presenter.setOwnPresence(new OwnPresence(ownStatus, text));
					}
				    });
				}
			    });
		}
	    });
	    break;
	}
	return checkItem;
    }

    private Menu createStatusMenu() {
	final Menu statusMenu = new Menu();
	statusMenu.setShadow(true);
	statusMenu.addItem(new TextItem("<b style=\"margin: 5px 0px;\" class=\"menu-title\">"
		+ i18n.t("Change your status") + "</b>"));
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
	final CheckItemListenerAdapter itemListener = new CheckItemListenerAdapter() {
	    @Override
	    public void onCheckChange(CheckItem item, boolean checked) {
		if (checked) {
		    DeferredCommand.addCommand(new Command() {
			public void execute() {
			    presenter.onUserSubscriptionModeChanged(mode);
			}
		    });
		}
	    }
	};
	final CheckItem item = new CheckItem();
	item.setText(text);
	item.setGroup("subscription");
	item.addListener(itemListener);
	submenu.addItem(item);
	return item;
    }

    private Menu createUserSubscriptionMenu() {
	final Menu submenu = new Menu();
	submenu.setShadow(true);
	submenu.setMinWidth(10);
	autoAcceptSubsItem = createSubscritionItem(i18n
		.t("Automatically accept users as buddies when a user request it"), submenu,
		SubscriptionMode.autoAcceptAll);
	autoRejectSubsItem = createSubscritionItem(i18n.t("Automatically reject new buddies inclusion requests"),
		submenu, SubscriptionMode.autoRejectAll);
	manualSubsItem = createSubscritionItem(i18n.t("Manual accept or reject new buddies inclusion requests"),
		submenu, SubscriptionMode.manual);
	return submenu;
    }

    private void setOwnStatusText(final String text) {
	statusButton.setTooltip(text);
    }
}
