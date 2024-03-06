@echo off
setlocal enabledelayedexpansion


rem Set the folder path to search
set "folder_path=D:\Projects\JavaProjects\attendance-system\"

rem Search for pom.xml file in immediate subfolders
for /d %%i in ("%folder_path%\*") do (
    if exist "%%i\pom.xml" (
        echo Found pom.xml in %%i
        echo Running 'mvn clean' in %%i...
        pushd "%%i"
        mvn clean 
        popd
    ) else (
        echo No pom.xml found in %%i
    )
)

pause
