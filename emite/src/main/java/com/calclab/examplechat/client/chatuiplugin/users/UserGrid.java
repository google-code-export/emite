package com.calclab.examplechat.client.chatuiplugin.users;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatUser;
import com.calclab.examplechat.client.chatuiplugin.dialog.StatusUtil;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
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

public class UserGrid extends GridPanel {

    private static final String JID = "jid";
    private static final String ALIAS = "alias";
    private static final String IMG = "img";
    private static final String COLOR = "color";
    private static final String STATUSIMG = "status";
    private Store store;
    private FieldDef[] fieldDefs;
    private RecordDef recordDef;

    public UserGrid() {
        createGrid();
    }

    private void createGrid() {
        fieldDefs = new FieldDef[] { new StringFieldDef(IMG), new StringFieldDef(JID), new StringFieldDef(ALIAS),
                new StringFieldDef(COLOR), new StringFieldDef(STATUSIMG) };
        recordDef = new RecordDef(fieldDefs);

        MemoryProxy proxy = new MemoryProxy(new Object[][] {});

        ArrayReader reader = new ArrayReader(recordDef);
        store = new Store(proxy, reader);
        store.load();
        this.setStore(store);

        Renderer iconRender = new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
                return Format.format("<img src=\"{0}\">", new String[] { record.getAsString(IMG) });
            }
        };
        Renderer userAliasRender = new Renderer() {
            public String render(Object value, CellMetadata cellMetadata, Record record, int rowIndex, int colNum,
                    Store store) {
                return Format.format("<span style=\"vertical-align: middle; color: {0} ;\">{1}&nbsp;</span>{2}",
                        new String[] { record.getAsString(COLOR), record.getAsString(ALIAS),
                                record.getAsString(STATUSIMG) });
            }
        };
        ColumnConfig[] columnsConfigs = new ColumnConfig[] {
                new ColumnConfig("Image", IMG, 24, false, iconRender, IMG),
                new ColumnConfig("Alias", ALIAS, 115, true, userAliasRender, ALIAS) };
        ColumnModel columnModel = new ColumnModel(columnsConfigs);
        this.setColumnModel(columnModel);

        // this.setAutoExpandColumn(ALIAS);
        this.stripeRows(true);
        GridView view = new GridView();
        // i18n
        view.setEmptyText("Nobody");
        // view.setAutoFill(true);
        this.setView(view);
        this.setHideColumnHeader(true);
        this.setBorder(false);
        // countriesGrid.setEnableDragDrop(true);
        // countriesGrid.setDdGroup("myDDGroup");
    }

    public void addUser(final PairChatUser user) {
        String statusIcon = StatusUtil.getStatusIcon(user.getStatus()).getHTML();
        addUser(user, statusIcon);
    }

    public void addUser(final GroupChatUser user) {
        String img = user.getUserType().equals(GroupChatUser.MODERADOR) ? "images/moderatoruser.gif"
                : "images/normaluser.gif";
        addUser(user, "<img src=\"" + img + "\">");
    }

    private void addUser(final AbstractChatUser user, final String statusIcon) {
        Record newUserRecord = recordDef.createRecord(new Object[] { user.getIconUrl(), user.getJid(), user.getAlias(),
                user.getColor(), statusIcon });
        store.add(newUserRecord);
    }

    public void addList() {

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
    }

    public void removeUser(final AbstractChatUser user) {
        Record newUserRecord = recordDef.createRecord(new Object[] { "", user.getJid(), user.getAlias(),
                user.getColor(), "" });
        store.remove(newUserRecord);
        store.load();
    }
}
