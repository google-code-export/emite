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
package com.calclab.emiteui.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.modular.ModuleContainer;
import com.calclab.emiteui.client.demo.DemoModule;
import com.calclab.emiteui.client.demo.EmiteDemoUI;
import com.calclab.emiteui.client.demo.LoginPanel;
import com.calclab.emiteui.client.demo.LoginPanel.LoginPanelListener;
import com.calclab.emiteuiplugin.client.EmiteDialog;
import com.calclab.emiteuiplugin.client.UserChatOptions;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence.OwnStatus;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class EmiteUIEntryPoint implements EntryPoint {

    public void onModuleLoad() {
	Log.setUncaughtExceptionHandler();
	DeferredCommand.addCommand(new Command() {
	    public void execute() {
		onModuleLoadCont();
	    }
	});
    }

    public void onModuleLoadCont() {
	final ModuleContainer container = new ModuleContainer();
	container.load(new EmiteUIModule(), new DemoModule());

	final EmiteDemoUI demo = container.getInstance(EmiteDemoUI.class);
	final EmiteDialog emiteDialog = container.getInstance(EmiteDialog.class);

	final LoginPanel loginPanel = demo.createLoginPanel(new LoginPanelListener() {
	    public void onUserChanged(final UserChatOptions userChatOptions) {
		emiteDialog.refreshUserInfo(userChatOptions);
	    }
	});

	demo.createShowHideButton();
	demo.createInfoPanel();

	final DemoParameters params = container.getInstance(DemoParameters.class);
	emiteDialog.start(loginPanel.getUserChatOptions(), params.getHttpBase(), params.getRoomHost());
	emiteDialog.show(OwnStatus.offline);
    }

}
