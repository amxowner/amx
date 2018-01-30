#!/bin/sh
if [ -e ~/.amx/amx.pid ]; then
    PID=`cat ~/.amx/amx.pid`
    ps -p $PID > /dev/null
    STATUS=$?
    echo "stopping"
    while [ $STATUS -eq 0 ]; do
        kill `cat ~/.amx/amx.pid` > /dev/null
        sleep 5
        ps -p $PID > /dev/null
        STATUS=$?
    done
    rm -f ~/.amx/amx.pid
    echo "Amx server stopped"
fi

