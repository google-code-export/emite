package com.calclab.examplechat.client.chatuiplugin.pairchat;

import com.calclab.examplechat.client.chatuiplugin.AbstractChatPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;

public class PairChatPanel extends AbstractChatPanel implements PairChatView {

    public PairChatPanel(final PairChatPresenter presenter) {
        super(presenter);
        this.addListener(new PanelListenerAdapter() {
            public void onActivate(final Panel panel) {
                presenter.onActivated();
            }
        });
    }

}
