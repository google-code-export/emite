package com.calclab.emiteuimodule.client.dialog;

import com.gwtext.client.widgets.MessageBox;

public class MessageDialog {

    public static void alert(final String title, final String message) {
	MessageBox.alert(title, message);
    }

}
