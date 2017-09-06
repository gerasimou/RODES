#! /bin/sh

#  launch.sh
#
#  Created by Simos Gerasimou on 04/09/2017.

if [ -d data ]; then
  rm -rf data/*
else
  mkdir data
fi


#For launching an instance only
PRISM_DIR=repo/prism

export DYLD_LIBRARY_PATH="$PRISM_DIR":$DYLD_LIBRARY_PATH

vmArgs="-Xmx3g -XX:ParallelGCThreads=1"

unameOut="$(uname -s)"
case "${unameOut}" in
    Linux*)     machine=Linux
    				export LD_LIBRARY_PATH="$PRISM_DIR":$LD_LIBRARY_PATH
    				;;
    Darwin*)    machine=Mac
    				export DYLD_LIBRARY_PATH="$PRISM_DIR":$DYLD_LIBRARY_PATH
    				;;
    CYGWIN*)    machine=Cygwin;;
    MINGW*)     machine=MinGw;;
    *)          machine="UNKNOWN:${unameOut}"
esac


java $vmArgs -jar RODES.jar

