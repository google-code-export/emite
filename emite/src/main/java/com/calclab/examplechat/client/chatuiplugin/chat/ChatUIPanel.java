package com.calclab.examplechat.client.chatuiplugin.chat;

import java.util.HashMap;

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

    private static final String[] USERCOLORS = { "green", "navy", "black", "grey", "olive", "teal", "blue", "lime",
            "purple", "fuchsia", "maroon", "red" };

    private int oldColor;

    private final HashMap<String, String> userColors;

    public ChatUIPanel(final ChatUIPresenter presenter) {
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
        userColors = new HashMap<String, String>();
        this.addListener(new PanelListenerAdapter() {
            public void onActivate(final Panel panel) {
                presenter.onActivated();
            }

            public void onDeactivate(final Panel panel) {
                presenter.onDeactivated();
            }
        });
    }

    public void setChatTitle(final String name) {
        setTitle(name);
    }

    public void addInfoMessage(final String message) {
        HTML messageHtml = new HTML(message);
        addWidget(messageHtml);
        messageHtml.addStyleName("emite-ChatPanel-EventMessage");
    }

    public void addMessage(final String userAlias, final String message) {
        // FIXME: Use gwt DOM.create... for this:
        // Element userAliasSpan = DOM.createSpan();
        // DOM.setInnerText(userAliasSpan, userAlias);
        // DOM.setStyleAttribute(userAliasSpan, "color", color);
        String userHtml = "<span style=\"color: " + getColor(userAlias) + ";\">" + userAlias + "</span>:&nbsp;";
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

    public void setUserColor(final String userAlias, final String color) {
        userColors.put(userAlias, color);
    }

    private void scrollDown() {
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

    private String getColor(final String userAlias) {
        String color = userColors.get(userAlias);
        if (color == null) {
            color = getNextColor();
            setUserColor(userAlias, color);
        }
        return color;
    }

    private String getNextColor() {
        final String color = USERCOLORS[oldColor++];
        if (oldColor >= USERCOLORS.length) {
            oldColor = 0;
        }
        return color;
    }

}
