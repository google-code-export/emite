#!/bin/bash

rm -rf src/main/webapp/gwt/com.calclab.examplechat.Example/
if [ ! -d src/main/webapp/gwt ]
then
  mkdir src/main/webapp/gwt
fi
cp -R target/emite-0.0.1/com.calclab.examplechat.Example/ src/main/webapp/gwt/com.calclab.examplechat.Example/
mvn jetty:run
