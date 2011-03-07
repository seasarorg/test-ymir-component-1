@echo off

set ANT_OPTS=-Xmx256M

set MY_PROJECT_NAME=scaffold

set DBFLUTE_HOME=..\mydbflute\dbflute-0.9.7.6

if "%pause_at_end%"=="" set pause_at_end=y
