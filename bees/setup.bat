@echo off

set BEES_HOME=%~dp0.
set PATH=%PATH%;%~dp0

if "%1" == "" goto welcome
goto %1

:err_nojavahome
echo JAVA_HOME not set
goto end

:homedir
goto welcome

:welcome
echo Welcome to the CloudBees Development Console
echo --------------------------------------------
echo Here are some useful "How do I?" commands...
echo creating a new web application project
echo    bees help create
echo running your web application
echo    bees help run
echo deploying your web application to AppStax
echo    bees help deploy


goto end

:end
