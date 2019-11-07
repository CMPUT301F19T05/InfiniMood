#!/bin/bash

set -e
set -u

GOOGLE_SERVICES=./src/app/google-services.json
GOOGLE_MAP=./src/app/src/main/res/values/keys.xml

function usage {
    echo 'Usage:'
    echo '    --encrypt  Encrypt secrets from files'
    exit 1
}

if [ "$#" -ne '1' ]; then
    usage
elif [ "$1" == '--encrypt' ]; then
    travis encrypt-file $GOOGLE_SERVICES --add
    travis encrypt-file $GOOGLE_MAP --add
else
    usage
fi

