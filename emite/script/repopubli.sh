#!/bin/bash

PARAM=$#
JAR=$1
GROUP=$2
ARTIFACT=$3
VER=$4

DESTHOST=ourproject.org
DESTREPO=/home/groups/kune/htdocs/mavenrepo 

DIRDESTREL=`echo $GROUP| sed 's/\./\//g'`

DIRDEST=$DESTREPO/$DIRDESTREL/$ARTIFACT/$VER/

ssh $DESTHOST "mkdir -p $DIRDEST"
NAME=$ARTIFACT-$VER
DESTJAR=$DIRDEST/$NAME.jar
scp $JAR $DESTHOST:$DESTJAR 
ssh $DESTHOST "md5sum $DESTJAR > $DESTJAR.md5"

POM=$DIRDEST/$NAME.pom
cat << EOF | ssh $DESTHOST "cat - > $POM"
<project>
<modelVersion>4.0.0</modelVersion>
<groupId>$GROUP</groupId>
<artifactId>$ARTIFACT</artifactId>
<version>$VER</version>
</project>
EOF

ssh $DESTHOST "md5sum $POM > $POM.md5"



