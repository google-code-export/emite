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

import org.ourproject.kune.platf.client.dispatch.Action;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.extend.PluginManager;
import org.ourproject.kune.platf.client.extend.UIExtensionPointManager;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emiteuiplugin.client.EmiteUIPlugin;
import com.calclab.emiteuiplugin.client.UserChatOptions;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.RootPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;

public class EmiteUIEntryPoint implements EntryPoint {
    private static final String GWT_PROPERTY_JID = "gwt_property_jid";
    private static final String GWT_PROPERTY_PASSWD = "gwt_property_passwd";
    private static final String GWT_PROPERTY_HTTPBASE = "gwt_property_httpbase";
    private static final String GWT_PROPERTY_ROOMHOST = "gwt_property_roomhost";
    private static final String GWT_PROPERTY_INFOHTML = "gwt_property_infohtml";
    private DefaultDispatcher dispatcher;
    private TextField passwd;
    private TextField jid;

    public void onModuleLoad() {
	/*
	 * Install an UncaughtExceptionHandler which will produce <code>FATAL</code>
	 * log messages
	 */

	Log.setUncaughtExceptionHandler();

	// At the moment, not in runtime (in html):
	// Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);

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
	createFormPanel();
	createInfoPanel();
	createExtUI();
    }

    private void createExtUI() {
	dispatcher = DefaultDispatcher.getInstance();
	final PluginManager kunePluginManager = new PluginManager(dispatcher, new UIExtensionPointManager(),
		new I18nTranslationServiceMocked());
	kunePluginManager.install(new EmiteUIPlugin());

	dispatcher.fire(EmiteUIPlugin.CREATE_CHAT_DIALOG, new MultiChatCreationParam("Emite Chat", new BoshOptions(
		getGwtMetaProperty(GWT_PROPERTY_HTTPBASE)), getGwtMetaProperty(GWT_PROPERTY_ROOMHOST),
		new I18nTranslationServiceMocked(), generateUserChatOptions()));
	dispatcher.fire(EmiteUIPlugin.SHOW_CHAT_DIALOG, null);

	dispatcher.subscribe(EmiteUIPlugin.ON_UNHIGHTLIGHTWINDOW, new Action<String>() {
	    public void execute(final String chatTitle) {
		// FIXME: js
		// Do something with window.parent.document.title
		// like remove (* chatTitle)
	    }
	});

	dispatcher.subscribe(EmiteUIPlugin.ON_HIGHTLIGHTWINDOW, new Action<String>() {
	    public void execute(final String chatTitle) {
		// FIXME: js
		// Do something with window.parent.document.title
		// like put (* chatTitle)
	    }
	});
    }

    private void createFormPanel() {
	final Panel panel = new Panel();
	panel.setBorder(false);
	panel.setPaddings(15);

	final FormPanel formPanel = new FormPanel();
	formPanel.setFrame(true);
	formPanel.setTitle("Some external Login Form");

	formPanel.setWidth(320);
	formPanel.setLabelWidth(75);

	jid = new TextField("Jabber id", "jid", 200);
	jid.setAllowBlank(false);
	jid.setValue(getGwtMetaProperty(GWT_PROPERTY_JID));
	formPanel.add(jid);

	passwd = new TextField("Password", "last", 200);
	passwd.setAllowBlank(false);
	passwd.setValue(getGwtMetaProperty(GWT_PROPERTY_PASSWD));
	passwd.setPassword(true);
	formPanel.add(passwd);

	jid.addListener(new FieldListenerAdapter() {
	    @Override
	    public void onChange(final Field field, final Object newVal, final Object oldVal) {
		dispatcher.fire(EmiteUIPlugin.REFLESH_USER_OPTIONS, generateUserChatOptions());
	    }
	});

	passwd.addListener(new FieldListenerAdapter() {
	    @Override
	    public void onChange(final Field field, final Object newVal, final Object oldVal) {
		dispatcher.fire(EmiteUIPlugin.REFLESH_USER_OPTIONS, generateUserChatOptions());
	    }
	});
	panel.add(formPanel);

	RootPanel.get().add(panel);
    }

    private void createInfoPanel() {
	final String info = getGwtMetaProperty(GWT_PROPERTY_INFOHTML);
	if (info.length() > 0) {
	    final Panel infoPanel = new Panel();
	    infoPanel.setHeader(false);
	    infoPanel.setClosable(false);
	    infoPanel.setBorder(false);
	    infoPanel.setPaddings(15);
	    final FormPanel formPanel = new FormPanel();
	    formPanel.setFrame(true);
	    formPanel.setTitle("Info", "info-icon");
	    formPanel.setWidth(320);
	    final Label infoLabel = new Label();
	    infoLabel.setHtml(info);
	    formPanel.add(infoLabel);
	    infoPanel.add(formPanel);
	    RootPanel.get().add(infoPanel);
	}
    }

    private UserChatOptions generateUserChatOptions() {
	// FIXME: vicente, no deberíamos acceder a esta constante, sino
	// preguntarle al roster manager en qué modo está...
	return new UserChatOptions(jid.getRawValue(), passwd.getRawValue(), "blue", RosterManager.DEF_SUBSCRIPTION_MODE);
    }

    private String getGwtMetaProperty(final String property) {
	return DOM.getElementProperty(DOM.getElementById(property), "content");
    }

}
