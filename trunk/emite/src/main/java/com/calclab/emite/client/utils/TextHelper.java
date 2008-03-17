package com.calclab.emite.client.utils;

public class TextHelper {

	public static final String template(String template, final Object... params) {
		for (int index = 0; index < params.length; index++) {
			template = template.replaceFirst("\\{" + index + "\\}", params[index].toString());
		}
		return template;
	}

}
