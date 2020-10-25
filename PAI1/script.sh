#!/bin/bash

function operacion_recursiva()
{
    for file in "$1"*
    do
        if [ ! -d "${file}" ] ; then
            nombre=$(basename "$file")
            echo "$nombre is a file"
        else
            echo "${file} es un directorio"
            operacion_recursiva "${file}/"
        fi
    done
}
operacion_recursiva $1

