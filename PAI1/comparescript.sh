#!/bin/bash


antiguo=$1
nuevo=$2

diff -W999 --side-by-side $antiguo $nuevo | sed '/^[^\t]*\t\s*|\t\(.*\)/{s//\1 U/;b};/^\([^\t]*\)\t*\s*<$/{s//\1 D/;b};/^.*>\t\(.*\)/{s//\1 N/;b};d' > output.txt

