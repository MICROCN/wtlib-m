@echo off

cd ../wtlib-common
call mvn clean install -Dmaven.test.skip
if %errorlevel% GTR 0 goto END

cd ../wtlib-base
echo ddddddddddddddddddddddddddddddddddddddddd
call mvn clean install -Dmaven.test.skip
if %errorlevel% GTR 0 goto END

cd ../wtlib-admin
call mvn clean install -Dmaven.test.skip
if %errorlevel% GTR 0 goto END

SET MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1066
call mvn tomcat:run
SET MAVEN_OPTS= 

:END