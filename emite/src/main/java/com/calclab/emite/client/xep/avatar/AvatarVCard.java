package com.calclab.emite.client.xep.avatar;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class AvatarVCard {

    final private XmppURI from;
    private String photoHash;
    private String photo;
    private String photoType;

    public AvatarVCard(XmppURI from) {
	this(from, null, null, null);
    }

    public AvatarVCard(XmppURI from, String photoHash, String photo, String photoType) {
	this.from = from;
	this.photoHash = photoHash;
	this.photo = photo;
	this.photoType = photoType;
    }

    public XmppURI getFrom() {
	return from;
    }

    public String getPhotoHash() {
	return photoHash;
    }

    public void setPhotoHash(String photoHash) {
	this.photoHash = photoHash;
    }

    public String getPhoto() {
	return photo;
    }

    public void setPhoto(String photo) {
	this.photo = photo;
    }

    public String getPhotoType() {
	return photoType;
    }

    public void setPhotoType(String photoType) {
	this.photoType = photoType;
    }

}
