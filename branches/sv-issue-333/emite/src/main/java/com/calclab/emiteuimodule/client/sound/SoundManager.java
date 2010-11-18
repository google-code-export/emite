/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuimodule.client.sound;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.subscription.SubscriptionUI;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

public class SoundManager {

    private SoundController soundController;
    private Sound sound;
    private boolean soundEnabled;
    private SoundPanel soundPanel;

    public SoundManager(final SubscriptionUI subsUI, final RoomUIManager roomUIManager) {
	configureSound();
	if (roomUIManager != null) {
	    roomUIManager.onUserAlert(new Listener<String>() {
		public void onEvent(final String parameter) {
		    click();
		}
	    });
	}
	if (subsUI != null) {
	    subsUI.onUserAlert(new Listener0() {
		public void onEvent() {
		    click();
		}
	    });
	}
    }

    // With more UI modules, click will be private (and SoundModule will be
    // optional)
    public void click() {
	// sound.setVolume(50);
	if (soundEnabled) {
	    sound.play();
	}
    }

    public void init(final SoundPanel soundPanel) {
	this.soundPanel = soundPanel;
	setSound(true);
    }

    public void onSoundEnabled(final boolean enabled) {
	soundEnabled = enabled;
    }

    public void setSound(final boolean enabled) {
	soundPanel.setSound(enabled);
	soundEnabled = enabled;
    }

    private void configureSound() {
	soundController = new SoundController();
	soundController.setPrioritizeFlashSound(true);
	soundController.setDefaultVolume(50);
	sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_X_WAV, "click.wav");
    }
}
