package com.calclab.emite.functionaltester.client.tests;

import com.calclab.emite.functionaltester.client.Context;
import com.calclab.emite.functionaltester.client.FunctionalTest;
import com.calclab.emite.xep.vcard.client.VCardManager;
import com.calclab.emite.xep.vcard.client.VCardResponse;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class VCardTestSuite extends BasicTestSuite {

    private final FunctionalTest requestVCard = new FunctionalTest() {

        @Override
        public void run(final Context ctx) {
            ctx.info("Starting request vcard test");
            manager = Suco.get(VCardManager.class);
            manager.requestOwnVCard(new Listener<VCardResponse>() {
                @Override
                public void onEvent(final VCardResponse response) {
                    ctx.success("Discovery features received");
                    ctx.info("Response: " + response.getType().toString());
                    ctx.info("Response hasVCard: " + response.hasVCard());
                    ctx.info("Response isError: " + response.isError());
                    ctx.info("Response isSuccess: " + response.isSuccess());
                    ctx.getSession().logout();
                }
            });
        }
    };
    private VCardManager manager;

    @Override
    public void registerTests() {
        add("Request vcard", requestVCard);
    }

}
