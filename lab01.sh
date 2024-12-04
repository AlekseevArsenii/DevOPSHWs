#!/bin/bash

PID_FILE="/tmp/my_daemon.pid"
LOG_FILE="/tmp/my_daemon.log"
ERR_LOG_FILE="/tmp/my_daemon_err.log"
CSV_PATH="/tmp"
INFO_CSV=""
DATE=$(date +"%D")

PERIOD="10m"

function check_arg(){
	if [[ $2 == -* ]]; then 
		echo "Option $1 requires an argument"
		exit 1
	fi
}

create_csv_file()
{	
	INFO_CSV="${CSV_PATH}/$(date | sed 's/ /_/g').csv"
	echo "Date,Disk,Size,Used,Free,Inode Size,Inode Used,Inode Free" > $INFO_CSV
	_log "Created file ${INFO_CSV}"
}

usage()
{
    echo "$0 (START|STOP|STATUS)"
}

stop()
{
    if [ -e ${PID_FILE} ]
    then
        _pid=$(cat ${PID_FILE}) 
        kill -9 $_pid 
        rt=$? 
        if [ "$rt" == "0" ]
        then
                echo "Daemon stop"
				_log "Daemon stop"
				rm -f ${PID_FILE} 
				cleanup
        else
                echo "Error stop daemon"
        fi
    else
        echo "Daemon is not running"
    fi
}

status()
{
	if [ -e ${PID_FILE} ]
	then
		echo "daemon is running"
	else
		echo "daemon is not runnint"
	fi
}

_log()
{
    ts=`date +"%b %d %Y %H:%M:%S"`
    hn=`cat /etc/hostname`
    echo "$ts $hn $*" >> ${LOG_FILE} 
}

create_log_files()
{
	if [ -e ${LOG_FILE} ]; then
		cat ${LOG_FILE} >> ${LOG_FILE}.prev
		rm -f ${LOG_FILE}
	fi
	touch ${LOG_FILE}
	
	if [ -e ${ERR_LOG_FILE} ]; then
		cat ${ERR_LOG_FILE} >> ${ERR_LOG_FILE}.prev
		rm -f ${ERR_LOG_FILE}
	fi
	touch ${ERR_LOG_FILE}
}

cleanup()
{
	cat ${LOG_FILE} >> ${LOG_FILE}.prev
	cat ${ERR_LOG_FILE} >> ${ERR_LOG_FILE}.prev
	rm -f ${LOG_FILE}
	rm -f ${ERR_LOG_FILE}
}

run_daemon()
{
    cd /
	( 
		
		exec 2> ${ERR_LOG_FILE} 
		exec < /dev/null 
		trap  "{ exit 0; }" TERM INT EXIT KILL
		
		_log "Daemon started"
		while [ 1 ] 
		do
			cur_date=$(date +"%D")
			if [ $cur_date != $DATE ]; then
				create_csv_file 
				DATE=${cur_date}
				_log "New .csv file created -- ${INFO_CSV}"
			fi

			for disk in ${disks}; do				
				date=$(date) 
        		size_info=$(df ${disk} -h | sed '1d' | awk '{print $2, $3, $4}' | sed 's/,/./g' | sed 's/ /,/g') 
        		inode_info=$(df ${disk} -hi | sed '1d' | awk '{print $2, $3, $4}' | sed 's/,/./g' | sed 's/ /,/g')
        		disk_info="${date},${disk},${size_info},${inode_info}"
				percent_used_disk_space=$(df ${disk} -h | sed '1d' | awk '{print $5}' | sed 's/%//')
				if [ ${percent_used_disk_space} -gt 80 ]; then
					_log There are too little space on disk ${disk}
				fi

				echo ${disk_info} >> ${INFO_CSV}
			done

			sleep $PERIOD
		done
	)& 
}

start()
{
	echo "start"
    if [ -e ${PID_FILE} ]; then
    	_pid=( `cat ${PID_FILE}` )
    	if [ -e "/proc/${_pid}" ]; then
        	echo "Daemon already running with pid = $_pid"
        	exit 0
    	fi
    fi
	create_log_files
	create_csv_file 
	disks=$(df --exclude-type=tmpfs --exclude-type=efivarfs | awk '{print $1}' | sed '1d')

	run_daemon
		
	child_pid=$! 
	echo ${child_pid} > ${PID_FILE}
	echo "daemon PID is ${child_pid}"
	_log "daemon PID is ${child_pid}"
}

arg_count=0

while getopts :c:p: OPTION; do
	case "$OPTION" in
		c) 
			echo "new csv path is $OPTARG"
			check_arg "-c" "$OPTARG"
			CSV_PATH=$OPTARG
			let "arg_count = arg_count + 2"
			;;
		p)
			echo "new periode is $OPTARG"
			check_arg "-p" "$OPTARG"
			PERIOD=$OPTARG
			let "arg_count = arg_count + 2"
			;;
		:)
			echo "Option -$OPTARG requires an argument (getopts)"
			exit 1
			;;
		*) 
			echo "unexpected option"
			exit 1
			;;
	esac
done

for (( i=0; i<$arg_count; i++ )); do
	shift 
done

case $1 in
    "START")
        start
        ;;
    "STOP")
        stop
        ;;
	"STATUS")
		status
		;;
    *)
        usage
        ;;
esac
exit
