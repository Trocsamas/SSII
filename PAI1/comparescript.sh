#!/bin/bash

usage="$(basename "$0") [-h] filename1 filename 2
This script calculates the number of lines that differ between 2 
files and write them in a txt file (output.txt)

where:
    -h  show this help text"


while getopts ':hs:' option; do
  case "$option" in
    h) echo "$usage"
       exit
       ;;
    **)
		antiguo=$1;
		nuevo=$2;
		diff -W999 --side-by-side $antiguo $nuevo | sed '/^[^\t]*\t\s*|\t\(.*\)/{s//\1 U/;b};/^\([^\t]*\)\t*\s*<$/{s//\1 D/;b};/^.*>\t\(.*\)/{s//\1 N/;b};d' > output.txt;
  esac
done
shift $((OPTIND - 1))

antiguo=$1;
nuevo=$2;

diff -W999 --side-by-side $antiguo $nuevo | sed '/^[^\t]*\t\s*|\t\(.*\)/{s//\1 U/;b};/^\([^\t]*\)\t*\s*<$/{s//\1 D/;b};/^.*>\t\(.*\)/{s//\1 N/;b};d' > output.txt;

