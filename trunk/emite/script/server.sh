#!/bin/bash

rm src/main/webapp/gwt/com.calclab.xmpptest.bosh.TestBosh/ -R
cp target/transa-0.0.1/gwt/com.calclab.xmpptest.bosh.TestBosh/ src/main/webapp/gwt/ -R
mvn jetty:run -o