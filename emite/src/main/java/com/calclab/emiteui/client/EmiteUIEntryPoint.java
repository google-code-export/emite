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
import com.calclab.emiteui.client.demo.DemoModule;
import com.calclab.emiteui.client.demo.EmiteDemoLoginPanel;
import com.calclab.emiteui.client.demo.EmiteDemoUI;
import com.calclab.emiteui.client.demo.EmiteDemoLoginPanel.LoginPanelListener;
import com.calclab.emiteui.client.demo.EmiteDemoUI.EmiteDemoChatIconListener;
import com.calclab.emiteuimodule.client.EmiteUIDialog;
import com.calclab.emiteuimodule.client.EmiteUIModule;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.emiteuimodule.client.status.OwnPresence.OwnStatus;
import com.calclab.modular.client.modules.ModuleBuilder;
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
        final ModuleBuilder app = new ModuleBuilder();
        app.add(new EmiteUIModule(), new DemoModule());

        final EmiteUIDialog emiteUIDialog = app.getInstance(EmiteUIDialog.class);

        final EmiteDemoUI demo = app.getInstance(EmiteDemoUI.class);

        final EmiteDemoLoginPanel emiteDemoLoginPanel = demo.createLoginPanel(new LoginPanelListener() {
            public void onOffline() {
                emiteUIDialog.show(OwnStatus.offline);
            }

            public void onOnline() {
                emiteUIDialog.show(OwnStatus.online);
            }

            public void onUserChanged(final UserChatOptions userChatOptions) {
                emiteUIDialog.refreshUserInfo(userChatOptions);
            }
        });

        demo.createChatIcon(new EmiteDemoChatIconListener() {

            public void onClick() {
                if (emiteUIDialog.isVisible()) {
                    emiteUIDialog.hide();
                } else {
                    emiteUIDialog.show();
                }
            }
        });
        demo.createInfoPanel();

        final DemoParameters params = app.getInstance(DemoParameters.class);
        emiteUIDialog.start(emiteDemoLoginPanel.getUserChatOptions(), params.getHttpBase(), params.getRoomHost());
        emiteUIDialog.show(OwnStatus.offline);
    }

}
