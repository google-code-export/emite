package com.calclab.emiteuimodule.client.sound;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emiteuimodule.client.status.StatusUI;
import com.gwtext.client.widgets.menu.CheckItem;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;
import com.gwtext.client.widgets.menu.event.CheckItemListenerAdapter;

public class SoundPanel implements View {
    public class SoundMenuItem extends MenuItem implements View {
        public SoundMenuItem(final String text, final Menu subMenu) {
            super(text, subMenu);
        }
    }
    private final I18nTranslationService i18n;
    private CheckItem soundDisabledItem;
    private CheckItem soundEnabledItem;

    private final SoundManager presenter;

    public SoundPanel(final SoundManager presenter, final I18nTranslationService i18n, final StatusUI statusUI) {
        this.presenter = presenter;
        this.i18n = i18n;
        final SoundMenuItem subsItem = new SoundMenuItem(i18n.t("Configure sound"), createSoundMenu());
        statusUI.addOptionsSubMenuItem(subsItem);
    }

    public void setSound(final boolean enabled) {
        if (enabled) {
            soundEnabledItem.setChecked(true);
        } else {
            soundDisabledItem.setChecked(true);
        }
    }

    private Menu createSoundMenu() {
        final Menu submenu = new Menu();
        submenu.setShadow(true);
        submenu.setMinWidth(10);
        soundEnabledItem = createSubMenuItem(i18n.t("Sound alerts enabled"), submenu, true);
        soundDisabledItem = createSubMenuItem(i18n.t("Sound alerts disabled"), submenu, false);
        return submenu;
    }

    private CheckItem createSubMenuItem(final String text, final Menu submenu, final boolean enable) {
        final CheckItemListenerAdapter itemListener = new CheckItemListenerAdapter() {
            @Override
            public void onCheckChange(CheckItem item, boolean checked) {
                if (checked) {
                    presenter.onSoundEnabled(enable);
                }
            }
        };
        final CheckItem item = new CheckItem();
        item.setText(text);
        item.setGroup("sound");
        item.addListener(itemListener);
        submenu.addItem(item);
        return item;
    }
}
