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
package com.calclab.emiteuimodule.client.openchat;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.calclab.emiteuimodule.client.status.StatusUI;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.event.BaseItemListener;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class OpenChatTestingPanel implements View, OpenChatTestingView {
    public class OpenChatTestingMenuItem extends Item implements View {
	public OpenChatTestingMenuItem(String text, BaseItemListener listener) {
	    super(text, listener);
	}
    }

    private final OpenChatTestingMenuItem item;

    public OpenChatTestingPanel(final OpenChatTestingPresenter presenter, StatusUI statusUI,
	    final I18nTranslationService i18n) {
	item = new OpenChatTestingMenuItem("Chat with other person (testing)", new BaseItemListenerAdapter() {
	    private OpenChatTestingJidPanel openChatTestingJidPanel;

	    @Override
	    public void onClick(BaseItem item, EventObject e) {
		if (openChatTestingJidPanel == null)
		    openChatTestingJidPanel = new OpenChatTestingJidPanel(i18n, presenter);
		openChatTestingJidPanel.show();
	    }
	});
	statusUI.addChatMenuItem(item);
    }

    public void setMenuItemEnabled(boolean enabled) {
	item.setDisabled(!enabled);
    }

}
