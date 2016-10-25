#!/bin/sh
 #for i in FUN*; do (echo $i | sed 's/_6/_11/g'); done
#for i in *; do mv $i $(echo $i | sed 's/_4/_25/g'); done; ls -l

for folder in 00*;
do
  #printf "Processing: %s\n" "${folder}";

  cd ${folder}/data;

  for algorithm in *;
  do
    cd ${algorithm}/GeneticProblem
    CWD=`pwd`
    #printf "%s\n" "${CWD}";
    read -p "Waiting..."
    index=0;
    for file in FUN_POINT*;
    do
      mv $file $(printf "FUN.%s" "${index}");
      #mv file=$(printf "FUN.%s" "${index}");
      printf "%s\n" "${file}";
      index=$((index+1));
        #mv $j $(echo $j | sed "s/_1/_${index}/g");
    done
    cd ../..
  done
  cd ../..
done
