#!/bin/sh
 #for i in FUN*; do (echo $i | sed 's/_6/_11/g'); done
#for i in *; do mv $i $(echo $i | sed 's/_4/_25/g'); done; ls -l

for tol in Tol*; #Tolerances
do
  #echo ${tol};
  cd ${tol};
  for run in Run*; #Run*;
  do
    echo ${run};
    cd ${run};
    for file in FUN_REGION_0*; #files
    do
      echo ${file};
      lines=`cat $file | wc -l`
      if [[ lines -lt 20 ]]; then
        read -p "problem";
      fi
    done
    cd ..
  done
  cd ..
done
