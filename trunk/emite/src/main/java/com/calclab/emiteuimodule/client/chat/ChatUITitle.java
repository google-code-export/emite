package com.calclab.emiteuimodule.client.chat;

import org.ourproject.kune.platf.client.ui.KuneUiUtils;

import com.google.gwt.user.client.ui.HTML;

public class ChatUITitle {
    private String title;
    private String tip;
    private String iconCls;
    private String textCls;

    public ChatUITitle() {
	this("", "", "", "");
    }

    public ChatUITitle(final String title, final String tip, final String iconCls, final String textCls) {
	this.title = title;
	this.tip = tip;
	this.iconCls = iconCls;
	this.textCls = textCls;
    }

    public String getIconCls() {
	return iconCls;
    }

    public String getTextCls() {
	return textCls;
    }

    public String getTip() {
	return tip;
    }

    public String getTitle() {
	return title;
    }

    public void setIconCls(final String iconCls) {
	this.iconCls = iconCls;
    }

    public void setTextCls(final String textCls) {
	this.textCls = textCls;
    }

    public void setTip(final String tip) {
	this.tip = tip;
    }

    public void setTitle(final String title) {
	this.title = title;
    }

    public String toHtml() {
	HTML titleHtml = new HTML(title);
	titleHtml.addStyleName(iconCls);
	titleHtml.addStyleName("e-tab-title");
	KuneUiUtils.setQuickTip(titleHtml, tip);
	return titleHtml.getHTML();
    }
}
