package com.calclab.emiteuimodule.client.dialog;

import com.gwtext.client.widgets.QuickTip;
import com.gwtext.client.widgets.QuickTips;

public class QuickTipsHelper {
    public QuickTipsHelper() {
	QuickTips.init();
	final QuickTip quickTipInstance = QuickTips.getQuickTip();
	quickTipInstance.setInterceptTitles(true);
	quickTipInstance.setDismissDelay(7000);
	quickTipInstance.setHideDelay(400);
	quickTipInstance.setMinWidth(100);
    }
}