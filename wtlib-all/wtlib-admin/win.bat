@echo off

cd ../px-base
call mvn clean install -Dmaven.test.skip
if %errorlevel% GTR 0 goto END

cd ../px-common
call mvn clean install -Dmaven.test.skip
if %errorlevel% GTR 0 goto END

cd ../px-admin
call mvn clean install -Dmaven.test.skip
if %errorlevel% GTR 0 goto END

SET MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=1066
call mvn tomcat7:run
SET MAVEN_OPTS= 

:END