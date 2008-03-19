#!/bin/bash

if [ -d src/main/webapp/gwt/com.calclab.examplechat.Example ]
then
  rm -rf src/main/webapp/gwt/com.calclab.examplechat.Example/
fi
cp -a target/emite-0.0.1/com.calclab.examplechat.Example/ src/main/webapp/gwt/
mvn jetty:run
