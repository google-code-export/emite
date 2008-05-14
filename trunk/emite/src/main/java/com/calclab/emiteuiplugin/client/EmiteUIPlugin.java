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
package com.calclab.emiteuiplugin.client;

import org.ourproject.kune.platf.client.dispatch.Dispatcher;
import org.ourproject.kune.platf.client.extend.Plugin;

import com.calclab.emite.client.Xmpp;
import com.calclab.emiteuiplugin.client.dialog.MultiChat;
import com.calclab.emiteuiplugin.client.dialog.MultiChatListener;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Image;

public class EmiteUIPlugin extends Plugin {

    private MultiChat multiChatDialog;

    public EmiteUIPlugin() {
	super("emiteuiplugin");
    }

    public void getChatDialog(final MultiChatCreationParam param) {
	if (multiChatDialog == null) {
	    createChatDialog(param);
	    preFetchImages();
	}
    }

    @Override
    protected void start() {
	EmiteEvents.subscribe(this, getDispatcher());
    }

    @Override
    protected void stop() {
	multiChatDialog.destroy();
    }

    private void createChatDialog(final MultiChatCreationParam param) {
	final Dispatcher dispatcher = getDispatcher();
	final Xmpp xmpp = Xmpp.create();
	xmpp.setBoshOptions(param.getBoshOptions());
	EmiteEvents.subscribe(xmpp, dispatcher);
	final MultiChatListener listener = EmiteEvents.createMultiChatListener(dispatcher);
	multiChatDialog = ChatDialogFactoryImpl.App.getInstance().createMultiChat(xmpp, param, listener);
	EmiteEvents.subscribeTo(dispatcher, multiChatDialog);

    }

    private void preFetchImages() {
	DeferredCommand.addCommand(new Command() {
	    public void execute() {
		final String[] imgs = { "ext-load.gif", "group_add.gif", "group-chat.gif", "moderatoruser.gif",
			"normaluser.gif", "person-def.gif", "smile.gif", "user_add.gif" };
		final String[] cssImgs = { "add.gif", "cancel.gif", "chat.gif", "colors.gif ", "del.gif", "exit.gif",
			"extload.gif", "forbidden.gif", "group-chat.gif", "group.gif", "new-chat.gif",
			"new-message.gif", "useradd.gif", "userf.gif", "user.gif" };
		doTheJob(imgs, "images");
		doTheJob(cssImgs, "css/img");
	    }

	    private void doTheJob(final String[] imgs, final String prefix) {
		for (int i = 0; i < imgs.length; i++) {
		    final String img = imgs[i];
		    Image.prefetch(prefix + "/" + img);
		}
	    }
	});
    }
}
