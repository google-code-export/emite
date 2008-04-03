package com.calclab.examplechat.client.chatuiplugin.users;

import java.util.HashMap;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatUser;
import com.calclab.examplechat.client.chatuiplugin.dialog.StatusUtil;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.util.Format;
import com.gwtext.client.widgets.grid.CellMetadata;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.ColumnModel;
import com.gwtext.client.widgets.grid.GridPanel;
import com.gwtext.client.widgets.grid.GridView;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.grid.event.GridRowListener;

public class UserGrid extends GridPanel {

	private static final String ALIAS = "alias";
	private static final String COLOR = "color";
	private static final String IMG = "img";
	private static final String JID = "jid";
	private static final String STATUSIMG = "status";
	private static final String STATUSTEXT = "statustext";
	private FieldDef[] fieldDefs;
	private final HashMap<XmppURI, UserGridMenu> menuMap;
	private RecordDef recordDef;
	private Store store;

	public UserGrid() {
		createGrid();
		menuMap = new HashMap<XmppURI, UserGridMenu>();
	}

	public void addUser(final GroupChatUser user) {
		final String img = user.getUserType().equals(GroupChatUser.MODERADOR) ? "images/moderatoruser.gif"
				: "images/normaluser.gif";
		addUser(user, "<img src=\"" + img + "\">", "FIXME");
	}

	public void addUser(final PairChatUser user, final UserGridMenu menu) {
		final AbstractImagePrototype statusAbsIcon = StatusUtil.getStatusIcon(user.getPresence());
		// Image img = new Image();
		// statusAbsIcon.applyTo(img);
		// KuneUiUtils.setQuickTip(img.getElement(), user.getStatusText());

		final String statusIcon = statusAbsIcon.getHTML();
		addUser(user, statusIcon, user.getPresence().getStatus());
		menuMap.put(user.getJid(), menu);
	}

	public void removeUser(final AbstractChatUser user) {
		final Record newUserRecord = recordDef.createRecord(new Object[] { "", user.getJid(), user.getAlias(),
				user.getColor(), "" });
		store.remove(newUserRecord);
		menuMap.remove(user.getJid());
	}

	private void addUser(final AbstractChatUser user, final String statusIcon, final String statusText) {
		final Record newUserRecord = recordDef.createRecord(new Object[] { user.getIconUrl(), user.getJid(),
				user.getAlias(), user.getColor(), statusIcon, statusText });
		store.add(newUserRecord);
	}

	private void createGrid() {
		fieldDefs = new FieldDef[] { new StringFieldDef(IMG), new StringFieldDef(JID), new StringFieldDef(ALIAS),
				new StringFieldDef(COLOR), new StringFieldDef(STATUSIMG), new StringFieldDef(STATUSTEXT) };
		recordDef = new RecordDef(fieldDefs);

		final MemoryProxy proxy = new MemoryProxy(new Object[][] {});

		final ArrayReader reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		store.load();
		this.setStore(store);

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
				new ColumnConfig("Alias", ALIAS, 120, true, userAliasRender, ALIAS) };
		final ColumnModel columnModel = new ColumnModel(columnsConfigs);
		this.setColumnModel(columnModel);

		this.addGridRowListener(new GridRowListener() {

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
				final UserGridMenu menu = menuMap.get(XmppURI.parse(jid));
				menu.showMenu(e);
			}

		});

		// this.setAutoExpandColumn(ALIAS);
		this.stripeRows(true);
		final GridView view = new GridView();
		// i18n
		view.setEmptyText("Nobody");
		// view.setAutoFill(true);
		this.setView(view);
		this.setHideColumnHeader(true);
		this.setBorder(false);
		// countriesGrid.setEnableDragDrop(true);
		// countriesGrid.setDdGroup("myDDGroup");
	}

	// public void addList() {
	// Use this to set a list of Users
	// Object[][] data = new Object[rows][cols];
	//
	// RecordDef recordDef = new RecordDef(fields);
	// ColumnModel columnModel = new ColumnModel(columns);
	//
	//
	//
	// MemoryProxy proxy = new MemoryProxy(data);
	// ArrayReader reader = new ArrayReader(recordDef);
	// store = new Store(proxy, reader);
	// store.load();
	//
	// this.reconfigure(store, columnModel);
	// }
	//
	// }

}
