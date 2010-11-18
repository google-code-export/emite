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
package com.calclab.emiteuimodule.client.utils.emoticons;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface Emoticons extends ImageBundle {

    public static class App {
	private static Emoticons ourInstance = null;

	public static synchronized Emoticons getInstance() {
	    if (ourInstance == null) {
		ourInstance = (Emoticons) GWT.create(Emoticons.class);
	    }
	    return ourInstance;
	}
    }

    @Resource("alien.png")
    AbstractImagePrototype alien();

    @Resource("andy.png")
    AbstractImagePrototype andy();

    @Resource("angel.png")
    AbstractImagePrototype angel();

    @Resource("angry.png")
    AbstractImagePrototype angry();

    @Resource("bandit.png")
    AbstractImagePrototype bandit();

    @Resource("blushing.png")
    AbstractImagePrototype blushing();

    @Resource("bullet_black.png")
    AbstractImagePrototype bulletBlack();

    @Resource("bullet_star.png")
    AbstractImagePrototype bulletStar();

    @Resource("cool.png")
    AbstractImagePrototype cool();

    @Resource("crying.png")
    AbstractImagePrototype crying();

    @Resource("devil.png")
    AbstractImagePrototype devil();

    @Resource("grin.png")
    AbstractImagePrototype grin();

    @Resource("happy.png")
    AbstractImagePrototype happy();

    @Resource("heart.png")
    AbstractImagePrototype heart();

    @Resource("joyful.png")
    AbstractImagePrototype joyful();

    @Resource("kissing.png")
    AbstractImagePrototype kissing();

    @Resource("lol.png")
    AbstractImagePrototype lol();

    @Resource("love.png")
    AbstractImagePrototype love();

    @Resource("ninja.png")
    AbstractImagePrototype ninja();

    @Resource("pinched.png")
    AbstractImagePrototype pinched();

    @Resource("policeman.png")
    AbstractImagePrototype policeman();

    @Resource("pouty.png")
    AbstractImagePrototype pouty();

    @Resource("sad.png")
    AbstractImagePrototype sad();

    @Resource("sick.png")
    AbstractImagePrototype sick();

    @Resource("sideways.png")
    AbstractImagePrototype sideways();

    @Resource("sleeping.png")
    AbstractImagePrototype sleeping();

    @Resource("smile.png")
    AbstractImagePrototype smile();

    @Resource("surprised.png")
    AbstractImagePrototype surprised();

    @Resource("tongue.png")
    AbstractImagePrototype tongue();

    @Resource("uncertain.png")
    AbstractImagePrototype uncertain();

    @Resource("unsure.png")
    AbstractImagePrototype unsure();

    @Resource("w00t.png")
    AbstractImagePrototype w00t();

    @Resource("whistling.png")
    AbstractImagePrototype whistling();

    @Resource("wink.png")
    AbstractImagePrototype wink();

    @Resource("wizard.png")
    AbstractImagePrototype wizard();

    @Resource("wondering.png")
    AbstractImagePrototype wondering();
}
