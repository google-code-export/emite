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
package com.calclab.examplechat.client;

import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.extend.PluginManager;
import org.ourproject.kune.platf.client.extend.UIExtensionPointManager;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.examplechat.client.chatuiplugin.EmiteUiPlugin;
import com.calclab.examplechat.client.chatuiplugin.UserChatOptions;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.params.MultiChatCreationParam;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ChatExampleEntryPoint implements EntryPoint {
    private DefaultDispatcher dispatcher;
    private PasswordTextBox passwordInput;
    private Presence presenceForTest;
    private TextBox userNameInput;

    public void onModuleLoad() {
        /*
         * Install an UncaughtExceptionHandler which will produce <code>FATAL</code>
         * log messages
         */

        /*
         * Currently we let firebug to catch the error:
         * Log.setUncaughtExceptionHandler();
         */

        // At the moment, in runtime:
        Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);

        // Log.getDivLogger().moveTo(10, 60);
        // Log.getDivLogger().setSize("1200", "400");

        /*
         * Use a deferred command so that the UncaughtExceptionHandler catches
         * any exceptions in onModuleLoadCont()
         */
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                onModuleLoadCont();
            }
        });
    }

    public void onModuleLoadCont() {
        createPresenceForTest();
        createInterface();
        createExtUI();
    }

    private HorizontalPanel createButtonsPane() {
        final HorizontalPanel buttons = new HorizontalPanel();

        final Button btnPanic = new Button("Panic!", new ClickListener() {
            public void onClick(final Widget arg0) {
                panic();
            }
        });
        buttons.add(btnPanic);

        return buttons;
    }

    private void createExtUI() {
        dispatcher = DefaultDispatcher.getInstance();
        final PluginManager kunePluginManager = new PluginManager(dispatcher, new UIExtensionPointManager(),
                new I18nTranslationServiceMocked());
        kunePluginManager.install(new EmiteUiPlugin());

        dispatcher.fire(EmiteUiPlugin.OPEN_MULTI_CHAT_DIALOG, new MultiChatCreationParam(new BoshOptions("http-bind",
                "localhost"), userNameInput.getText(), passwordInput.getText(), new UserChatOptions(
                MultiChatView.DEF_USER_COLOR, Roster.DEF_SUBSCRIPTION_MODE)));
    }

    private void createInterface() {
        final VerticalPanel vertical = new VerticalPanel();
        vertical.add(createUserNamePane());
        vertical.add(createPasswdPane());
        vertical.add(new Label("Currently we are only supporting PLAIN authentication, "
                + "them for your security, only use jabber test accounts."));
        vertical.add(createButtonsPane());

        RootPanel.get().add(vertical);
    }

    private HorizontalPanel createUserNamePane() {
        final HorizontalPanel userNamePanel = new HorizontalPanel();
        userNameInput = new TextBox();
        userNameInput.setText("admin@localhost");
        userNamePanel.add(new Label("user name: "));
        userNamePanel.add(userNameInput);
        return userNamePanel;
    }

    private HorizontalPanel createPasswdPane() {
        final HorizontalPanel passwdPanel = new HorizontalPanel();
        passwordInput = new PasswordTextBox();
        passwordInput.setText("easyeasy");
        passwdPanel.add(new Label("password: "));
        passwdPanel.add(passwordInput);
        return passwdPanel;
    }

    private void createPresenceForTest() {
        presenceForTest = new Presence();
        presenceForTest.setShow(Presence.Show.available);
        presenceForTest.setType(Presence.Type.available.toString());
        presenceForTest.setStatus("I\'m out for dinner");
    }

    private void panic() {
        dispatcher.fire(EmiteUiPlugin.ON_PANIC, null);
    }

}
