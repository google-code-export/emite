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
	// TODO use DOM
	// HTML titleHtml = new HTML(title);
	// if (iconCls != null && iconCls.length() > 0) {
	// titleHtml.addStyleName(iconCls);
	// }
	// titleHtml.addStyleName("e-tab-title");
	// KuneUiUtils.setQuickTip(titleHtml, tip);
	// return titleHtml.getHTML();
	return "<span class=\"e-tab-title " + iconCls + " " + textCls + "\" ext:qtip=\"" + tip + "\">" + title
		+ "</span>";
    }
}
