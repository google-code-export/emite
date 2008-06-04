#!/bin/bash
NAMES=$*
PARAMS=$#

# CORRECT PARAMS ###############################################################

if [ $PARAMS -lt 1 ]
then
  echo "Use: $0 signalName1 signalName2 ... signalNameN"
  echo "$0 onEvent1 onEvent2 onEvent3"
  exit
fi

echo ""

for NAME in $NAMES
do
  echo "private final Signal<Message> $NAME"
  echo "this.$NAME = new Signal<Message>(\"$NAME\")"
  echo "public void $NAME(final Slot<Message> slot) {
  	$NAME.add(slot);
      }"
  echo ""
done