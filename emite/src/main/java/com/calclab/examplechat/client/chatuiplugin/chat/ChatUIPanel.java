package com.calclab.examplechat.client.chatuiplugin.chat;

import org.ourproject.kune.platf.client.ui.HorizontalLine;

import com.calclab.examplechat.client.chatuiplugin.utils.ChatTextFormatter;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;

public class ChatUIPanel extends Panel implements ChatUIView {

    private final Panel childPanel;

    public ChatUIPanel(final ChatUIPresenter presenter) {
        setClosable(true);
        setAutoScroll(true);
        setBorder(false);
        childPanel = new Panel();
        childPanel.setAutoScroll(false);
        childPanel.setBorder(false);
        childPanel.setPaddings(5);
        add(childPanel);
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
        HorizontalPanel hp = new HorizontalPanel();
        HorizontalLine hr = new HorizontalLine();
        hp.add(new Label(datetime));
        hp.add(hr);
        hp.setWidth("100%");
        hp.setCellWidth(hr, "100%");
        addWidget(hp);
        hp.setStyleName("emite-ChatPanel-HorizDelimiter");
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

    public void setChatTitle(final String name) {
        super.setTitle(name);
    }

    private void addWidget(final Widget widget) {
        childPanel.add(widget);
        childPanel.doLayout();
        widget.addStyleName("emite-ChatPanel-Message");
        scrollDown();
    }

    private Element getScrollableElement() {
        return DOM.getParent(childPanel.getElement());
    }

    private void scrollDown() {
        DOM.setElementPropertyInt(getScrollableElement(), "scrollTop", childPanel.getOffsetHeight());
    }

}
