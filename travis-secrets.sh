#!/bin/bash

set -e
set -u

GOOGLE_SERVICES=./src/app/google-services.json
GOOGLE_MAP=./src/app/src/main/res/values/keys.xml

function usage {
    echo 'Usage:'
    echo '    --encrypt  Encrypt secrets from files'
    echo '    --restore  Put secrets in environment variables back to files'
    exit 1
}

if [ "$#" -ne '1' ]; then
    usage
elif [ "$1" == '--encrypt' ]; then
    travis encrypt-file $GOOGLE_SERVICES --add
    travis encrypt-file $GOOGLE_MAP --add
elif [ "$1" == '--restore' ]; then
    openssl aes-256-cbc                \
        -K $encrypted_0a6446eb3ae3_key \
        -iv $encrypted_0a6446eb3ae3_iv \
        -in google-services.json.enc   \
        -out $GOOGLE_SERVICES          \
        -d
    openssl aes-256-cbc                \
        -K $encrypted_0a6446eb3ae3_key \
        -iv $encrypted_0a6446eb3ae3_iv \
        -in keys.xml.enc               \
        -out $GOOGLE_MAP               \
        -d
else
    usage
fi

