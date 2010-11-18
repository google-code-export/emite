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
package com.calclab.emiteuimodule.client.users;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.packet.TextUtils;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.muc.client.Occupant.Role;
import com.calclab.emiteuimodule.client.utils.ChatUIUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.SortDir;
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
import com.gwtext.client.widgets.grid.RowSelectionModel;
import com.gwtext.client.widgets.grid.event.GridCellListenerAdapter;
import com.gwtext.client.widgets.grid.event.GridRowListener;
import com.gwtext.client.widgets.layout.FitLayout;

public class UserGridPanel extends Panel {
    public static final String USER_GROUP_DD = "userGroupDD";
    public static final String JID = "jid";
    public static final int DEFAULT_INITIAL_WIDTH = 120;
    private static final String ALIAS = "alias";
    private static final String COLOR = "color";
    private static final String IMG = "img";
    private static final String STATUSIMG = "status";
    private static final String STATUSTEXT = "statustext";
    private FieldDef[] fieldDefs;
    private final HashMap<XmppURI, UserGridMenu> menuMap;
    private final HashMap<XmppURI, Record> recordMap;
    private RecordDef recordDef;
    private Store store;
    private GridPanel grid;
    private final UserGridListener listener;

    public UserGridPanel(final String emptyText, final DragGridConfiguration dragGridConfiguration,
	    final DropGridConfiguration dropGridConfiguration, final UserGridListener listener) {
	this.listener = listener;
	setBorder(false);
	setLayout(new FitLayout());
	createGrid(emptyText, dragGridConfiguration, dropGridConfiguration);
	menuMap = new HashMap<XmppURI, UserGridMenu>();
	recordMap = new HashMap<XmppURI, Record>();
    }

    public UserGridPanel(final String emptyText, final DragGridConfiguration dragGridConfiguration,
	    final UserGridListener listener) {
	this(emptyText, dragGridConfiguration, null, listener);
    }

    public UserGridPanel(final String emptyText, final DropGridConfiguration dropGridConfiguration,
	    final UserGridListener listener) {
	this(emptyText, null, dropGridConfiguration, listener);
    }

    public UserGridPanel(final String emptyText, final UserGridListener listener) {
	this(emptyText, null, null, listener);
    }

    public void addUser(final ChatUserUI user, final UserGridMenu menu) {
	final String statusIcon = formatStatusIcon(user);
	addRecord(user, statusIcon, user.getStatusText(), menu);
    }

    public void addUser(final RoomUserUI user, final UserGridMenu menu, final String userType) {
	final String img = user.getRole().equals(Role.moderator) ? "images/moderatoruser.gif" : "images/normaluser.gif";
	addRecord(user, "<img src=\"" + img + "\">", userType, menu);
    }

