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
package com.calclab.emiteuiplugin.client.chat;

import org.ourproject.kune.platf.client.ui.HorizontalLine;

import com.calclab.emiteuiplugin.client.roster.ChatIconDescriptor;
import com.calclab.emiteuiplugin.client.utils.ChatTextFormatter;
import com.calclab.emiteuiplugin.client.utils.ChatUIUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.core.RegionPosition;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;
import com.gwtext.client.widgets.layout.BorderLayout;
import com.gwtext.client.widgets.layout.BorderLayoutData;

public class ChatUIPanel extends Panel implements ChatUIView {

    public static String genQuickTipLabel(final String labelText, final String tipTitle, final String tipText,
	    final AbstractImagePrototype icon) {
	String tipHtml = "<span style=\"vertical-align: middle;\" ext:qtip=\"" + tipText + "\"";
	if (tipTitle != null && tipTitle.length() > 0) {
	    tipHtml += " ext:qtitle=\"" + tipTitle + "\"";
	}
	tipHtml += ">";
	final Image iconImg = new Image();
	icon.applyTo(iconImg);
	iconImg.setStyleName("vamiddle");
	// setQuickTip(iconImg, tipText, tipTitle);
	tipHtml += iconImg.toString();
	tipHtml += "&nbsp;";
	tipHtml += labelText;
	tipHtml += "</span>";
	return tipHtml;
    }

    private final Panel childPanel;
    private final Panel conversationPanel;

    public ChatUIPanel(final ChatUIPresenter presenter) {
	setLayout(new BorderLayout());
	conversationPanel = new Panel();
	conversationPanel.setBorder(false);
	conversationPanel.setAutoScroll(true);
	setClosable(true);
	setAutoScroll(false);
	setBorder(false);
	childPanel = new Panel();
	final BorderLayoutData centerData = new BorderLayoutData(RegionPosition.CENTER);
	childPanel.setAutoScroll(false);
	childPanel.setBorder(false);
	childPanel.setPaddings(5);
	conversationPanel.add(childPanel);
	add(conversationPanel, centerData);
	addStyleName("emite-ChatPanel-Conversation");
	this.addListener(new PanelListenerAdapter() {
	    public void onActivate(final Panel panel) {
		presenter.onActivated();
	    }

	    public void onDeactivate(final Panel panel) {
		presenter.onDeactivated();
	    }
	});
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

    public void setChatTitle(final String chatTitle, final String tip, final ChatIconDescriptor iconId) {
	// FIXME Vicente: try to do this with css (with gwt-ext don't works)
	super.setTitle(genQuickTipLabel(chatTitle, "", tip, ChatUIUtils.getIcon(iconId)));
	postChatTitle();
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
	return DOM.getParent(childPanel.getElement());
    }

    private void postChatTitle() {
	if (super.isRendered()) {
	    super.doLayout();
	    // before: tab.getTextEl().highlight()
	}
    }

    private void scrollDown() {
	DOM.setElementPropertyInt(getScrollableElement(), "scrollTop", childPanel.getOffsetHeight());
    }

}
