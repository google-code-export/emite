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
package com.calclab.emiteuimodule.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Image;

public class ImagesHelper {

    public static void preFetchImages() {
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                final String[] imgs = { "ext-load.gif", "group_add.gif", "group-chat.gif", "moderatoruser.gif",
                        "normaluser.gif", "person-def.gif", "smile.gif", "user_add.gif", "user-unavail.gif" };
                final String[] cssImgs = { "add.gif", "cancel.gif", "emite-chat.gif", "colors.gif ", "del.gif",
                        "exit.gif", "extload.gif", "forbidden.gif", "group-chat.gif", "group.gif", "new-chat.gif",
                        "new-message.gif", "useradd.gif", "userf.gif", "user.gif", "e-icon.gif", "e-icon-a.gif" };
                prefetchImages(imgs, "images");
                prefetchImages(cssImgs, "images");
            }

            private void prefetchImages(final String[] imgs, final String prefix) {
                for (int i = 0; i < imgs.length; i++) {
                    final String img = imgs[i];
                    Image.prefetch(prefix + "/" + img);
                }
            }
        });
    }

}
