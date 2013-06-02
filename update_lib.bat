@echo off
REM
REM Please ignore this file.
REM

REM copy /Y bin\\*.jar ..\lib\\
REM pause

echo 1. Removing old stuff
rd /S /Q tmp2
mkdir tmp2
cd tmp2 || exit

echo 2. Getting the project JAR
REM jar xf ../bin/*.jar

xcopy ..\classes /E /Q

echo 3. Removing unwanted files
del /Q *.*
rd /S /Q META-INF
rd /S /Q tube42\imagelib_demo

echo 4. Creating the new library
jar cf imagelib.jar .
copy /Y imagelib.jar ..\..\lib
echo 5. all done
dir ..\..\lib\*.jar
pause
