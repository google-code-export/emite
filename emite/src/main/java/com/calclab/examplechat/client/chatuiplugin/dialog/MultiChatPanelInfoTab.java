package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.Label;
import com.gwtext.client.widgets.Panel;

public class MultiChatPanelInfoTab extends Panel {
    private final Label infoLabel;
    private final I18nTranslationService i18n;

    public MultiChatPanelInfoTab(final I18nTranslationService i18n) {
        this.i18n = i18n;
        setTitle(i18n.t("Info"));
        setClosable(false);
        infoLabel = new Label();
        add(infoLabel);
        setPaddings(7);
    }

    public void setOnlineInfo() {
        infoLabel.setText(i18n.t("To start a chat, select a buddy or join to a chat room. "
                + "If you don't have buddies you can add them. "));
        Log.info("Online info show");
        doLayout();
    }

    public void setOfflineInfo() {
        infoLabel.setText(i18n.t("To start a chat you need to be 'online'."));
        doLayout();
    }
}
