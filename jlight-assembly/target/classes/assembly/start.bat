@echo off & setlocal enabledelayedexpansion

set LIB_JARS=""
cd .\lib
for %%i in (*) do set LIB_JARS=!LIB_JARS!;.\lib\%%i
cd ..\

if ""%1"" == ""debug"" goto debug
if ""%1"" == ""jmx"" goto jmx

set JAVA_MAIN_CLASS="com.lew.jlight.web.Application"
set JAVA_CLASS_PATH=".\config;%LIB_JARS%"

java -classpath %JAVA_CLASS_PATH% %JAVA_MAIN_CLASS%
goto end

:debug
java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n -classpath %JAVA_CLASS_PATH% %JAVA_MAIN_CLASS%
goto end

:jmx
java -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -classpath %JAVA_CLASS_PATH% %JAVA_MAIN_CLASS%

:end
pause
