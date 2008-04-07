package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.services.I18nTranslationService;
import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.calclab.emite.client.im.roster.Roster.SubscriptionMode;
import com.calclab.examplechat.client.chatuiplugin.UserChatOptions;
import com.gwtext.client.widgets.ColorPalette;
import com.gwtext.client.widgets.event.ColorPaletteListenerAdapter;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.Radio;
import com.gwtext.client.widgets.form.TextField;

public class MultiChatOptionsDialog {

    private static final String TYPEOFSUBS_FIELD = "subscription";
    private final I18nTranslationService i18n;
    private final MultiChatPresenter presenter;
    private BasicDialog dialog;
    private FormPanel formPanel;
    private TextField userTextField;
    private String selectedColor;
    private Radio manualRadio;
    private Radio autoAcceptRadio;
    private Radio autoRejectRadio;
    private UserChatOptions currentOptions;

    public MultiChatOptionsDialog(final I18nTranslationService i18n, final MultiChatPresenter presenter) {
        this.i18n = i18n;
        this.presenter = presenter;
    }

    public void show() {
        if (dialog == null) {
            createOptionsForm();
            selectedColor = currentOptions.getColor();
            dialog = new BasicDialogExtended(i18n.t("Chat options"), false, false, 350, 260, "useradd-icon", i18n
                    .tWithNT("Save", "used in button"), i18n.tWithNT("Cancel", "used in button"),
                    new BasicDialogListener() {
                        public void onCancelButtonClick() {
                            dialog.hide();
                        }

                        public void onFirstButtonClick() {
                            if (!currentOptions.getColor().equals(selectedColor)) {
                                presenter.onUserColorChanged(selectedColor);
                            }
                            SubscriptionMode subscriptionSelected;
                            if (autoAcceptRadio.getValue()) {
                                subscriptionSelected = SubscriptionMode.auto_accept_all;
                            } else if (autoRejectRadio.getValue()) {
                                subscriptionSelected = SubscriptionMode.auto_reject_all;
                            } else {
                                subscriptionSelected = SubscriptionMode.manual;
                            }
                            presenter.onUserSubscriptionModeChanged(subscriptionSelected);
                            dialog.hide();
                        }
                    });
        }
        setOptions(currentOptions);
        dialog.show();
    }

    public void setChatOptions(final UserChatOptions userChatOptions) {
        this.currentOptions = userChatOptions;
    }

    private void createOptionsForm() {
        formPanel = new FormPanel();
        formPanel.setFrame(true);

        formPanel.setWidth(333);
        formPanel.setLabelWidth(100);
        formPanel.setPaddings(10);

        Label label = new Label();
        label.setHtml("<p>" + i18n.t("Select your chat options:") + "</p>");
        label.setWidth(270);
        label.setHeight(40);
        formPanel.add(label);

        userTextField = new TextField("Color");
        userTextField.setWidth(146);
        userTextField.setValue(i18n.t("Your color"));

        ColorPalette colorPalette = new ColorPalette();
        colorPalette.addListener(new ColorPaletteListenerAdapter() {
            public void onSelect(final ColorPalette colorPalette, final String color) {
                setColor(color);
            }
        });

        colorPalette.setTitle("Pick a color");

        FieldSet groupTypeFieldSet = new FieldSet(i18n.t("Subscription options"));
        groupTypeFieldSet.setStyle("margin-left: 105px");
        groupTypeFieldSet.setCollapsible(false);
        formPanel.add(groupTypeFieldSet);

        autoAcceptRadio = new Radio();
        createRadio(groupTypeFieldSet, autoAcceptRadio,
                "Automatically accept other users request for add you as a buddy");

        autoRejectRadio = new Radio();
        createRadio(groupTypeFieldSet, autoRejectRadio,
                "Automatically reject other users request for add you as a buddy");

        manualRadio = new Radio();
        createRadio(groupTypeFieldSet, manualRadio, "Manually accept/reject other users request for add you as a buddy");

        dialog.add(formPanel);

    }

    private void createRadio(final FieldSet fieldSet, final Radio radio, final String label) {
        radio.setName(TYPEOFSUBS_FIELD);
        radio.setBoxLabel(label);
        radio.setAutoCreate(true);
        radio.setHideLabel(true);
        fieldSet.add(radio);
    }

    private void setOptions(final UserChatOptions options) {
        setColor(options.getColor());
        switch (options.getSubscriptionMode()) {
        case auto_accept_all:
            autoAcceptRadio.setChecked(true);
            break;
        case auto_reject_all:
            autoRejectRadio.setChecked(true);
            break;
        default:
            manualRadio.setChecked(true);
        }

    }

    private void setColor(final String color) {
        userTextField.setStyle("color:" + color + ";background-image:none;");
    }

}
