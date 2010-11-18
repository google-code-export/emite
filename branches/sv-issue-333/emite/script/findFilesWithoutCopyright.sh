#!/bin/bash
for i in `find src/main/java/com/calclab -name '*.java'`; do egrep -q "Copyright|The emite development team" $i ; if [[ $? -ne 0 ]]; then echo $i ; fi; done
