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
	// soundController.setPrioritizeFlashSound(false);
	// soundController.setDefaultVolume(0);
	sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_X_WAV, "click.wav");
    }
}
