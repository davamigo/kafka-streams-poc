#!/usr/bin/env bash

cd $(dirname $0)

HELP=false
SCHEMA_REGISTRY_URL="http://localhost:8081/"
CONFIG_PATH="config/___SUBJECT___"
REGISTER_SCHEMA_PATH="subjects/___SUBJECT___/versions"
CONTENT_TYPE="application/vnd.schemaregistry.v1+json"
SCHEMA_FILE=""
SUBJECT=""

ARGS=$(getopt -o "hu:s:f:" --long "help,url:,subject:,file:" -- "$@");
if [ $? != 0 ]; then
    HELP=true;
fi;

eval set -- "${ARGS}";
while [ "$1" != "" ]; do

    case "$1" in

        '-h' | '--help')
            HELP=true;
            shift;;

        '-u' | '--url')
            SCHEMA_REGISTRY_URL=$2
            shift 2;;

        '-s' | '--subject')
            SUBJECT=$2
            shift 2;;

        '-f' | '--file')
            SCHEMA_FILE=$2
            shift 2;;

        '--')
            shift;
            break;;
    esac;
done;

if [ ${HELP} == false ]; then
    if [ "${SUBJECT}" == "" ]; then
        echo -e "\nYou have to specify the subject: topic-value or topic-key."
        HELP=true;
    fi;

    if [ "${SCHEMA_FILE}" == "" ]; then
        echo -e "\nYou have to specify the schema AVSC file."
        HELP=true;
    elif [ ! -f ${SCHEMA_FILE} ]; then
        echo -e "\nSchema file does not exist: ${SCHEMA_FILE}"
        HELP=true
    fi;
fi;

if [ ${HELP} == true ]; then
    echo -e "\nUsage: $0 [options]"
    echo -e "\t-h, --help\n\t\tShow this help."
    echo -e "\t-u, --url=[URL]\n\t\tThe schema registry base URL. Default: --url=http://localhost:8081/"
    echo -e "\t-s, --subject=[SUBJECT]\n\t\tMandatory. The subject in the form topic-key or topic-value."
    echo -e "\t-f, --file=[FILE]\n\t\tMandatory. The schema file in AVSC format."
    echo -e ""
    exit 0;
fi;

CONFIG_PATH=${CONFIG_PATH/___SUBJECT___/${SUBJECT}}
STR_JSON="{\"compatibility\": \"FULL\"}";

COMMAND="curl -X PUT -H 'Content-Type: ${CONTENT_TYPE}' --data '${STR_JSON}' ${SCHEMA_REGISTRY_URL}${CONFIG_PATH}"
echo -e "\n\$ ${COMMAND}\n"
curl -X PUT -H "Content-Type: ${CONTENT_TYPE}" --data "${STR_JSON}" ${SCHEMA_REGISTRY_URL}${CONFIG_PATH}
echo -e "";

REGISTER_SCHEMA_PATH=${REGISTER_SCHEMA_PATH/___SUBJECT___/${SUBJECT}}
STR_SCHEMA=$(cat ${SCHEMA_FILE} | python -c 'import json,sys; print(json.dumps(sys.stdin.read().replace("\n", "").replace("  ", "")))')
STR_JSON="{\"schema\": ${STR_SCHEMA}}";

COMMAND="curl -X POST -H 'Content-Type: ${CONTENT_TYPE}' --data '${STR_JSON}' ${SCHEMA_REGISTRY_URL}${REGISTER_SCHEMA_PATH}";
echo -e "\n\$ ${COMMAND}\n"
curl -X POST -H "Content-Type: ${CONTENT_TYPE}" --data "${STR_JSON}" ${SCHEMA_REGISTRY_URL}${REGISTER_SCHEMA_PATH}
echo -e "";

