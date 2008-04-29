#!/bin/bash
REV=`svn info --xml| grep -m 1 revision | cut -d \" -f 2`
COMMITSPENDING=`svn status | grep -c ""`
perl -p -i -e "s/(<title>Emite UI application )(.*)(<\/title>)/\$1r$REV+c$COMMITSPENDING \(0.2.0-alpha\)\$3/gi" src/main/java/com/calclab/emiteui/public/EmiteUI.html
perl -p -i -e "s/(<title>Emite UI application )(.*)(<\/title>)/\$1r$REV+c$COMMITSPENDING \(0.2.0-alpha\)\$3/gi" src/main/java/com/calclab/emiteui/public/EmiteUIDemo.html
perl -p -i -e "s/(gwt_property_release\" content=\")(0\.2\.0)/\$1r$REV\-c$COMMITSPENDING/gi" src/main/java/com/calclab/emiteui/public/EmiteUI.html
perl -p -i -e "s/(gwt_property_release\" content=\")(0\.2\.0)/\$1r$REV\-c$COMMITSPENDING/gi" src/main/java/com/calclab/emiteui/public/EmiteUIDemo.html
