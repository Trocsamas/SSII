#!/bin/bash

encryption=sha256
verbose=false
noMail=false

mkdir -p $HOME/.apicultor/history

historial=$HOME/.apicultor/history

hashesNuevos=$HOME/.apicultor/hashesNuevos.csv
hashesAntiguos=$HOME/.apicultor/hashesAntiguos.csv

directoriosNuevos=$HOME/.apicultor/directoriosNuevos.csv
directoriosAntiguo=$HOME/.apicultor/directoriosAntiguo.csv

diffHashes=$HOME/.apicultor/diffHashes.csv
diffDirectorios=$HOME/.apicultor/diffDirectorios.csv

touch $hashesNuevos
touch $directoriosNuevos
function usage()
{
    cat<<USAGE
Apicultor is a script which manages an automatic HIDS to detect changes in directories and files
usage: ${0##*/} [options] [paths]

  Options:
   -h, --help               Display this help
   -V, --version            Display version information
   -v, --verbose            Display information while the script is running
   --encryption=HASH        Use the name as encryption
   --md5                    Use of MD5 as encryption method
   --sha1                   Use of SHA1 as encryption method
   --sha224                 Use of SHA224 as encryption method
   --sha256(default)        Use of SHA256 as encryption method
   --sha384                 Use of SHA384 as encryption method
   --sha512                 Use of SHA512 as encryption method
   --no-mail                Only use for Debugging

USAGE
}

function print_notification()
{
    if which zenity >/dev/null; then zenity --notification --text="$1" &>/dev/null;
        elif which kdialog >/dev/null; then kdialog --passivepopup 30 "$1" &>/dev/null;
    fi
}

function print_error()
{
    if which zenity >/dev/null; then zenity --error --text="$1" &>/dev/null;
        elif which kdialog >/dev/null; then kdialog --error "$1" &>/dev/null;
    fi
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
            if $verbose; then echo -e "Entering path:\t $file/"
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
            print_error "The encryption method is not a valid one"
            exit 1
    esac
}

function comparador()
{
    antiguo=$1;
    nuevo=$2;
    output=$3
    
    diff -W999 --side-by-side $antiguo $nuevo | sed '/^[^\t]*\t\s*|\t\(.*\)/{s//\1,MODIFIED/;b};/^\([^\t]*\)\t*\s*<$/{s//\1,NEW/;b};/^.*>\t\(.*\)/{s//\1,REMOVED/;b};d' > $output;
}

function main()
{
    
    if $verbose; then echo "Initialization Apicultor, using $encryption as encryption method"
    fi
    
    for arg in "$@"; do
        if $verbose; then echo -e "Entering path:\t $1"
        fi
        echo "$1">>$directoriosNuevos
        operacion_recursiva $1
        shift
    done
    
    comparador "$hashesNuevos" "$hashesAntiguos" "$diffHashes"
    comparador "$directoriosNuevos" "$directoriosAntiguo" "$diffDirectorios"
    
    nCambios=$(wc -l < "$diffHashes")
    if [ $nCambios -ne 0 ]; then
        print_notification "Ha habido cambios en $nCambios archivos"
        if ! $noMail; then
            echo "Ha habido cambios en los archivos adjuntos" | mail -s "Cambios en ficheros de $1" root --attach=$diffHashes
        fi
    fi
    rm $diffHashes
    
    nCambios=$(wc -l < "$diffDirectorios")
    if [ $nCambios -ne 0 ]; then
        print_notification "Ha habido un cambio en la estructura de directorios"
        if ! $noMail; then
            echo "Ha habido cambios en los directorios adjuntos" | mail -s "Cambios en directorios de $1" root --attach=$diffDirectorios
        fi
    fi
    rm $diffDirectorios
    
    cp $hashesNuevos $hashesAntiguos
    mv $hashesNuevos $historial/$(date +%F_%R)_hashes.csv
    
    cp $directoriosNuevos $directoriosAntiguo
    mv $directoriosNuevos $historial/$(date +%F_%R)_directorios.csv
}

if [ $# -eq 0 ]; then
    usage
    exit 1
fi

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
        --no-mail)
            noMail=true
            shift
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

main "$@"
