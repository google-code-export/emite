#!/bin/bash

# For Import/Export User Openfire plugin
# http://localhost:9090/plugin-admin.jsp?plugin=userimportexport&showReadme=true&decorator=none

cat <<EOF
<?xml version="1.0" encoding="UTF-8"?>

<Openfire>
EOF

NUM=0

while [ $NUM -ne 101 ]
do
cat <<EOF
<User>
  <Username>test${NUM}</Username>
  <Password>test${NUM}</Password>
  <Email>test${NUM}@example.com</Email>
  <Name>test user ${NUM}</Name>
  <CreationDate>1125442154664</CreationDate>
  <ModifiedDate>1125442154664</ModifiedDate>
  <Roster>
  </Roster>
</User>

EOF
NUM=$(( $NUM + 1 ))
done

for NAME in dani antonio jef roberto vlad samer vicente
do
cat <<EOF
<User>
  <Username>${NAME}</Username>
  <Password>${NAME}</Password>
  <Email>test${NAME}@example.com</Email>
  <Name>test user ${NAME}</Name>
  <CreationDate>1125442154664</CreationDate>
  <ModifiedDate>1125442154664</ModifiedDate>
  <Roster>
  </Roster>
</User>

EOF
done

echo "</Openfire>"
