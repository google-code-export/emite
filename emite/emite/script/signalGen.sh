#!/bin/bash
NAMES=$*
PARAMS=$#

# CORRECT PARAMS ###############################################################

if [ $PARAMS -lt 1 ]
then
  echo "Use: $0 signalName1.ParamType signalName2.ParamType ... signalNameN.ParamType"
  echo "$0 onEvent1.String onEvent2.Long onEvent3.String"
  echo "$0 onEvent1. onEvent2. onEvent3. # for Signal0/Slot0"
  exit
fi

echo ""

for NAME in $NAMES
do
  SIGNALNAME=${NAME/%\.*/}
  SIGNALPARAM=${NAME/#*./}

  if [[ -n $SIGNALPARAM ]]
  then
    SIGNALPARAM="<$SIGNALPARAM>"
  else
    SIGNALPARAM="0"
  fi

  echo "private final Signal$SIGNALPARAM $SIGNALNAME;"
  echo "public void $SIGNALNAME(final Slot$SIGNALPARAM slot) {
  	$SIGNALNAME.add(slot);
      }"
  echo ""
  echo "this.$SIGNALNAME = new Signal$SIGNALPARAM(\"$SIGNALNAME\");"
  echo ""
done
