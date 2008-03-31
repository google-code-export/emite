package com.calclab.examplechat.client.chatuiplugin.users;

import com.gwtext.client.widgets.menu.Item;
import com.gwtext.client.widgets.menu.Menu;
import com.gwtext.client.widgets.menu.MenuItem;

public class UserGridMenu {
    public UserGridMenu() {
        // create the menu we want to assign to the button
        Menu menu = new Menu();

        Item wordItem = new Item("Word");
        wordItem.setIconCls("word-icon");
        menu.addItem(wordItem);

        Item excelItem = new Item("Excel");
        excelItem.setIconCls("excel-icon");
        menu.addItem(excelItem);

        // create a sub menu
        Menu subMenu = new Menu();
        Item cItem = new Item("C");
        cItem.setIconCls("c-icon");

        Item cppItem = new Item("C++");
        cppItem.setIconCls("cpp-icon");

        Item csharpItem = new Item("CSharp");
        csharpItem.setIconCls("csharp-icon");

        subMenu.addItem(cItem);
        subMenu.addItem(cppItem);
        subMenu.addItem(csharpItem);

        // add menu item that has sub menu
        MenuItem vsItem = new MenuItem("Visual Studio", subMenu);
        vsItem.setIconCls("visualstudio-icon");
        menu.addItem(vsItem);

        Item powerPointItem = new Item("PowerPoint");
        powerPointItem.setIconCls("powerpoint-icon");
        menu.addItem(powerPointItem);
    }
}
