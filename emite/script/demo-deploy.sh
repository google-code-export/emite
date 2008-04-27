#!/bin/bash
PARAM=$#
USER=$1
if [ $PARAM -eq 1 ]
then
  EXTRA=$USER@
fi

chmod -R g+rw target/emite-0.2.0/com.calclab.emiteui.EmiteUI/std/
rsync --progress -C -r -p target/emite-0.2.0/com.calclab.emiteui.EmiteUI/std/ ${EXTRA}ourproject.org:/home/groups/kune/htdocs/emitedemo/
rsync --progress -C -r -p target/emite-0.2.0/com.calclab.emiteui.EmiteUI/std/EmiteUIDemo.html  ${EXTRA}ourproject.org:/home/groups/kune/htdocs/emitedemo/EmiteUI.html
