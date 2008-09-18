/**
 * 
 */
package com.calclab.emite.j2se.swing.login;

public class LoginParams {
    public final String httpBase;
    public final String domain;
    public final String userName;
    public final String password;

    public LoginParams(final String httpBase, final String domain, final String userName, final String password) {
        this.httpBase = httpBase;
        this.domain = domain;
        this.userName = userName;
        this.password = password;
    }
}