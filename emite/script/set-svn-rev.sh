#!/bin/bash
REV=`svn info --xml| grep -m 1 revision | cut -d \" -f 2`
COMMITSPENDING=`svn status | grep -c ""`
perl -p -i -e "s/(<title>Emite UI application )(.*)(<\/title>)/\$1r$REV+c$COMMITSPENDING \(inestable\)\$3/gi" src/main/java/com/calclab/emiteui/public/EmiteUI.html

