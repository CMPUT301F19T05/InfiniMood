#!/bin/bash

set -e
set -u

GOOGLE_SERVICES=./src/app/google-services.json
GOOGLE_MAP_API=./src/app/src/main/res/values/keys.xml

SECRETS_FILE=./secrets.zip

function usage {
    echo 'Usage:'
    echo '    --encrypt   Compress and encrypt secrets'
    echo '    --restore   Decompress secrets'
    echo '    --compress  Compress but not encrypt'
    exit 1
}

function compress {
    zip -9 $SECRETS_FILE $GOOGLE_SERVICES $GOOGLE_MAP_API
}

if [ "$#" -ne '1' ]; then
    usage
elif [ "$1" == '--encrypt' ]; then
    compress
    travis encrypt-file $SECRETS_FILE --add
elif [ "$1" == '--restore' ]; then
    unzip -o $SECRETS_FILE
elif [ "$1" == '--compress' ]; then
    compress
else
    usage
fi

