#!/bin/sh
java -cp "classes:lib/*:conf" amx.tools.SignTransactionJSON $@
exit $?
