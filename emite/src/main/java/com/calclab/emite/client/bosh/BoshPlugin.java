package com.calclab.emite.client.bosh;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.packet.XMLService;
import com.calclab.emite.client.plugin.PublisherPlugin;
import com.calclab.emite.client.x.core.SASLPlugin;

public class BoshPlugin extends PublisherPlugin {
    private final Connector connector;
    private final XMLService xmler;
    private final BoshOptions options;
    private Bosh bosh;
    private StreamManager stream;

    public BoshPlugin(final Connector connector, final XMLService xmler, final BoshOptions options) {
        this.connector = connector;
        this.xmler = xmler;
        this.options = options;
    }

    @Override
    public void attach() {
        when.Event(SASLPlugin.Events.authorized).Do(bosh.restartStream);
        when.Event(Connection.Events.start).Do(bosh.sendCreation);
        when.Event(Connection.Events.error).Do(bosh.stop);

        when.Packet("body").Do(stream.publishStanzas);
        when.Event(Connection.Events.error).Do(stream.clear);
    }

    @Override
    public void install() {
        final Dispatcher dispatcher = getDispatcher();
        bosh = new Bosh(dispatcher, getGlobals(), connector, xmler, options);
        stream = new StreamManager(bosh, dispatcher);

        register(Components.CONNECTION, bosh);
        dispatcher.addListener(new DispatcherStateListener() {
            public void afterDispatching() {
                if (bosh.isRunning()) {
                    bosh.firePackets();
                }
            }

            public void beforeDispatching() {
                bosh.catchPackets();
            }
        });

    }

}
