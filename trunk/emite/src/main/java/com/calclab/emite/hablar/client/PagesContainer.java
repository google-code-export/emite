package com.calclab.emite.hablar.client;

import com.calclab.emite.hablar.client.pages.ConversationPage;
import com.calclab.emite.hablar.client.pages.MainPage;
import com.calclab.emite.hablar.client.pages.roster.RosterView;
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

public class PagesContainer extends DockPanel {
    private final DecoratedStackPanel stack;

    public PagesContainer(final MainPage mainPage, final RosterView rosterView) {
	stack = new DecoratedStackPanel();
	stack.add(mainPage, "login");
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

    public void removePanel(final ConversationPage view) {
	stack.remove(view);
    }

    public void setPanelLabel(final int index, final String label) {
	stack.setStackText(index, label, true);
    }

    public void showPage(final int index) {
	stack.showStack(index);
    }

}
