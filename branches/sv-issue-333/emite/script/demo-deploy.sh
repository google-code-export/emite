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

chmod -R g+rw target/emite-0.4.6/com.calclab.emiteui.EmiteUI/
# from time to time use --delete
rsync --delete --progress -C -r -p target/emite-0.4.6/com.calclab.emiteui.EmiteUI/ ${EXTRA}ourproject.org:/home/groups/kune/htdocs/emitedemo/$EXTRADEST
rsync --progress -C -r -p target/emite-0.4.6/com.calclab.emiteui.EmiteUI/EmiteUIDemo.html  ${EXTRA}ourproject.org:/home/groups/kune/htdocs/emitedemo/${EXTRADEST}index.html

ssh ${EXTRA}ourproject.org "chgrp -R kune /home/groups/kune/htdocs/emitedemo/"
