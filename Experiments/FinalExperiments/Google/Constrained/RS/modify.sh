#!/bin/sh
 #for i in FUN*; do (echo $i | sed 's/_6/_11/g'); done
#for i in *; do mv $i $(echo $i | sed 's/_4/_25/g'); done; ls -l

mkdir AllData
for tol in Tol*;
do
  echo $tol;
  read -p "Do";
  cd $tol;
  index=0;
  mkdir Temp;
  mkdir all;
  for i in Run*
  #Run*;
  do
    #echo $i;
    index=$((index+1));
    printf "Processing:%s\n" "${i}";
    #read -p "Go...";

    #read -p "Copying...";
    cp ${i}/* Temp/

    #read -p "Changing Dir..";
    cd Temp/

    #read -p "Renaming...";
    for j in *;
    do
      #(echo $j | sed 's/_1/_${index}/g');
      mv $j $(echo $j | sed "s/_1/_${index}/g");
    done
    for j in *all;
    do
      mv $j $(echo $j | sed "s/_all/_all_${index}/g");
    done

    #read -p "Moving...";
    mv * ../all/

    #read -p "Finishing...";
    cd ../

    #index
    #rm ${i}/FUN_0*
    #rm ${i}/VAR_0*
  done
  rm -r Temp
  cd ..
  cp ${tol}/all/* AllData/
  rm AllData/*all*
done
