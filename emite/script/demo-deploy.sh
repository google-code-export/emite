#!/bin/bash
PARAM=$#
USER=$1
DEST=$2
if [ $PARAM -gt 0 ]
then
  EXTRA=$USER@
fi

if [ $PARAM -gt 1 ]
then
  EXTRADEST=$DEST/
fi

chmod -R g+rw target/emite-0.2.0/com.calclab.emiteui.EmiteUI/std/
rsync --progress -C -r -p target/emite-0.2.0/com.calclab.emiteui.EmiteUI/std/ ${EXTRA}ourproject.org:/home/groups/kune/htdocs/emitedemo/$EXTRADEST
rsync --progress -C -r -p target/emite-0.2.0/com.calclab.emiteui.EmiteUI/std/EmiteUIDemo.html  ${EXTRA}ourproject.org:/home/groups/kune/htdocs/emitedemo/${EXTRADEST}index.html