    public void confDropInPanel(final Panel panel, final DropGridConfiguration dropGridConfiguration) {
	// FIXME: This doesn't works :-/
	// http://extjs.com/forum/showthread.php?t=18105
	final DropTargetConfig dCfg = new DropTargetConfig();
	dCfg.setTarget(true);
	dCfg.setdDdGroup(dropGridConfiguration.getDdGroupId());
	new DropTarget(panel, dCfg) {
	    @Override
	    public boolean notifyDrop(final DragSource src, final EventObject e, final DragData dragData) {
		if (dragData instanceof GridDragData) {
		    final GridDragData gridDragData = (GridDragData) dragData;
		    final Record[] records = gridDragData.getSelections();
		    for (Record record : records) {
			dropGridConfiguration.getListener().onDrop(XmppURI.jid(record.getAsString(UserGridPanel.JID)));
		    }
		}
		return true;
	    }

	    @Override
	    public String notifyEnter(final DragSource src, final EventObject e, final DragData data) {
		return "x-tree-drop-ok-append";
	    }

	    @Override
	    public String notifyOver(final DragSource src, final EventObject e, final DragData data) {
		return "x-tree-drop-ok-append";
	    }
	};
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

    @Override
    public void setWidth(final int width) {
	// Log.info("Grid width: " + width);
	grid.setWidth(width - 27);
	super.setWidth(width - 25);
	doLayoutIfNeeded();
    }

    public void updateRosterItem(final ChatUserUI user, final UserGridMenu menu) {
	final Record recordToUpdate = recordMap.get(user.getURI());
	final String alias = formatAlias(user);
	final String jid = formatJid(user);
	final String statusFormated = formatStatus(alias, user.getStatusText());
	recordToUpdate.set(IMG, user.getIconUrl());
	recordToUpdate.set(ALIAS, alias);
	recordToUpdate.set(JID, jid);
	recordToUpdate.set(COLOR, user.getColor());
	recordToUpdate.set(STATUSTEXT, statusFormated);
	recordToUpdate.set(STATUSIMG, formatStatusIcon(user));
	store.commitChanges();
	menuMap.put(user.getURI(), menu);
	sort();
	doLayoutIfNeeded();
    }

    private void addRecord(final AbstractChatUser user, final String statusIcon, final String statusTextOrig,
	    final UserGridMenu menu) {
	final String alias = formatAlias(user);
	final String jid = formatJid(user);
	final String statusFormated = formatStatus(alias, statusTextOrig);
	final Record newUserRecord = recordDef.createRecord(new Object[] { user.getIconUrl(), jid, alias,
		user.getColor(), statusIcon, statusFormated });
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
	    @Override
	    public boolean notifyDrop(final DragSource src, final EventObject e, final DragData dragData) {
		if (dragData instanceof GridDragData) {
		    final GridDragData gridDragData = (GridDragData) dragData;
		    final Record[] records = gridDragData.getSelections();
		    for (Record record : records) {
			dropGridConfiguration.getListener().onDrop(XmppURI.jid(record.getAsString(JID)));
		    }
		}
		return true;
	    }

	    @Override
	    public String notifyEnter(final DragSource src, final EventObject e, final DragData data) {
		return "x-tree-drop-ok-append";
	    }

	    @Override
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
	store.setDefaultSort(ALIAS, SortDir.ASC);
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
		return Format.format("<img src=\"{0}\" style=\"width: 16px; height: 16px;\">", new String[] { record
			.getAsString(IMG) });
	    }
	};
	final Renderer userAliasRender = new Renderer() {
	    public String render(final Object value, final CellMetadata cellMetadata, final Record record,
		    final int rowIndex, final int colNum, final Store store) {
		return Format
			.format(
				"<span ext:qtip=\"<b>{3}</b><br/>{2}\" style=\"vertical-align: middle; color: {0} ;\">{1}</span>",
				new String[] {
					record.getAsString(COLOR),
					record.getAsString(ALIAS),
					record.getAsString(STATUSTEXT).equals("null") ? " " : TextUtils.escape(record
						.getAsString(STATUSTEXT)), record.getAsString(JID) });
	    }
	};
	final Renderer userStatusRender = new Renderer() {
	    public String render(final Object value, final CellMetadata cellMetadata, final Record record,
		    final int rowIndex, final int colNum, final Store store) {
		return Format.format("{0}", new String[] { record.getAsString(STATUSIMG) });
	    }
	};
	final ColumnConfig[] columnsConfigs = new ColumnConfig[] {
		new ColumnConfig("Image", IMG, 24, false, iconRender, IMG),
		new ColumnConfig("Alias", ALIAS, 24, true, userAliasRender, ALIAS),
		new ColumnConfig("Status", STATUSIMG, 24, true, userStatusRender, STATUSIMG) };
	final ColumnModel columnModel = new ColumnModel(columnsConfigs);
	grid.setColumnModel(columnModel);
	grid.setAutoExpandColumn(ALIAS);
	// grid.setAutoExpandMax(81);
	grid.setSelectionModel(new RowSelectionModel());
	grid.addGridCellListener(new GridCellListenerAdapter() {
	    @Override
	    public void onCellDblClick(final GridPanel grid, final int rowIndex, final int colIndex, final EventObject e) {
		onDoubleClick(rowIndex);
	    }
	});
	grid.addGridRowListener(new GridRowListener() {
	    public void onRowClick(final GridPanel grid, final int rowIndex, final EventObject e) {
		showMenu(rowIndex, e);
	    }

	    public void onRowContextMenu(final GridPanel grid, final int rowIndex, final EventObject e) {
		showMenu(rowIndex, e);
	    }

	    public void onRowDblClick(final GridPanel grid, final int rowIndex, final EventObject e) {
		Log.debug("Row double click");
		onDoubleClick(rowIndex);
	    }

	    private void showMenu(final int rowIndex, final EventObject e) {
		final Record record = store.getRecordAt(rowIndex);
		final String jid = record.getAsString(JID);
		final UserGridMenu menu = menuMap.get(uri(jid));
		menu.showMenu(e);
	    }
	});
	grid.stripeRows(true);
	final GridView view = new GridView();
	// i18n
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
	// http://groups.google.com/group/gwt-ext/browse_thread/thread/6d
	// ef43c434147596
	// /69e487525d4c68e9?hl=en&lnk=gst&q=dolayout#69e487525d4c68e9
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

    private String formatStatus(final String userAlias, final String statusText) {
	// ext don't like ""
	final String statusFormated = statusText != null ? "<br/>" + statusText : "";
	return userAlias + statusFormated;
    }

    private String formatStatusIcon(final ChatUserUI user) {
	final AbstractImagePrototype icon = ChatUIUtils.getIcon(user.getStatusIcon());
	final Image iconImg = new Image();
	icon.applyTo(iconImg);
	// Don't works:
	// final ToolTip tooltip = new ToolTip();
	// tooltip.setHtml("foo foo foo <b>bold</b> foo foo.");
	// tooltip.setWidth(150);
	// tooltip.applyTo(iconImg.getElement());
	return iconImg.toString();
    }

    private void onDoubleClick(final int rowIndex) {
	Log.info("Double click in user");
	final Record record = store.getRecordAt(rowIndex);
	final String jid = record.getAsString(JID);
	listener.onDoubleClick(uri(jid));
    }

    private void sort() {
	// store.sort(ALIAS);
    }
}
