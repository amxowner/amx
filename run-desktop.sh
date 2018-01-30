#!/bin/sh
if [ -x jre/bin/java ]; then
    JAVA=./jre/bin/java
else
    JAVA=java
fi
${JAVA} -cp classes:lib/*:conf:addons/classes:addons/lib/* -Damx.runtime.mode=desktop -Damx.runtime.dirProvider=amx.env.DefaultDirProvider amx.Amx
