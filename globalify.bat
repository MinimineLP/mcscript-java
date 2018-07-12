@echo off

echo.
:ask
choice /N /C:NY /M "Do you realy want to globalify this folder (press y or n): "
if errorlevel 255 goto ask
if errorlevel 3 goto pick3
if errorlevel 2 goto do
if errorlevel 1 echo canceled & goto end
if errorlevel 0 goto ask

:do
setlocal ENABLEEXTENSIONS
set REG=C:\Windows\System32\reg.exe
set KEY_NAME="HKCU\Software\Microsoft\Command Processor"

FOR /F "usebackq tokens=3*" %%A IN (`%REG% QUERY %KEY_NAME% /v AutoRun`) DO (
    set appdir=%%A %%B
)

%REG% add %KEY_NAME% /v AutoRun ^ /t REG_EXPAND_SZ /d "%appdir%^& set PATH=%%PATH%%;%cd%" /f > nul
echo Done correctly!
echo.
:end