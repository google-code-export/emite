/*
 * Copyright (C) 2007 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * Kune is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kune is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.ui.HorizontalLine;

import com.calclab.examplechat.client.chatuiplugin.utils.ChatTextFormatter;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;

public abstract class AbstractChatPanel extends Panel implements AbstractChatView {

    private final Panel childPanel;

    public AbstractChatPanel(final AbstractChatPresenter presenter) {
        setClosable(true);
        setAutoScroll(true);
        setBorder(false);
        childPanel = new Panel();
        childPanel.setAutoScroll(false);
        childPanel.setBorder(false);
        add(childPanel);
        addStyleName("emite-ChatPanel-Conversation");
    }

    public void setChatTitle(final String name) {
        setTitle(name);
    }

    public void showInfoMessage(final String message) {
        HTML messageHtml = new HTML(message);
        addWidget(messageHtml);
        messageHtml.addStyleName("emite-ChatPanel-EventMessage");
    }

    public void showMessage(final String userAlias, final String color, final String message) {
        // FIXME: Use gwt DOM.create... for this:
        String userHtml = "<span style=\"color: " + color + ";\">" + userAlias + "</span>:&nbsp;";
        HTML messageHtml = new HTML(userHtml + ChatTextFormatter.format(message).getHTML());
        addWidget(messageHtml);
    }

    public void showDelimiter(final String datetime) {
        HorizontalPanel hp = new HorizontalPanel();
        HorizontalLine hr = new HorizontalLine();
        hp.add(new Label(datetime));
        hp.add(hr);
        hp.setWidth("100%");
        hp.setCellWidth(hr, "100%");
        addWidget(hp);
        hp.setStyleName("emite-ChatPanel-HorizDelimiter");
    }

    public void restoreScrollPos(final int position) {
        DOM.setElementPropertyInt(getScrollableElement(), "scrollTop", position);
    }

    public int getScrollPos() {
        return DOM.getElementPropertyInt(getScrollableElement(), "scrollTop");
    }

    public void scrollDown() {
        DOM.setElementPropertyInt(getScrollableElement(), "scrollTop", childPanel.getOffsetHeight());
    }

    private Element getScrollableElement() {
        return DOM.getParent(childPanel.getElement());
    }

    private void addWidget(final Widget widget) {
        childPanel.add(widget);
        childPanel.render(widget.getElement());
        widget.addStyleName("emite-ChatPanel-Message");
        scrollDown();
    }

}
