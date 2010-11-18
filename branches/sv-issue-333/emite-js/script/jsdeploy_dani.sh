#!/bin/bash

rm -r /var/www/emitejs/*
mkdir /var/www/emitejs/lib
cp target/emite-js-0.4.0/com.calclab.emite.js.EmiteJS/* /var/www/emitejs/lib
cp src/samples/* /var/www/emitejs
