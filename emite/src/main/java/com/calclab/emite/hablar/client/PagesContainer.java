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
package com.calclab.emite.hablar.client;

import com.calclab.emite.hablar.client.pages.roster.RosterView;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

public class PagesContainer extends DockPanel {
    private final DecoratedStackPanel stack;

    public PagesContainer(final RosterView rosterView) {
	stack = new DecoratedStackPanel();
	stack.add(rosterView, "roster");
	add(stack, DockPanel.CENTER);
    }

    /**
     * @param label
     * @param page
     *            a widget
     * @return a page id
     */
    public int addPage(final String label, final Widget page) {
	stack.add(page, label);
	return stack.getWidgetCount() - 1;
    }

    public void changePageTitle(final int id, final String title) {
	stack.setStackText(id, title);
    }

    public int getSelectedIndex() {
	return stack.getSelectedIndex();
    }

    public void setPanelLabel(final int index, final String label) {
	stack.setStackText(index, label, true);
    }

    public void showPage(final int index) {
	stack.showStack(index);
    }

}
