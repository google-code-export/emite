package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;

public class RosterItemDialog {

    private BasicDialog dialog;
    private FormPanel formPanel;
    private final I18nTranslationService i18n;
    private final MultiChatPresenter presenter;

    public RosterItemDialog(final I18nTranslationService i18n, final MultiChatPresenter presente) {
        this.i18n = i18n;
        this.presenter = presente;
    }

    public void show() {
        if (dialog == null) {
            dialog = new BasicDialog(i18n.t("Add a new buddy"), true, false, 350, 260);
            createForm();
        }
        dialog.show();
    }

    private void createForm() {
        formPanel = new FormPanel();
        formPanel.setFrame(true);

        formPanel.setWidth(290);
        formPanel.setLabelWidth(110);
        formPanel.setPaddings(10);

        Label label = new Label();
        label.setText(i18n.t("Please fill this form with the info of your new buddy. "
                + "Note tha the 'Jabber Id' sometimes is the same as the email " + "(in gmail accounts for instance). "
                + "As a 'Name' use something meaningful for you to refer your buddy (a nickname is ok)"));
        label.setCls("simple-form-label");
        label.setWidth(270);
        label.setHeight(40);
        formPanel.add(label);

        final TextField name = new TextField(i18n.t("Buddy Name"), "name", 150);
        name.setAllowBlank(false);
        formPanel.add(name);

        final TextField jid = new TextField(i18n.t("Buddy Jabber Id"), "jid", 150);
        jid.setAllowBlank(false);
        jid.setVtype(VType.EMAIL);
        formPanel.add(jid);

        Button add = new Button(i18n.tWithNT("Add", "used in button"));
        formPanel.addButton(add);
        add.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                presenter.addRosterItem(name.getValueAsString(), jid.getValueAsString());
                dialog.close();
                e.stopEvent();
                reset();
            }
        });

        Button cancel = new Button(i18n.tWithNT("Cancel", "used in button"));
        formPanel.addButton(cancel);
        cancel.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                dialog.close();
                e.stopEvent();
                reset();
            }
        });

        dialog.add(formPanel);
    }

    public void reset() {
        formPanel.getForm().reset();
    }

}
