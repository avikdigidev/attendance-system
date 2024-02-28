@echo off
setlocal enabledelayedexpansion

rem Set the folder path to search
set "folder_path=D:\Projects\JavaProjects\attendance-system\attendance-system-v2"


rem Search for pom.xml file in immediate subfolders
for /d %%i in ("%folder_path%\*") do (
    if exist "%%i\pom.xml" (
        echo Found pom.xml in %%i
        echo Running 'mvn clean install' in %%i...
        pushd "%%i"
        mvn clean install -DskipTests
        popd
    ) else (
        echo No pom.xml found in %%i
    )
)

pause
