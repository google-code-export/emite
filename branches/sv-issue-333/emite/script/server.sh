#!/bin/bash

./script/compile.sh
./script/clean_deploy.sh
./script/deploy_copy.sh

mvn jetty:run
