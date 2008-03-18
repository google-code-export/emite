package com.calclab.emite.client.x.im.session;

public class SessionOptions {
	private final String userName, password;

	public SessionOptions(final String userName, final String password) {
		this.userName = userName;
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public String getUserName() {
		return userName;
	}

}
