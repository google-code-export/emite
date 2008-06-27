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
package com.calclab.emite.client.xep.chatstate;

import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.xmpp.session.SessionScope;
import com.calclab.suco.client.container.Container;
import com.calclab.suco.client.container.Provider;
import com.calclab.suco.client.modules.Module;
import com.calclab.suco.client.modules.ModuleBuilder;

/**
 * Implements XEP-0085: Chat State Notifications
 * 
 * @see http://www.xmpp.org/extensions/xep-0085.html (Version: 1.2)
 * 
 */
public class ChatStateModule implements Module {
    private static final Class<StateManager> COMPONENTS_MANAGER = StateManager.class;

    public static StateManager getChatStateManager(final Container components) {
	return components.getInstance(COMPONENTS_MANAGER);
    }

    public Class<? extends Module> getType() {
	return ChatStateModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {
	builder.registerProvider(StateManager.class, new Provider<StateManager>() {
	    public StateManager get() {
		final ChatManager chatManager = builder.getInstance(ChatManager.class);
		return new StateManager(chatManager);
	    }
	}, SessionScope.class);

    }
}
