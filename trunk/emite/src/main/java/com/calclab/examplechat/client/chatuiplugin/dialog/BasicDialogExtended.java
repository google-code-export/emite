package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.ourproject.kune.platf.client.ui.dialogs.BasicDialog;

import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Position;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class BasicDialogExtended extends BasicDialog {

    public BasicDialogExtended(final String title, final boolean modal, final boolean autoscroll, final int width,
            final int heigth, final String icon, final String firstButtonTitle, final String cancelButtonTitle,
            final BasicDialogListener listener) {
        super(title, modal, autoscroll, width, heigth);
        setLayout(new FitLayout());
        setCollapsible(false);
        setButtonAlign(Position.RIGHT);
        setIconCls(icon);

        Button firstButton = new Button(firstButtonTitle);
        firstButton.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                listener.onFirstButtonClick();
            }
        });

        Button cancel = new Button(cancelButtonTitle);
        cancel.addListener(new ButtonListenerAdapter() {
            public void onClick(final Button button, final EventObject e) {
                listener.onCancelButtonClick();
            }
        });

        addButton(firstButton);
        addButton(cancel);
    }
}
