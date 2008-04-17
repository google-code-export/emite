#!/bin/bash

./script/clean_deploy.sh
./script/deploy_copy.sh


mvn jetty:run
