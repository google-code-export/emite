package com.calclab.examplechat.client.chatuiplugin.room;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.examplechat.client.chatuiplugin.dialog.BasicDialogExtended;
import com.calclab.examplechat.client.chatuiplugin.dialog.BasicDialogListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;

public class JoinRoomDialogPanel {

    private final I18nTranslationService i18n;
    private final MultiChatPresenter presenter;
    private BasicDialogExtended dialog;
    private FormPanel formPanel;
    private TextField roomName;
    private TextField serverName;

    public JoinRoomDialogPanel(final I18nTranslationService i18n, final MultiChatPresenter presenter) {
        this.i18n = i18n;
        this.presenter = presenter;
    }

    public void reset() {
        formPanel.getForm().reset();
    }

    public void show() {
        if (dialog == null) {
            dialog = new BasicDialogExtended(i18n.t("Join a chat room"), false, false, 330, 160, "chat-icon", i18n
                    .tWithNT("Join", "used in button"), i18n.tWithNT("Cancel", "used in button"),
                    new BasicDialogListener() {

                        public void onCancelButtonClick() {
                            dialog.hide();
                            reset();
                        }

                        public void onFirstButtonClick() {
                            presenter.joinRoom(roomName.getValueAsString(), serverName.getValueAsString());
                            dialog.hide();
                            reset();
                        }

                    });
            dialog.setResizable(false);
            createForm();

            // TODO define a UI Extension Point here
        }
        dialog.show();
    }

    private void createForm() {
        formPanel = new FormPanel();
        formPanel.setFrame(true);
        formPanel.setAutoScroll(false);

        formPanel.setWidth(333);
        formPanel.setLabelWidth(100);
        formPanel.setPaddings(10);

        roomName = new TextField(i18n.t("Room Name"), "name", 150);
        roomName.setAllowBlank(false);
        formPanel.add(roomName);

        serverName = new TextField(i18n.t("Room Server Name"), "jid", 150);
        serverName.setAllowBlank(false);
        // FIXME (get from options)
        serverName.setValue("rooms.localhost");
        ToolTip fieldToolTip = new ToolTip(i18n.t("Something like 'conference.jabber.org'."));
        fieldToolTip.applyTo(serverName);
        formPanel.add(serverName);

        dialog.add(formPanel);
    }
}
