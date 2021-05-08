#!/bin/sh
# $Id$

MODE=gui
JAR=bcomp-ng.jar
ADD=""

while getopts gcdnuC:D:p: arg
do
	case "$arg" in
	g)	MODE=gui;;
	c)	MODE=cli;;
	d)	MODE=decoder;;
	n)	MODE=nightmare;;
	u)	MODE=dual;;
	C)	ADD="$ADD -Dcode=$OPTARG";;
	D)	ADD="$ADD -Ddebuglevel=$OPTARG";;
	p)	JAR="$OPTARG";;
	*)
		echo "Usage: bcomp [-g | -c | -d | -u] [-b | -o | -e] [-p path]
	-g - run in GUI mode (default)
	-c - run in CLI mode
	-u - run in dual GUI + CLI mode
	-n - run in nightmare mode
	-d - run microprogram decoder
	-p path - path to bcomp.jar"
		exit 1
	esac
done

java -server -Dmode=$MODE $ADD -jar "$JAR"
