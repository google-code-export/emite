#!/bin/bash

mvn clean gwt:compile
rm -r /var/www/emitejs/*
mkdir /var/www/emitejs/emite-js
cp src/examples/javascript/* /var/www/emitejs/
cp target/emite-js-0.4.0/com.calclab.emite.js.EmiteJS/* /var/www/emitejs/emite-js/
sudo /etc/init.d/apache2 reload
