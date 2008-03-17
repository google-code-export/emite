#!/bin/bash

rm src/main/webapp/gwt/com.calclab.examplechat.Example/ -R
cp target/emite-0.0.1/gwt/com.calclab.examplechat.Example/ src/main/webapp/gwt/ -R
mvn jetty:run -o