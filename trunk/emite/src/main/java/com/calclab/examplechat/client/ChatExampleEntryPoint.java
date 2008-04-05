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

import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.extend.PluginManager;
import org.ourproject.kune.platf.client.extend.UIExtensionPointManager;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.EmiteUiPlugin;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
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
    private Button btnLogin;
    private Button btnLogout;
    private DefaultDispatcher dispatcher;
    private PasswordTextBox passwordInput;
    private Presence presenceForTest;
    private TextBox userNameInput;
    private Xmpp xmpp;

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

        Log.getDivLogger().moveTo(10, 60);
        Log.getDivLogger().setSize("1200", "400");

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

        this.xmpp = Xmpp.create(new BoshOptions("http-bind", "localhost"));

        createPresenceForTest();
        createInterface();
        createExtUI();
    }

    private HorizontalPanel createButtonsPane() {
        final HorizontalPanel buttons = new HorizontalPanel();
        btnLogin = new Button("Login", new ClickListener() {
            public void onClick(final Widget source) {
                login();
            }
        });
        buttons.add(btnLogin);
        btnLogout = new Button("Logout", new ClickListener() {
            public void onClick(final Widget arg0) {
                logout();
            }
        });
        buttons.add(btnLogout);

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

        dispatcher.fire(EmiteUiPlugin.OPEN_MULTI_CHAT_DIALOG, new MultiChatCreationParam(xmpp, new PairChatUser(
                "images/person-def.gif", XmppURI.parse(userNameInput.getText()), userNameInput.getText(),
                MultiChatView.DEF_USER_COLOR, presenceForTest), passwordInput.getText()));
        dispatcher.subscribe(EmiteUiPlugin.ON_STATE_CONNECTED, new Action<Object>() {
            public void execute(final Object param) {
                btnLogin.setEnabled(false);
                btnLogout.setEnabled(true);
            }
        });

        dispatcher.subscribe(EmiteUiPlugin.ON_STATE_DISCONNECTED, new Action<Object>() {
            public void execute(final Object param) {
                btnLogin.setEnabled(true);
                btnLogout.setEnabled(false);
            }
        });
    }

    private void createInterface() {
        final VerticalPanel vertical = new VerticalPanel();
        vertical.add(createButtonsPane());
        vertical.add(createLoginPane());
        vertical.add(createOutputPane());

        RootPanel.get().add(vertical);
    }

    private HorizontalPanel createLoginPane() {
        final HorizontalPanel login = new HorizontalPanel();
        userNameInput = new TextBox();
        userNameInput.setText("admin@localhost");
        passwordInput = new PasswordTextBox();
        passwordInput.setText("easyeasy");
        login.add(new Label("user name:"));
        login.add(userNameInput);
        login.add(new Label("Resource:"));
        login.add(new Label("password"));
        login.add(passwordInput);
        return login;
    }

    private HorizontalPanel createOutputPane() {
        final HorizontalPanel split = new HorizontalPanel();
        return split;
    }

    private void createPresenceForTest() {
        presenceForTest = new Presence();
        presenceForTest.setShow(Presence.Show.available);
        presenceForTest.setType(Presence.Type.available.toString());
        presenceForTest.setStatus("I\'m out for dinner");
    }

    private void login() {
        xmpp.login(userNameInput.getText(), passwordInput.getText());
    }

    private void logout() {
        xmpp.logout();
        btnLogout.setEnabled(false);
        btnLogin.setEnabled(true);
        dispatcher.fire(EmiteUiPlugin.SET_STATUS, MultiChatView.STATUS_OFFLINE);
    }

    private void panic() {
        xmpp.getDispatcher().publish(BoshManager.Events.error);
        btnLogin.setEnabled(true);
        btnLogout.setEnabled(true);

    }

}
