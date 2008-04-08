package com.calclab.examplechat.client.chatuiplugin.roster;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.calclab.examplechat.client.chatuiplugin.dialog.BasicDialogExtended;
import com.calclab.examplechat.client.chatuiplugin.dialog.BasicDialogListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.gwtext.client.widgets.ToolTip;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;

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
            dialog = new BasicDialogExtended(i18n.t("Add a new buddy"), false, false, 330, 200, "useradd-icon", i18n
                    .tWithNT("Add", "used in button"), i18n.tWithNT("Cancel", "used in button"),
                    new BasicDialogListener() {

                        public void onCancelButtonClick() {
                            presenter.addRosterItem(name.getValueAsString(), jid.getValueAsString());
                            dialog.hide();
                            reset();
                        }

                        public void onFirstButtonClick() {
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

        Label label = new Label();
        label.setHtml("<p>" + i18n.t("Please fill this form with the info of your new buddy:") + "</p>");
        label.setWidth(270);
        label.setHeight(40);
        formPanel.add(label);

        name = new TextField(i18n.t("Buddy Nickname"), "name", 150);
        name.setAllowBlank(false);
        formPanel.add(name);

        jid = new TextField(i18n.t("Buddy Jabber Id"), "jid", 150);
        jid.setAllowBlank(false);
        jid.setVtype(VType.EMAIL);
        ToolTip fieldToolTip = new ToolTip(i18n.t("Note that the 'Jabber Id' sometimes is the same as the email "
                + "(in gmail accounts for instance)."));
        fieldToolTip.applyTo(jid);
        formPanel.add(jid);

        dialog.add(formPanel);
    }

    public void reset() {
        formPanel.getForm().reset();
    }

}
