#!/bin/bash

if [ ! -d target/emite-0.0.1/com.calclab.examplechat.Example]
then 
	rm -rf target/emite-0.0.1/com.calclab.examplechat.Example
fi

mvn gwt:compile
