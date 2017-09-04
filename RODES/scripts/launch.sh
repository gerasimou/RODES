#! /bin/sh

#  launch.sh
#
#  Created by Simos Gerasimou on 04/09/2017.

if [ ! -d data ]; then
  mkdir data
fi


#For launching an instance only
PRISM_DIR=repo/prism

export DYLLD_LIBRARY_PATH="$PRISM_DIR":$DYLD_LIBRARY_PATH

vmArgs="-Xmx3g -XX:ParallelGCThreads=1"

java $vmArgs -jar RODES.jar
