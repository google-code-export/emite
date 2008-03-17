#!/bin/bash

rm src/main/webapp/gwt/com.calclab.exmite.example.Example/ -R
cp target/emite-0.0.1/gwt/com.calclab.emite.example.Example/ src/main/webapp/gwt/ -R
mvn jetty:run -o