@REM Maven Wrapper startup script for Windows
@REM
@echo off

set MAVEN_PROJECTBASEDIR=%~dp0..
set MAVEN_OPTS=%MAVEN_OPTS% -Xms256m -Xmx512m

@REM Find the project base dir
FOR /F "tokens=*" %%i IN ("%MAVEN_PROJECTBASEDIR%") DO SET MAVEN_PROJECTBASEDIR=%%~fi

@REM Execute Maven
"%JAVA_HOME%\bin\java.exe" ^
  -classpath "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar" ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  org.apache.maven.wrapper.MavenWrapperMain %*

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
exit /B %ERROR_CODE%
