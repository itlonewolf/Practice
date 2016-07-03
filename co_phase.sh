#!/bin/bash

base_method(){
	echo "参数为 $1"
	options=("一期目" "二期" "三期")
	echo "切到哪期项目？"
	select choice in ${options[*]};do
		break;
	done
}

co_first_phase(){
	base_method 1
}

options=("一期项目" "二期项目" "三期项目")
echo "切到哪期项目？"
select choice in ${options[*]};do
	break;
done
echo You have selected $choice

case "$choice" in
	${options[0]})
		co_first_phase
		;;
	${options[1]})
		echo demo1
		;;
	esac

