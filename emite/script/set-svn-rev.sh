#!/bin/bash
REV=`svn info --xml| grep -m 1 revision | cut -d \" -f 2`
perl -p -i -e "s/gwt_property_ver\" content=\".*\"/gwt_property_ver\" content=\"r$REV\"/gi" src/main/java/com/calclab/emiteui/public/EmiteUI.html

