#!/bin/bash

if [ -d src/main/webapp/gwt/com.calclab.emiteui.EmiteUI ]
then
  rm -rf src/main/webapp/gwt/com.calclab.emiteui.EmiteUI/*
fi

if [ ! -d src/main/webapp/gwt ]
then
	mkdir src/main/webapp/gwt
fi


cp -a target/emite-0.0.1/com.calclab.emiteui.EmiteUI/ src/main/webapp/gwt/
mvn jetty:run
