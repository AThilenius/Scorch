thrift.exe --gen cpp Flame.thrift
thrift.exe --gen csharp Flame.thrift
thrift.exe --gen java Flame.thrift

del .\gen-cpp\*.skeleton.cpp

xcopy ".\gen-csharp\Flame" "..\..\FlameCC\FlameCC\gen-files" /E /Y /I /R
xcopy ".\gen-cpp" "..\..\Blaze\src\FlameGenFiles" /E /Y /I /R
xcopy ".\gen-java" "..\..\Flame\src\main\java"  /E /Y /I /R