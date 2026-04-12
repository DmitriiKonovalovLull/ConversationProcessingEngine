@REM -----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script for Windows
@REM -----------------------------------------------------------------------------

@echo off

@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set "HOME=%HOMEDRIVE%%HOMEPATH%")

@REM Validate Java installation
if not "%JAVA_HOME%" == "" goto JavaHomeFound
echo Error: JAVA_HOME not found in environment >&2
goto error

:JavaHomeFound
if exist "%JAVA_HOME%\bin\java.exe" goto Init
echo Error: JAVA_HOME is set to invalid directory >&2
goto error

:Init
@REM Find project base dir
set "MAVEN_PROJECTBASEDIR=%~dp0"

set MAVEN_JAVA_EXE="%JAVA_HOME%\bin\java.exe"
set WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
set WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@REM Build the command line
set MAVEN_CMD_LINE_ARGS=%*

%MAVEN_JAVA_EXE% ^
  -classpath %WRAPPER_JAR% ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  %WRAPPER_LAUNCHER% ^
  %MAVEN_CMD_LINE_ARGS%

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%

exit /B %ERROR_CODE%
