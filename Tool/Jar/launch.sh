#!/bin/bash

#  launch.sh
#
#  Created by Simos Gerasimou on 08/05/2014.
#  Copyright (c) 2014 SPG. All rights reserved.

#For launching an instance only
PRISM_DIR=repo/prism
#export MALLOC_ARENA_MAX=4
export DYLD_LIBRARY_PATH="$PRISM_DIR":$DYLD_LIBRARY_PATH
#mArgs="-Xmx3g -XX:ParallelGCThreads=1"

echo $DYLD_LIBRARY_PATH

java $vmArgs -jar RODES.jar






