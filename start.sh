#!/bin/sh
if [ -e ~/.amx/amx.pid ]; then
    PID=`cat ~/.amx/amx.pid`
    ps -p $PID > /dev/null
    STATUS=$?
    if [ $STATUS -eq 0 ]; then
        echo "Amx server already running"
        exit 1
    fi
fi
mkdir -p ~/.amx/
DIR=`dirname "$0"`
cd "${DIR}"
if [ -x jre/bin/java ]; then
    JAVA=./jre/bin/java
else
    JAVA=java
fi
nohup ${JAVA} -cp classes:lib/*:conf:addons/classes:addons/lib/* -Damx.runtime.mode=desktop amx.Amx > /dev/null 2>&1 &
echo $! > ~/.amx/amx.pid
cd - > /dev/null
