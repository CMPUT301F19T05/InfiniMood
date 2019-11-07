#!/bin/bash

set -e
set -u

GOOGLE_SERVICES=./src/app/google-services.json
GOOGLE_MAP_API=./src/app/src/main/res/values/keys.xml

SECRETS_FILE=./secrets.zip

function usage {
    echo 'Usage:'
    echo '    --encrypt  Compress and encrypt secrets'
    echo '    --restore  Decompress secrets'
    exit 1
}

if [ "$#" -ne '1' ]; then
    usage
elif [ "$1" == '--encrypt' ]; then
    zip -9 $SECRETS_FILE $GOOGLE_SERVICES $GOOGLE_MAP_API
    travis encrypt-file $SECRETS_FILE --add
elif [ "$1" == '--restore' ]; then
    unzip $SECRETS_FILE
else
    usage
fi

