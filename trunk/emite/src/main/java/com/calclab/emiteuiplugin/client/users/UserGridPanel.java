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
package com.calclab.emiteuiplugin.client.users;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.extra.muc.Occupant.Role;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.utils.ChatUIUtils;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.dd.DragSource;
import com.gwtext.client.dd.DropTarget;
import com.gwtext.client.dd.DropTargetConfig;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridDragData;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.layout.FitLayout;

public class UserGridPanel extends Panel {

    public static final String INVITE_TO_GROUP_DD = "inviteToGroupDD";
    private static final String ALIAS = "alias";
    private static final String COLOR = "color";
    private static final String IMG = "img";
    private static final String JID = "jid";
    private static final String STATUSIMG = "status";
    private static final String STATUSTEXT = "statustext";
    private FieldDef[] fieldDefs;
    private final HashMap<XmppURI, UserGridMenu> menuMap;
    private final HashMap<XmppURI, Record> recordMap;
    private RecordDef recordDef;
    private Store store;
    private GridPanel grid;

    public UserGridPanel(final String emptyText) {
	this(emptyText, null, null);
    }

    public UserGridPanel(final String emptyText, final DragGridConfiguration dragGridConfiguration) {
	this(emptyText, dragGridConfiguration, null);
    }

    public UserGridPanel(final String emptyText, final DragGridConfiguration dragGridConfiguration,
	    final DropGridConfiguration dropGridConfiguration) {
	setBorder(false);
	setLayout(new FitLayout());
	createGrid(emptyText, dragGridConfiguration, dropGridConfiguration);
	menuMap = new HashMap<XmppURI, UserGridMenu>();
	recordMap = new HashMap<XmppURI, Record>();
    }

    public UserGridPanel(final String emptyText, final DropGridConfiguration dropGridConfiguration) {
	this(emptyText, null, dropGridConfiguration);
    }

    public void addUser(final ChatUserUI user, final UserGridMenu menu) {
	final String statusIcon = formatStatusIcon(user);
	addRecord(user, statusIcon, user.getStatusText(), menu);
    }

    public void addUser(final RoomUserUI user, final UserGridMenu menu, final String userType) {
	final String img = user.getRole().equals(Role.moderator) ? "images/moderatoruser.gif" : "images/normaluser.gif";
	addRecord(user, "<img src=\"" + img + "\">", userType, menu);
    }

    public void removeAllUsers() {
	store.removeAll();
	recordMap.clear();
	menuMap.clear();
    }

    public void removeUser(final AbstractChatUser user) {
	final XmppURI userJid = user.getURI();
	final Record storeToRemove = recordMap.get(userJid);
	if (storeToRemove == null) {
	    Log.error("Trying to remove a non existing roster item: " + userJid);
	} else {
	    store.remove(storeToRemove);
	    menuMap.remove(userJid);
	    recordMap.remove(userJid);
	}
	doLayoutIfNeeded();
    }

    public void resizeGrid(final int newWidthAvailable) {
	// TODO
	// grid.setWidth(newWidthAvailable - SOMETHING);
    }

    public void updateRosterItem(final ChatUserUI user, final UserGridMenu menu) {
	final Record recordToUpdate = recordMap.get(user.getURI());
	recordToUpdate.set(IMG, user.getIconUrl());
	recordToUpdate.set(ALIAS, formatAlias(user));
	recordToUpdate.set(JID, formatJid(user));
	recordToUpdate.set(COLOR, user.getColor());
	recordToUpdate.set(STATUSTEXT, formatStatus(user.getStatusText()));
	recordToUpdate.set(STATUSIMG, formatStatusIcon(user));
	menuMap.put(user.getURI(), menu);
	sort();
	doLayoutIfNeeded();
    }

    private void addRecord(final AbstractChatUser user, final String statusIcon, final String statusTextOrig,
	    final UserGridMenu menu) {
	final Record newUserRecord = recordDef.createRecord(new Object[] { user.getIconUrl(), formatJid(user),
		formatAlias(user), user.getColor(), statusIcon, formatStatus(statusTextOrig) });
	recordMap.put(user.getURI(), newUserRecord);
	store.add(newUserRecord);
	menuMap.put(user.getURI(), menu);
	sort();
	doLayoutIfNeeded();
    }

