#!/bin/bash

base_method(){
	echo "切换到 $1 分支"
    git checkout $1
    cd library
    git submodule update

}

co_master_phase(){
    base_method master
}

co_first_phase(){
	base_method developer
}

co_second_phase(){
    base_method b1.1.1
}

git branch
options=("一期项目" "二期项目" "三期项目")
echo "你想切到哪期项目？"
select choice in ${options[*]};do
	break;
done
echo You have selected $choice

case "$choice" in
	${options[0]})
		co_first_phase
		;;
	${options[1]})
		co_second_phase
		;;
	esac

