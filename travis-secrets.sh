#!/bin/bash

set -e
set -u

function usage {
    echo 'Usage:'
    echo '    --encrypt  Encrypt secrets from files'
    echo '    --restore  Put secrets in environment variables back to files'
    exit 1
}

if [ "$#" -ne '1' ]; then
    usage
elif [ "$1" == '--encrypt' ]; then
    travis encrypt-file ./src/app/google-services.json --add
    travis encrypt-file ./src/app/src/main/res/values/keys.xml --add
elif [ "$1" == '--restore' ]; then
    echo $GOOGLE_SERVICES_JSON > ./src/app/google-services.json
    echo $GOOGLE_MAP_API > ./src/app/src/main/res/values/keys.xml
else
    usage
fi

