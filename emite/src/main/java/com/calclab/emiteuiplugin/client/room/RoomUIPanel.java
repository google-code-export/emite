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
import org.ourproject.kune.platf.client.ui.EditableClickListener;
import org.ourproject.kune.platf.client.ui.EditableIconLabel;

import com.calclab.emiteuiplugin.client.chat.ChatUIPanel;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.layout.BorderLayoutData;
import com.gwtext.client.widgets.layout.FitLayout;

public class RoomUIPanel extends ChatUIPanel implements RoomUIView {

    private final RoomUIPresenter presenter;
    private final I18nTranslationService i18n;
    private EditableIconLabel subject;

    public RoomUIPanel(final I18nTranslationService i18n, final RoomUserListUIPanel roomUserListUIPanel,
	    final RoomUIPresenter presenter) {
	super(presenter);
	this.i18n = i18n;
	this.presenter = presenter;

	final BorderLayoutData eastData = new BorderLayoutData(RegionPosition.EAST);
	final Panel eastPanel = new Panel(i18n.t("Now in this room"));
	eastPanel.setLayout(new FitLayout());
	eastPanel.setBorder(false);
	eastPanel.setAutoScroll(true);
	eastPanel.setCollapsible(true);
	eastPanel.setWidth(150);
	eastPanel.setIconCls("group-icon");
	eastData.setMinSize(100);
	eastData.setMaxSize(250);
	// FIXME: when manual calc of size, set this to true
	eastData.setSplit(false);
	eastPanel.add(roomUserListUIPanel);
	super.add(eastPanel, eastData);

	final Panel northPanel = new Panel();
	northPanel.setHeight(27);
	northPanel.add(createSubjectPanel());
	northPanel.setBorder(false);
	final BorderLayoutData northData = new BorderLayoutData(RegionPosition.NORTH);
	northData.setSplit(false);
	super.add(northPanel, northData);
    }

    public void setSubject(final String newSubject) {
	subject.setText(newSubject);
    }

    public void setSubjectEditable(final boolean editable) {
	subject.setEditable(editable);
    }

    private Panel createSubjectPanel() {
	subject = new EditableIconLabel(i18n.t("Welcome to this room"), new EditableClickListener() {
	    public void onEdited(final String text) {
		presenter.onModifySubjectRequested(text);
	    }
	});
	subject.setHeight("27");
	// subject.addStyleName("x-panel-tbar");
	// subject.addStyleName("x-panel-tbar-noheader");
	subject.addStyleName("x-panel-header");
	subject.addStyleName("x-panel-header-noborder");
	subject.addStyleName("x-unselectable");
	subject.setClickToRenameLabel(i18n.t("Click to rename this room"));
	subject.setRenameDialogLabel(i18n.t("Write a new subject for this room"));
	subject.setRenameDialogTitle(i18n.t("Change the subject"));
	final Panel subjectPanel = new Panel();
	subjectPanel.add(subject);
	// final Toolbar topBar = new Toolbar();
	// topBar.addElement(subject.getElement());
	// subjectPanel.setTopToolbar(topBar);
	subjectPanel.setBorder(false);
	return subjectPanel;
    }

}
