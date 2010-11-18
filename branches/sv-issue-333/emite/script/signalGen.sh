#!/bin/bash
NAMES=$*
PARAMS=$#

# CORRECT PARAMS ###############################################################

if [ $PARAMS -lt 1 ]
then
  echo "Use: $0 signalName1.ParamType signalName2.ParamType ... signalNameN.ParamType"
  echo "$0 onEvent1.String onEvent2.Long onEvent3.String"
  echo "$0 onEvent1. onEvent2. onEvent3. # for Event0/Listener0"
  exit
fi

echo ""

for NAME in $NAMES
do
  EVENTNAME=${NAME/%\.*/}
  EVENTPARAM=${NAME/#*./}

  if [[ -n $EVENTPARAM ]]
  then
    EVENTPARAM="<$EVENTPARAM>"
  else
    EVENTPARAM="0"
  fi

  echo "private final Event$EVENTPARAM $EVENTNAME;"
  echo "public void $EVENTNAME(final Listener$EVENTPARAM listener) {
  	$EVENTNAME.add(listener);
      }"
  echo ""
  echo "this.$EVENTNAME = new Event$EVENTPARAM(\"$EVENTNAME\");"
  echo ""
done
