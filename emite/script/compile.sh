#!/bin/bash

if [ -d target/emite-0.4.4/com.calclab.emiteui.EmiteUI ]
then
  rm -rf target/emite-0.4.4/com.calclab.emiteui.EmiteUI
fi

mvn gwt:compile -P emiteui
