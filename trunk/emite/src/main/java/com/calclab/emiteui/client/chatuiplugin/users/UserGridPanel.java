/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteui.client.chatuiplugin.users;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emiteui.client.chatuiplugin.dialog.StatusUtil;
import com.calclab.emiteui.client.chatuiplugin.users.RoomUserUI.RoomUserType;
import com.calclab.emiteui.client.chatuiplugin.utils.XmppJID;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.layout.FitLayout;

public class UserGridPanel extends Panel {

    private static final String ALIAS = "alias";
    private static final String COLOR = "color";
    private static final String IMG = "img";
    private static final String JID = "jid";
    private static final String STATUSIMG = "status";
    private static final String STATUSTEXT = "statustext";
    private FieldDef[] fieldDefs;
    private final HashMap<XmppJID, UserGridMenu> menuMap;
    private final HashMap<XmppJID, Record> recordMap;
    private RecordDef recordDef;
    private Store store;
    private GridPanel grid;

    public UserGridPanel() {
        setBorder(false);
        setLayout(new FitLayout());
        createGrid();
        menuMap = new HashMap<XmppJID, UserGridMenu>();
        recordMap = new HashMap<XmppJID, Record>();
    }

    public void addUser(final ChatUserUI user, final UserGridMenu menu) {
        final String statusIcon = formatStatusIcon(user);
        addUser(user, statusIcon, formatPresenceStatus(user.getPresence()), menu);
    }

    public void addUser(final RoomUserUI user, final UserGridMenu menu, final String userType) {
        final String img = user.getUserType().equals(RoomUserType.moderator) ? "images/moderatoruser.gif"
                : "images/normaluser.gif";
        addUser(user, "<img src=\"" + img + "\">", userType, menu);
    }

    public void removeAllUsers() {
        store.removeAll();
        recordMap.clear();
        menuMap.clear();
    }

    public void removeUser(final AbstractChatUser user) {
        XmppJID userJid = user.getJid();
        Record storeToRemove = recordMap.get(userJid);
        if (storeToRemove == null) {
            Log.error("Trying to remove a non existing roster item: " + userJid);
        } else {
            store.remove(storeToRemove);
            menuMap.remove(userJid);
            recordMap.remove(userJid);
        }
        doLayoutIfNeeded();
    }

    public void updateRosterItem(final ChatUserUI user, final UserGridMenu menu) {
        // removeUser(user);
        // addUser(user, menu);
        Record recordToUpdate = recordMap.get(user.getJid());
        recordToUpdate.set(IMG, user.getIconUrl());
        recordToUpdate.set(ALIAS, formatAlias(user));
        recordToUpdate.set(JID, formatJid(user));
        recordToUpdate.set(COLOR, user.getColor());
        recordToUpdate.set(STATUSTEXT, formatPresenceStatus(user.getPresence()));
        recordToUpdate.set(STATUSIMG, formatStatusIcon(user));
        menuMap.put(user.getJid(), menu);
        doLayoutIfNeeded();
    }

    private void addUser(final AbstractChatUser user, final String statusIcon, final String statusTextOrig,
            final UserGridMenu menu) {
        final Record newUserRecord = recordDef.createRecord(new Object[] { user.getIconUrl(), formatJid(user),
                formatAlias(user), user.getColor(), statusIcon, statusTextOrig });
        recordMap.put(user.getJid(), newUserRecord);
        store.add(newUserRecord);
        menuMap.put(user.getJid(), menu);
        doLayoutIfNeeded();
    }

