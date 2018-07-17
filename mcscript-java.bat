@echo off
if "%1"=="" (
echo [91mNeeding min one argument![0m
goto end
)
JAVA -jar "%~dp0\mcscript.jar" %*
:end