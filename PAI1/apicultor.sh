#!/bin/bash

encryption=sha256
verbose=false

mkdir -p $HOME/.apicultor/

hashesNuevos=$HOME/.apicultor/hashesNuevos.csv
hashesAntiguos=$HOME/.apicultor/hashesAntiguos.csv
directoriosNuevos=$HOME/.apicultor/directoriosNuevos.csv
directoriOSAntiguo=$HOME/.apicultor/directoriOSAntiguo.csv
diffHashes=$HOME/.apicultor/diffHashes.csv
diffDirectorios=$HOME/.apicultor/diffDirectorios.csv

function usage()
{
    cat<<USAGE
Apicultor es un script que maneja de forma automática un hids para detectar cambios en archivos y directorios
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
    Apicultor 0.2
version
}

function operacion_recursiva()
{
    for file in "$1"*
    do
        if [ -f "${file}" ] ; then
            nombre=$(basename "$file")
            encrypt "$encryption" "${file}"
            echo "$nombre,${hash%% *},"${file}"">>$hashesNuevos
            if $verbose; then echo -e "\t ${hash%% *}\t:\t$nombre"
            fi
            elif [ -d "${file}" ] ; then
            echo "$file">>$directoriosNuevos
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

function comparador()
{
    antiguo=$1;
    nuevo=$2;
    output=$3
    
    diff -W999 --side-by-side $antiguo $nuevo | sed '/^[^\t]*\t\s*|\t\(.*\)/{s//\1 U/;b};/^\([^\t]*\)\t*\s*<$/{s//\1,CREADO/;b};/^.*>\t\(.*\)/{s//\1,ELIMINADO/;b};d' > $output;
}

function main()
{
    
    if $verbose; then echo "Iniciando Apicultor donde se analizará el path $1 y se usará $encryption"
    fi
    
    cp $hashesNuevos $hashesAntiguos
    rm $hashesNuevos
    
    cp $directoriosNuevos $directoriOSAntiguo
    rm $directoriosNuevos
    
    echo "$1">>$directoriosNuevos
    operacion_recursiva $1
    
    comparador "$hashesNuevos" "$hashesAntiguos" "$diffHashes"
    comparador "$directoriosNuevos" "$directoriOSAntiguo" "$diffDirectorios"
    
    nCambios=$(wc -l < "$diffHashes")
    if [ $nCambios -ne 0 ]; then
        zenity --notification --text "Ha habido cambios en $nCambios archivos"
        echo "Ha habido cambios en los archivos adjuntos" | mail -s "Cambios en ficheros de $1" root
    fi
    #rm $diffHashes
    
    nCambios=$(wc -l < "$diffDirectorios")
    if [ $nCambios -ne 0 ]; then
        zenity --notification --text "Ha habido un cambio en la estructura de directorios"
        echo "Ha habido cambios en los directorios adjuntos" | mail -s "Cambios en directorios de $1" root
    fi
    #rm $diffDirectorio
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

main $1