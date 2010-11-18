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
package com.calclab.emiteuimodule.client.chat;

import org.ourproject.kune.platf.client.ui.HorizontalLine;

import com.calclab.emiteuimodule.client.utils.ChatTextFormatter;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

public class ChatUIPanel extends Panel implements ChatUIView {
    private static final int MAX_TITLE_LENGTH = 12;
    // private static final String CHAT_PANEL_ID = "emite-cuip-c-";
    private final Panel childPanel;
    private final Panel conversationPanel;
    private Element scrollableElement;
    private final ChatUITitle chatTitle;

    public ChatUIPanel(final ChatUIPresenter presenter) {
	setLayout(new BorderLayout());
	conversationPanel = new Panel();
	conversationPanel.setBorder(true);
	conversationPanel.setAutoScroll(true);
	setClosable(true);
	setAutoScroll(false);
	setBorder(false);
	childPanel = new Panel();
	final BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
	childPanel.setAutoScroll(false);
	childPanel.setBorder(false);
	childPanel.setPaddings(5);
	// FIXME: we have to test that the id generated is correct:
	// childPanel.setId(CHAT_PANEL_ID + presenter.getOtherURI().toString());
	conversationPanel.add(childPanel);
	add(conversationPanel, centerData);
	addStyleName("emite-ChatPanel-Conversation");
	this.addListener(new PanelListenerAdapter() {
	    @Override
	    public void onActivate(final Panel panel) {
		presenter.onActivated();
	    }

	    @Override
	    public void onDeactivate(final Panel panel) {
		presenter.onDeactivated();
	    }
	});
	chatTitle = new ChatUITitle();
    }

    public void addDelimiter(final String datetime) {
	final HorizontalPanel hp = new HorizontalPanel();
	final HorizontalLine hr = new HorizontalLine();
	hp.add(new Label(datetime));
	hp.add(hr);
	hp.setWidth("100%");
	hp.setCellWidth(hr, "100%");
	addWidget(hp);
	hp.setStyleName("emite-ChatPanel-HorizDelimiter");
    }

    public void addInfoMessage(final String message) {
	final HTML messageHtml = new HTML(message);
	addWidget(messageHtml);
	messageHtml.addStyleName("emite-ChatPanel-EventMessage");
    }

    public void addMessage(final String userAlias, final String color, final String message) {
	// FIXME: Use gwt DOM.create... for this:
	// Element userAliasSpan = DOM.createSpan();
	// DOM.setInnerText(userAliasSpan, userAlias);
	// DOM.setStyleAttribute(userAliasSpan, "color", color);
	final String userHtml = "<span style=\"color: " + color + ";\">" + userAlias + "</span>:&nbsp;";
	final HTML messageHtml = new HTML(userHtml + ChatTextFormatter.format(message == null ? "" : message).getHTML());
	addWidget(messageHtml);
    }

    public void setChatIconCls(final String iconCls) {
	chatTitle.setIconCls(iconCls);
	updateTitle();
    }

    public void setChatTitle(final String title, final String tip) {
	chatTitle.setTitle(Format.ellipsis(title, MAX_TITLE_LENGTH));
	chatTitle.setTip(tip);
	updateTitle();
    }

    public void setChatTitleTextCls(final String textCls) {
	chatTitle.setTextCls(textCls);
	updateTitle();
    }

    private void addWidget(final Widget widget) {
	childPanel.add(widget);
	if (childPanel.isRendered()) {
	    childPanel.doLayout();
	}
	widget.addStyleName("emite-ChatPanel-Message");
	scrollDown();
    }

    private Element getScrollableElement() {
	if (scrollableElement == null) {
	    scrollableElement = DOM.getElementById(childPanel.getElement().getId()).getParentElement();
	}
	// Log.info("Parent: " + scrollableElement.getId());
	return scrollableElement;
    }

    private void postChatTitle() {
	if (super.isRendered()) {
	    super.doLayout(false);
	}
    }

    private void scrollDown() {
	if (childPanel.isRendered()) {
	    getScrollableElement().setScrollTop(childPanel.getOffsetHeight());
	    // Log.info("Offset: " + childPanel.getOffsetHeight());
	}
    }

    private void updateTitle() {
	setTitle(chatTitle.toHtml());
	postChatTitle();
    }

}
