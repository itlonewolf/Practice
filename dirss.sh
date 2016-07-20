#!/usr/bin/env bash
#
#for ba in $(git branch)
#do
#     echo $ba
#done

isNotContainThis(){
  strArray=`echo "$1"`
  strTarget=`echo "$2"`
  for str in ${strArray[*]}; do
    if test "$strTarget" = "$str"; then
      return 0
    fi
  done
  return 1
}

listDir(){
    echo "**********文件夹中所有文件"
    dirs=$(ls)
    for dir in $dirs
    do
        echo $dir
    done
}

listBranch(){
    echo "**********所有分支"
    branchs=$(git branch)
    for branch in $branchs
    do
        echo $branch
    done
}

showAllBranch(){
    branchs=$(git branch)
    dirs=$(ls)

    for branch in $branchs
    do
        if isNotContainThis dirs branch ; then
            echo $branch
        fi
    done
}

var=$(git branch --list)
echo $var