package com.calclab.examplechat.client.chatuiplugin.abstractchat;

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
        // ScrollProblems?
        // childPanel.setMargins(5);
        add(childPanel);
        addStyleName("emite-ChatPanel-Conversation");
    }

    public void setChatTitle(final String name) {
        setTitle(name);
    }

    public void addInfoMessage(final String message) {
        HTML messageHtml = new HTML(message);
        addWidget(messageHtml);
        messageHtml.addStyleName("emite-ChatPanel-EventMessage");
    }

    public void addMessage(final String userAlias, final String color, final String message) {
        // FIXME: Use gwt DOM.create... for this:
        // Element userAliasSpan = DOM.createSpan();
        // DOM.setInnerText(userAliasSpan, userAlias);
        // DOM.setStyleAttribute(userAliasSpan, "color", color);
        String userHtml = "<span style=\"color: " + color + ";\">" + userAlias + "</span>:&nbsp;";
        HTML messageHtml = new HTML(userHtml + ChatTextFormatter.format(message).getHTML());
        addWidget(messageHtml);
    }

    public void addDelimiter(final String datetime) {
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
        childPanel.doLayout();
        widget.addStyleName("emite-ChatPanel-Message");
        scrollDown();
    }

}
