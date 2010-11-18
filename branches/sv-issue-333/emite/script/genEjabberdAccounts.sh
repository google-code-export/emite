#!/bin/bash

NUM=0

while [ $NUM -ne 101 ]
do
echo ejabberdctl register test${NUM} emitedemo.ourproject.org test${NUM}
NUM=$(( $NUM + 1 ))
done

for NAME in dani antonio jef roberto vlad samer vicente
do
echo ejabberdctl register ${NAME} emitedemo.ourproject.org ${NAME}
done
