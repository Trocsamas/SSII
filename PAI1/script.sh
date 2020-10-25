#!/bin/bash

encryption=sha256
function usage()
{
    cat<<USAGE
Este es un script que maneja de forma automática un hids
usage: ${0##*/} [options] [path]

  Options:
   -h, --help           display this help
   -V, --version        display version information
   --md5                Use of MD5 as encryption method
   --sha1               Use of SHA1 as encryption method
   --sha224             Use of SHA224 as encryption method
   --sha256(default)    Use of SHA256 as encryption method
   --sha384             Use of SHA384 as encryption method
   --sha512             Use of SHA512 as encryption method

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
            echo "$nombre:$hash">>Hashes.txt
        else
            echo "$file">>Hashes.txt
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

echo "$1">>Hashes.txt
operacion_recursiva $1