    private void configureDrag(final DragGridConfiguration dragGridConfiguration) {
	grid.setEnableDragDrop(true);
	grid.setDdGroup(dragGridConfiguration.getDdGroupId());
	grid.setDragDropText(dragGridConfiguration.getDragMessage());
    }

    private void configureDrop(final DropGridConfiguration dropGridConfiguration) {
	grid.setEnableDragDrop(true);
	grid.setDdGroup(dropGridConfiguration.getDdGroupId());
	final DropTargetConfig dCfg = new DropTargetConfig();
	dCfg.setTarget(true);
	dCfg.setdDdGroup(dropGridConfiguration.getDdGroupId());
	new DropTarget(grid, dCfg) {
	    public boolean notifyDrop(final DragSource src, final EventObject e, final DragData dragData) {
		if (dragData instanceof GridDragData) {
		    final GridDragData gridDragData = (GridDragData) dragData;
		    final Record[] records = gridDragData.getSelections();
		    for (int i = 0; i < records.length; i++) {
			dropGridConfiguration.getListener().onDrop(XmppURI.jid(records[i].getAsString(JID)));
		    }
		}
		return true;
	    }

	    public String notifyEnter(final DragSource src, final EventObject e, final DragData data) {
		return "x-tree-drop-ok-append";
	    }

	    public String notifyOver(final DragSource src, final EventObject e, final DragData data) {
		return "x-tree-drop-ok-append";
	    }
	};
    }

    private void createGrid(final String emptyText, final DragGridConfiguration dragGridConfiguration,
	    final DropGridConfiguration dropGridConfiguration) {
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
	    public String render(final Object value, final CellMetadata cellMetadata, final Record record,
		    final int rowIndex, final int colNum, final Store store) {
		return Format.format("<img src=\"{0}\">", new String[] { record.getAsString(IMG) });
	    }
	};
	final Renderer userAliasRender = new Renderer() {
	    public String render(final Object value, final CellMetadata cellMetadata, final Record record,
		    final int rowIndex, final int colNum, final Store store) {
		return Format
			.format(
				"<span ext:qtip=\"{3}\" ext:qtitle=\"{4}\"\" style=\"vertical-align: bottom; color: {0} ;\">{1}&nbsp;&nbsp;</span>{2}",
				new String[] {
					record.getAsString(COLOR),
					record.getAsString(ALIAS),
					record.getAsString(STATUSIMG),
					record.getAsString(STATUSTEXT).equals("null") ? " " : record
						.getAsString(STATUSTEXT), record.getAsString(JID) });
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
		final UserGridMenu menu = menuMap.get(uri(jid));
		menu.showMenu(e);
	    }
	});
	// grid.setAutoExpandColumn(ALIAS);
	grid.stripeRows(true);
	final GridView view = new GridView();
	// i18n
	// view.setAutoFill(true);
	view.setEmptyText(emptyText);
	grid.setView(view);
	grid.setHideColumnHeader(true);
	grid.setBorder(false);
	grid.setAutoScroll(true);
	if (dropGridConfiguration != null) {
	    configureDrop(dropGridConfiguration);
	}
	if (dragGridConfiguration != null) {
	    configureDrag(dragGridConfiguration);
	} else {
	    grid.setDraggable(false);
	}
	super.add(grid);
    }

    private void doLayoutIfNeeded() {
	// http://groups.google.com/group/gwt-ext/browse_thread/thread/6def43c434147596/69e487525d4c68e9?hl=en&lnk=gst&q=dolayout#69e487525d4c68e9
	// You should only explicitly call doLayout() if you add a child
	// component to a parent Container after the container has been
	// rendered.
	if (isRendered()) {
	    this.doLayout();
	}
    }

    private String formatAlias(final AbstractChatUser user) {
	final String alias = user.getAlias();
	if (alias == null || alias.length() == 0) {
	    return user.getURI().getNode();
	} else {
	    return alias;
	}
    }

    private String formatJid(final AbstractChatUser user) {
	return user.getURI().toString();
    }

    private String formatStatus(final String statusText) {
	// ext don't like ""
	return statusText == null ? " " : statusText;
    }

    private String formatStatusIcon(final ChatUserUI user) {
	return ChatUIUtils.getIcon(user.getStatusIcon()).getHTML();
    }

    private void sort() {
	store.sort(ALIAS);
    }
}