    private void createGrid() {
        grid = new GridPanel();
        fieldDefs = new FieldDef[] { new StringFieldDef(IMG), new StringFieldDef(JID), new StringFieldDef(ALIAS),
                new StringFieldDef(COLOR), new StringFieldDef(STATUSIMG), new StringFieldDef(STATUSTEXT) };
        recordDef = new RecordDef(fieldDefs);

        final MemoryProxy proxy = new MemoryProxy(new Object[][] {});

        final ArrayReader reader = new ArrayReader(1, recordDef);
        store = new Store(proxy, reader);
        store.load();
        grid.setStore(store);

        // GroupingStore store = new GroupingStore();
        // store.setReader(reader);
        // store.setDataProxy(proxy);
        // store.setSortInfo(new SortState("company", SortDir.ASC));
        // store.setGroupField("industry");
        // store.load();

        final Renderer iconRender = new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
                return Format.format("<img src=\"{0}\">", new String[] { record.getAsString(IMG) });
            }
        };
        final Renderer userAliasRender = new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
                return Format
                        .format(
                                "<span ext:qtip=\"{3}\" ext:qtitle=\"{4}\"\" style=\"vertical-align: bottom; color: {0} ;\">{1}&nbsp;&nbsp;</span>{2}",
                                new String[] { record.getAsString(COLOR), record.getAsString(ALIAS),
                                        record.getAsString(STATUSIMG), record.getAsString(STATUSTEXT),
                                        record.getAsString(JID) });
            }
        };
        final ColumnConfig[] columnsConfigs = new ColumnConfig[] {
                new ColumnConfig("Image", IMG, 24, false, iconRender, IMG),
                new ColumnConfig("Alias", ALIAS, 105, true, userAliasRender, ALIAS) };
        final ColumnModel columnModel = new ColumnModel(columnsConfigs);
        grid.setColumnModel(columnModel);

        grid.addGridRowListener(new GridRowListener() {

            public void onRowClick(final GridPanel grid, final int rowIndex, final EventObject e) {
                showMenu(rowIndex, e);
            }

            public void onRowContextMenu(final GridPanel grid, final int rowIndex, final EventObject e) {
                showMenu(rowIndex, e);
            }

            public void onRowDblClick(final GridPanel grid, final int rowIndex, final EventObject e) {
                showMenu(rowIndex, e);
            }

            private void showMenu(final int rowIndex, final EventObject e) {
                final Record record = store.getRecordAt(rowIndex);
                final String jid = record.getAsString(JID);
                final UserGridMenu menu = menuMap.get(new XmppJID(jid));
                menu.showMenu(e);
            }

        });

        // grid.setAutoExpandColumn(ALIAS);
        grid.stripeRows(true);
        final GridView view = new GridView();
        // i18n
        view.setEmptyText("Nobody");
        // view.setAutoFill(true);
        grid.setView(view);
        grid.setHideColumnHeader(true);
        grid.setBorder(false);
        // grid.setAutoHeight(true);
        grid.setAutoScroll(true);
        // countriesGrid.setEnableDragDrop(true);
        // countriesGrid.setDdGroup("myDDGroup");
        super.add(grid);
    }

    private void doLayoutIfNeeded() {
        // http://groups.google.com/group/gwt-ext/browse_thread/thread/6def43c434147596/69e487525d4c68e9?hl=en&lnk=gst&q=dolayout#69e487525d4c68e9
        // You should only explicitly call doLayout() if you add a child
        // component to a parent Container after the container has been
        // rendered.

        if (isRendered()) {
            this.doLayout();
            // this.syncSize();
        }
    }

    private String formatAlias(final AbstractChatUser user) {
        return user.getAlias() != null ? user.getAlias() : user.getJid().getNode();
    }

    private String formatJid(final AbstractChatUser user) {
        return user.getJid().toString();
    }

    private String formatPresenceStatus(final Presence presence) {
        // FIXME: maybe use default status messages
        String statusText;
        if (presence == null) {
            statusText = "";
        } else {
            statusText = presence.getStatus();
        }
        String statusToReturn = statusText != null ? statusText : "";
        if (statusToReturn == "null") {
            Log.error("null string in status from Presence from: " + presence.getFrom());
        }
        return statusToReturn;
    }

    private String formatStatusIcon(final ChatUserUI user) {
        return StatusUtil.getStatusIcon(user.getSubscription(), user.getPresence()).getHTML();
    }

}
