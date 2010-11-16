package com.calclab.hablar.vcard.client;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.XmppRoster;
import com.calclab.emite.im.client.roster.events.RosterItemChangedEvent;
import com.calclab.emite.im.client.roster.events.RosterItemChangedHandler;
import com.calclab.emite.xep.vcard.client.VCard;
import com.calclab.emite.xep.vcard.client.VCardManager;
import com.calclab.emite.xep.vcard.client.VCardResponse;
import com.calclab.hablar.core.client.Hablar;
import com.calclab.hablar.core.client.container.PageAddedEvent;
import com.calclab.hablar.core.client.container.PageAddedHandler;
import com.calclab.hablar.core.client.container.overlay.OverlayContainer;
import com.calclab.hablar.core.client.page.PagePresenter.Visibility;
import com.calclab.hablar.core.client.ui.menu.Action;
import com.calclab.hablar.core.client.ui.menu.SimpleAction;
import com.calclab.hablar.roster.client.groups.RosterItemPresenter;
import com.calclab.hablar.roster.client.page.RosterPage;
import com.calclab.hablar.roster.client.page.RosterPresenter;
import com.calclab.hablar.user.client.UserContainer;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class HablarVCard implements EntryPoint {

	private static VCardMessages messages;

	public static void setMessages(final VCardMessages messages) {
		HablarVCard.messages = messages;
	}

	public static VCardMessages i18n() {
		return messages;
	}

	private static final String ACTION_ID_VIEW_VCARD = "HablarVCard-seeVCardAction";

	public static void install(final Hablar hablar, VCardConfig vCardConfig) {
		VCardWidget vCardWidget = new VCardWidget(vCardConfig.vCardReadOnly,
				"OwnVCardWidget");
		final OwnVCardPresenter ownVCardPage = new OwnVCardPresenter(hablar
				.getEventBus(), vCardWidget);
		hablar.addPage(ownVCardPage, UserContainer.ROL);

		final OthersVCardPresenter othersVCardPage = new OthersVCardPresenter(
				hablar.getEventBus(), new OtherVCardWidget(true));
		hablar.addPage(othersVCardPage, OverlayContainer.ROL);

		hablar.addPageAddedHandler(new PageAddedHandler() {
			@Override
			public void onPageAdded(final PageAddedEvent event) {
				final RosterPage rosterPage = RosterPresenter.asRoster(event
						.getPage());
				if (rosterPage != null) {
					rosterPage.getItemMenu().addAction(
							createViewVCardAction(othersVCardPage));
				}
			}
		}, true);

		prepareDefaultNicknameListener();
	}

	protected static Action<RosterItemPresenter> createViewVCardAction(
			final OthersVCardPresenter othersVCardPage) {
		return new SimpleAction<RosterItemPresenter>(i18n()
				.seeUserProfileAction(), ACTION_ID_VIEW_VCARD) {
			@Override
			public void execute(final RosterItemPresenter target) {
				othersVCardPage.setUser(target.getItem().getJID());
				othersVCardPage.requestVisibility(Visibility.focused);
			}
		};
	}

	@Override
	public void onModuleLoad() {
		final VCardMessages messages = GWT.create(VCardMessages.class);
		I18nVCard.set(messages);
		VCardWidget.setMessages(messages);
		HablarVCard.setMessages(messages);
	}

	private static void prepareDefaultNicknameListener() {
		final XmppRoster roster = Suco.get(XmppRoster.class);

		roster.addRosterItemChangedHandler(new RosterItemChangedHandler() {

			@Override
			public void onRosterItemChanged(RosterItemChangedEvent event) {
				if (event.isAdded()) {
					final RosterItem rosterItem = event.getRosterItem();
					String itemName = rosterItem.getName();
					XmppURI jid = rosterItem.getJID();
					if (itemName == null || "".equals(itemName)
							|| itemName.equals(jid.getNode())) {
						final VCardManager manager = Suco
								.get(VCardManager.class);
						manager.getUserVCard(jid,
								new Listener<VCardResponse>() {

									@Override
									public void onEvent(VCardResponse parameter) {
										VCard vcard = parameter.getVCard();
										if (vcard != null) {
											rosterItem.setName(vcard
													.getDisplayName());
											roster
													.requestUpdateItem(rosterItem);
										}
									}

								});
					}

				}
			}
		});
	}
}
