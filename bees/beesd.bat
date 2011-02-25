@echo off
@setlocal

if "%BEES_HOME%" == "" set BEES_HOME=%STAX_HOME%
if "%BEES_HOME%" == "" goto err_nobeeshome
set GROOVY_HOME=%BEES_HOME%\groovy
set STAX_HOME=%BEES_HOME%

set JAVA_OPTS=-Dbees.home=%BEES_HOME% -Xmx256m -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
%GROOVY_HOME%\bin\groovy.bat %BEES_HOME%\scripts\bootstrap.groovy %*

:err_nobeeshome
echo "BEES_HOME is not set"
goto end

:end
@endlocal