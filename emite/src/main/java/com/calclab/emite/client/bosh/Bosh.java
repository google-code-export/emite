package com.calclab.emite.client.bosh;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Globals;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.connector.ConnectorCallback;
import com.calclab.emite.client.connector.ConnectorException;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.BasicPacket;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.XMLService;
import com.calclab.emite.client.plugin.dsl.BussinessLogic;

public class Bosh implements Connection {

    public final BussinessLogic stop;
    final BussinessLogic restartStream;
    final BussinessLogic sendCreation;
    private Packet body;
    private final XMLService xmler;
    private final Connector connector;
    private final String httpBase;
    private final Dispatcher dispatcher;
    private boolean isRunning;
    // FIXME: gwt and long problems
    private long rid;

    public Bosh(final Dispatcher dispatcher, final Globals globals, final Connector connector, final XMLService xmler,
            final BoshOptions options) {
        this.dispatcher = dispatcher;
        this.connector = connector;
        this.xmler = xmler;
        this.httpBase = options.getHttpBase();
        this.body = null;
        this.isRunning = false;
        this.rid = 0;

        globals.setDomain(options.getDomain());

        restartStream = new BussinessLogic() {
            public Packet logic(final Packet received) {
                setRestart();
                return null;
            }
        };

        sendCreation = new BussinessLogic() {
            public Packet logic(final Packet received) {
                isRunning = true;
                rid = generateRID();
                body.With("content", "text/xml; charset=utf-8").With("rid", rid).With("to", options.getDomain()).With(
                        "secure", "true").With("ver", "1.6").With("wait", "60").With("ack", "1").With("hold", "1")
                        .With("xml:lang", "en");
                return null;
            }
        };

        stop = new BussinessLogic() {
            public Packet logic(final Packet received) {
                stop();
                return null;
            }
        };

    }

    public void catchPackets() {
        rid++;
        this.body = new BasicPacket("body", "http://jabber.org/protocol/httpbind").With("rid", rid);
    }

    public void firePackets() {
        try {
            connector.send(httpBase, xmler.toString(body), new ConnectorCallback() {
                public void onError(final Throwable throwable) {
                    dispatcher.publish(Connection.Events.error);
                }

                public void onResponseReceived(final int statusCode, final String content) {
                    final Packet response = xmler.toXML(content);
                    dispatcher.publish(response);
                }

            });
        } catch (final ConnectorException e) {
            dispatcher.publish(Connection.Events.error);
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void send(final Packet toBeSend) {
        Log.debug("BOSH::Send: " + toBeSend);
        body.addChild(toBeSend);
    }

    public void setRestart() {
        body.setAttribute("restart", "true");
    }

    public void setSID(final String sid) {
        body.setAttribute("sid", sid);
    }

    public void stop() {
        isRunning = false;
    }

    private long generateRID() {
        final long rid = (long) (Math.random() * 1245234);
        return rid;
    }

}
