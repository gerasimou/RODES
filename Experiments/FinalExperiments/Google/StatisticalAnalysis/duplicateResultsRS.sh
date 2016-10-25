#!/bin/sh
CWD=`pwd`

for folder in 00*;
do
  #printf "Processing: %s\n" "${folder}";

  cd ${folder}/data/RS/GeneticProblem/;
  pwd
  read -p "Waiting"
  START=`ls -l FUN* | wc -l`
  END=30
  INDEX=0
  for ((i=$START; i<$END; i++))
  do
     echo "$i"
     cp FUN.${INDEX} FUN.${i}
     INDEX=$((INDEX+1));
  done
  read -p "Test"
  cd $CWD
done
