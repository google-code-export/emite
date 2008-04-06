package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.layout.FitLayout;

public class RosterItemDialog {

    private BasicDialog dialog;
    private FormPanel formPanel;
    private final I18nTranslationService i18n;
    private final MultiChatPresenter presenter;
    private TextField name;
    private TextField jid;

    public RosterItemDialog(final I18nTranslationService i18n, final MultiChatPresenter presente) {
        this.i18n = i18n;
        this.presenter = presente;
    }

    public void show() {
        if (dialog == null) {
            dialog = new BasicDialog(i18n.t("Add a new buddy"), false, false, 350, 260);
            dialog.setLayout(new FitLayout());
            dialog.setCollapsible(false);
            dialog.setButtonAlign(Position.RIGHT);
            dialog.setIconCls("useradd-icon");

            Button add = new Button(i18n.tWithNT("Add", "used in button"));
            add.addListener(new ButtonListenerAdapter() {
                public void onClick(final Button button, final EventObject e) {
                    presenter.addRosterItem(name.getValueAsString(), jid.getValueAsString());
                    dialog.hide();
                    reset();
                }
            });

            Button cancel = new Button(i18n.tWithNT("Cancel", "used in button"));
            cancel.addListener(new ButtonListenerAdapter() {
                public void onClick(final Button button, final EventObject e) {
                    dialog.hide();
                    reset();
                }
            });

            dialog.addButton(add);
            dialog.addButton(cancel);

            createForm();

            // TODO define a UI Extension Point here
        }
        dialog.show();
    }

    private void createForm() {
        formPanel = new FormPanel();
        formPanel.setFrame(true);

        formPanel.setWidth(333);
        formPanel.setLabelWidth(100);
        formPanel.setPaddings(10);

        Label label = new Label();
        label.setText(i18n
                .t("Please fill this form with the info of your new buddy. "
                        + "Note that the 'Jabber Id' sometimes is the same as the email "
                        + "(in gmail accounts for instance)."));
        label.setCls("form-label-bpadding");
        label.setWidth(270);
        label.setHeight(40);
        formPanel.add(label);

        name = new TextField(i18n.t("Buddy Nickname"), "name", 150);
        name.setAllowBlank(false);
        formPanel.add(name);

        jid = new TextField(i18n.t("Buddy Jabber Id"), "jid", 150);
        jid.setAllowBlank(false);
        jid.setVtype(VType.EMAIL);
        formPanel.add(jid);

        dialog.add(formPanel);
    }

    public void reset() {
        formPanel.getForm().reset();
    }

}
