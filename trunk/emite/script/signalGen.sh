#!/bin/bash
NAMES=$*
PARAMS=$#

# CORRECT PARAMS ###############################################################

if [ $PARAMS -lt 1 ]
then
  echo "Use: $0 signalName1.ParamType signalName2.ParamType ... signalNameN.ParamType"
  echo "$0 onEvent1.String onEvent2.Long onEvent3.String"
  exit
fi

echo ""

for NAME in $NAMES
do
  SIGNALNAME=${NAME/%\.*/}
  SIGNALPARAM=${NAME/#*./}
  echo "private final Signal<$SIGNALPARAM> $SIGNALNAME;"
  echo "public void $SIGNALNAME(final Slot<$SIGNALPARAM> slot) {
  	$SIGNALNAME.add(slot);
      }"
  echo ""
  echo "this.$SIGNALNAME = new Signal<$SIGNALPARAM>(\"$SIGNALNAME\");"
  echo ""
done
