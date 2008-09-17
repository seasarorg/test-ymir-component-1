#!/bin/sh

NATIVE_PROPERTIES_PATH=$1

sh $DBFLUTE_HOME/etc/cmd/_df-copy-properties.sh $NATIVE_PROPERTIES_PATH

$DBFLUTE_HOME/ant/bin/ant -Ddfenv=$DBFLUTE_ENVIRONMENT_TYPE -f $DBFLUTE_HOME/build-torque.xml jdbc

cp ./schema/project-schema-${MY_PROJECT_NAME}.xml $DBFLUTE_HOME/schema/project-schema-${MY_PROJECT_NAME}.xml
