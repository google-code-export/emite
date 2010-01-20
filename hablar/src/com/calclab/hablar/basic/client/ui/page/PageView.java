package com.calclab.hablar.basic.client.ui.page;

import com.calclab.suco.client.events.Listener;

public interface PageView {

    public static enum Visibility {
	open, closed, hidden
    }

    PageHeader getHeader();

    /**
     * Get the page type (Roster, Chat...)
     * 
     * @return
     */
    String getPageType();

    String getStatusMessage();

    /**
     * Ask the page about it's current visibility state
     * 
     * @param visibility
     */
    Visibility getVisibility();

    /**
     * Add a listener to onClose events. This listener is called when the close
     * button in the header is clicked
     * 
     * @param closeListener
     */
    void onClose(Listener<PageView> closeListener);

    /**
     * Add a listener to know when the status message of this page has changed
     * 
     * @param statusListener
     */
    void onStatusMessageChanged(Listener<PageView> statusListener);

    /**
     * Add a listener to know when this page is open
     * 
     * @param openListener
     */
    void onVisibilityChanged(Listener<PageView> openListener);

    /**
     * Change the icon class of the header
     * 
     * @param iconStyle
     */
    void setHeaderIconClass(String iconStyle);

    /**
     * Changes the header title of this page
     * 
     * @param title
     *            the new title
     */
    void setHeaderTitle(String title);

    /**
     * Change the status message of this page
     * 
     * @param status
     */
    void setStatusMessage(String status);

    /**
     * Inform the page about it's current visibility state
     * 
     * @param visibility
     */
    void setVisibility(Visibility visibility);

}