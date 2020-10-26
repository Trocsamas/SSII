#!/bin/bash

encryption=sha256
verbose=false

function usage()
{
    cat<<USAGE
Este es un script que maneja de forma automática un hids
usage: ${0##*/} [options] [path]

  Options:
   -h, --help               Display this help
   -V, --version            Display version information
   -v, --verbose            Display information while the script is running
   --encryption==HASH       Use the name as encryption
   --md5                    Use of MD5 as encryption method
   --sha1                   Use of SHA1 as encryption method
   --sha224                 Use of SHA224 as encryption method
   --sha256(default)        Use of SHA256 as encryption method
   --sha384                 Use of SHA384 as encryption method
   --sha512                 Use of SHA512 as encryption method

USAGE
}

function version()
{
    cat<<version
    script 0.1
version
}

function operacion_recursiva()
{
    for file in "$1"*
    do
        if [ ! -d "${file}" ] ; then
            nombre=$(basename "$file")
            encrypt "$encryption" "${file}"
            echo "$nombre,${hash%% *},"${file}"">>hashesNuevos.csv
            if $verbose; then echo -e "\t ${hash%% *}\t:\t$nombre"
            fi
        else
            echo "$file">>directorios.txt
            if $verbose; then echo "Entering path: $file/"
            fi
            operacion_recursiva "${file}/"
        fi
    done
}

function encrypt()
{
    case $1 in
        sha1)
            hash=$(sha1sum "$2")
            return 1
        ;;
        sha224)
            hash=$(sha224sum "$2")
            return 1
        ;;
        sha256)
            hash=$(sha256sum "$2")
            return 1
        ;;
        sha512)
            hash=$(sha512sum "$2")
            return 1
        ;;
        md5)
            hash=$(md5sum "$2")
            return 1
        ;;
        *)
            echo "The encryption method is not a valid one"
            exit 1
    esac
}


while [ ! -d "$1" ]; do
    case $1 in
        -h|--help)
            usage
            exit 0
        ;;
        -V|--version)
            version
            exit 0
        ;;
        -v|--verbose)
            verbose=true
            shift
        ;;
        --encryption=*)
            encryption="${1#*=}"
            shift
        ;;
        --sha1)
            encryption=sha1
            shift
            break 2
        ;;
        --sha224)
            encryption=sha224
            shift
            break 2
        ;;
        --sha256)
            encryption=sha256
            shift
            break 2
        ;;
        --sha512)
            encryption=sha512
            shift
            break 2
        ;;
        --md5)
            encryption=md5
            shift
            break 2
        ;;
        --)
            shift
            break 2
        ;;
    esac
done
if $verbose; then echo "Iniciando el script donde se analizará el path $1 y se usará $encryption"
fi
echo "$1">>directorios.txt
operacion_recursiva $1
