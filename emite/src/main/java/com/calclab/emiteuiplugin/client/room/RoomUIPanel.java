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
package com.calclab.emiteuiplugin.client.room;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteuiplugin.client.chat.ChatUIPanel;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class RoomUIPanel extends ChatUIPanel implements RoomUIView {

    public RoomUIPanel(final I18nTranslationService i18n, final RoomUserListUIPanel roomUserListUIPanel,
	    final RoomUIPresenter presenter) {
	super(presenter);
	final BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
	final Panel userPanel = new Panel(i18n.t("Now in this room"));
	userPanel.setLayout(new FitLayout());
	userPanel.setBorder(false);
	userPanel.setAutoScroll(true);
	userPanel.setWidth(150);
	userPanel.setIconCls("group-icon");
	eastData.setMinSize(100);
	eastData.setMaxSize(250);
	eastData.setSplit(true);
	userPanel.add(roomUserListUIPanel);
	super.add(userPanel, eastData);
    }
}
