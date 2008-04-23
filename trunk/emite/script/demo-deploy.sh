#!/bin/bash
PARAM=$#
USER=$1
if [ $PARAM -eq 1 ]
then
  EXTRA=$USER
fi
rsync --no-g --progress -C -r target/emite-0.2.0/com.calclab.emiteui.EmiteUI/std/ ${EXTRA}@ourproject.org:/home/groups/kune/htdocs/emitedemo/
