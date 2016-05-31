#!/bin/bash

if [ ! -d bin ]; then
	mkdir bin
fi

rm -rf bin/*

javac -g -Xlint:all -d bin/ -classpath lib/gson-2.6.2.jar -sourcepath src src/fi/bioklaani/klaanonbot/Main.java

jar cfm klaanonupdatebot.jar MANIFEST.mf -C bin/ .
