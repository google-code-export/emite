package com.calclab.hablar.client.ui.pages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabPages extends AbstractPages {

    interface PagesTabsUiBinder extends UiBinder<Widget, TabPages> {
    }

    interface TabStyles extends HeaderStyles {

    }

    private static PagesTabsUiBinder uiBinder = GWT.create(PagesTabsUiBinder.class);

    @UiField
    TabLayoutPanel tabs;

    @UiField
    TabStyles headerStyle;

    public TabPages() {
	initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void removePage(PageWidget page) {
	tabs.remove(page);
    }

    @Override
    protected void addPage(PageWidget page, Position position) {
	PageHeader header = page.getHeader();
	header.setStyles(headerStyle);
	tabs.add(page, header);
    }

    @Override
    protected void showPage(PageWidget page) {
	int index = tabs.getWidgetIndex(page);
	tabs.selectTab(index);
    }

}
