#!/bin/bash
for i in `./script/findFilesWithoutCopyright.sh` ;do cp COPYRIGHT-LGPLv3.java /tmp/copy; cat $i >> /tmp/copy ; mv /tmp/copy $i ; done
