package com.calclab.emite.widgets.client.base;

public interface EmiteWidget {
    public static final String[] EMPTY_PARAMS = new String[0];

    public String[] getParamNames();

    public void setParam(String name, String value);

}
