#!/bin/bash

if [ ! -d bin ]; then
	mkdir bin
fi

rm -rf bin/*

javac -g -d bin/ -sourcepath src/ src/fi/bioklaani/klaanonbot/Main.java

jar cfm klaanonupdatebot.jar MANIFEST.mf -C bin/ .
