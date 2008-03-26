/**
 * 
 */
package com.calclab.emite.client.plugin.dsl;

import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.matcher.BasicMatcher;
import com.calclab.emite.client.packet.Packet;

public class ActionBuilder {
    /**
     * 
     */
    private final FilterBuilder actionBuilder;

    /**
     * @param filterBuilder
     */
    ActionBuilder(final FilterBuilder filterBuilder) {
        actionBuilder = filterBuilder;
    }

    public void Do(final Action action) {
        actionBuilder.getDispatcher().subscribe(actionBuilder.filter, action);
    }

    public void publish(final Packet packet) {
        final PublishAction action = new PublishAction(actionBuilder.getDispatcher(), packet);
        actionBuilder.getDispatcher().subscribe(actionBuilder.filter, action);
    }

    public void Publish(final Answer bussinessLogic) {
        final PublishAction action = new PublishAction(actionBuilder.getDispatcher(), bussinessLogic);
        actionBuilder.getDispatcher().subscribe(actionBuilder.filter, action);
    }

    public void Send(final Packet packet) {
        final SendAction action = new SendAction(actionBuilder.getConnection(), packet);
        actionBuilder.getDispatcher().subscribe(actionBuilder.filter, action);
    }

    public void Send(final Answer logic) {
        final SendAction action = new SendAction(actionBuilder.getConnection(), logic);
        actionBuilder.getDispatcher().subscribe(actionBuilder.filter, action);
    }

    BasicMatcher getCurrentFilter() {
        return actionBuilder.filter;
    }
}