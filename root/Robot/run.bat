@echo off
cd /d "%~dp0"  REM Go to the directory where the batch file is located

REM Enable delayed expansion
setlocal EnableDelayedExpansion

REM Set initial classpath to bin
set "CLASSPATH=bin"

REM Add all JARs from bin\+libs to classpath
for %%f in (bin\+libs\*.jar) do (
    set "CLASSPATH=!CLASSPATH!;%%f"
)

REM Run Main from bin
java -cp "!CLASSPATH!" Main

pause