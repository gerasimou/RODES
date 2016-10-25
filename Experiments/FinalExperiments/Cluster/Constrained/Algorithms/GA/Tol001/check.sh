for i in *
	do
		v=`cat $i | wc -l`
		if [ ${v}  -lt 20 ]; then
			echo $i
			read -p "Fix this"
		fi
	done