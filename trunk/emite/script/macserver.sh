#!/bin/bash

rm -rf src/main/webapp/gwt/com.calclab.emiteui.EmiteUI/



if [ ! -d src/main/webapp/gwt ]
then
  mkdir src/main/webapp/gwt
fi

cp -R target/emite-0.2.0/com.calclab.emiteui.EmiteUI/std src/main/webapp/gwt/com.calclab.emiteui.EmiteUI/
cp -R target/emite-0.2.0/com.calclab.emite.examples.chat.Chat/std src/main/webapp/gwt/com.calclab.emite.examples.chat.Chat/

mvn jetty:run
