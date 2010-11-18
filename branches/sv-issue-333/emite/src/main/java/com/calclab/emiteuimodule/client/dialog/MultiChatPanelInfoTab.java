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

import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.widgets.Panel;

public class MultiChatPanelInfoTab extends Panel {
    private static final String INFO_LABEL_ID = "MultiChatPanel-InfoLabel";
    private final Label infoLabel;

    public MultiChatPanelInfoTab(final String title) {
	setAttribute("enableDragDrop", true, true);
	setAttribute("enableDrop", true, true);
	setTitle(title, "info-icon");
	setBorder(true);
	setHeader(false);
	setClosable(false);
	infoLabel = new Label();
	infoLabel.ensureDebugId(INFO_LABEL_ID);
	add(infoLabel);
	setPaddings(7);
    }

    public void setText(final String info) {
	infoLabel.setText(info);
	if (isRendered()) {
	    doLayout();
	}
    }
}
