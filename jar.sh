#!/bin/sh
java -cp classes amx.tools.ManifestGenerator
/bin/rm -f amx.jar
jar cfm amx.jar resource/amx.manifest.mf -C classes . || exit 1
/bin/rm -f amxservice.jar
jar cfm amxservice.jar resource/amxservice.manifest.mf -C classes . || exit 1

echo "jar files generated successfully"
