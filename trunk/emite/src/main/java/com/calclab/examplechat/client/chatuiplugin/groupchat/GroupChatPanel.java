package com.calclab.examplechat.client.chatuiplugin.groupchat;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.event.PanelListenerAdapter;

public class GroupChatPanel extends AbstractChatPanel implements GroupChatView {

    public GroupChatPanel(final GroupChatPresenter presenter) {
        super(presenter);
        this.addListener(new PanelListenerAdapter() {
            public void onActivate(final Panel panel) {
                presenter.onActivate();
            }

            public void onDeactivate(final Panel panel) {
                presenter.onDeactivate();
            }
        });
    }

}
