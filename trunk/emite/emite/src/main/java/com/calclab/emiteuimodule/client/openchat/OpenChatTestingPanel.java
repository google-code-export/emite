package com.calclab.emiteuimodule.client.openchat;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteuimodule.client.status.StatusUI;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.menu.BaseItem;
import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.event.BaseItemListener;
import com.gwtext.client.widgets.menu.event.BaseItemListenerAdapter;

public class OpenChatTestingPanel implements View, OpenChatTestingView {
    public class OpenChatTestingMenuItem extends Item implements View {
	public OpenChatTestingMenuItem(String text, BaseItemListener listener) {
	    super(text, listener);
	}
    }

    private final OpenChatTestingMenuItem item;

    public OpenChatTestingPanel(final OpenChatTestingPresenter presenter, StatusUI statusUI,
	    final I18nTranslationService i18n) {
	item = new OpenChatTestingMenuItem("Chat with other person (testing)", new BaseItemListenerAdapter() {
	    private OpenChatTestingJidPanel openChatTestingJidPanel;

	    @Override
	    public void onClick(BaseItem item, EventObject e) {
		if (openChatTestingJidPanel == null)
		    openChatTestingJidPanel = new OpenChatTestingJidPanel(i18n, presenter);
		openChatTestingJidPanel.show();
	    }
	});
	statusUI.addChatMenuItem(item);
    }

    public void setMenuItemEnabled(boolean enabled) {
	item.setDisabled(!enabled);
    }

}
