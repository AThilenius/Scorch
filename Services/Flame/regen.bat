thrift.exe --gen cpp Flame.thrift
thrift.exe --gen csharp Flame.thrift

xcopy ".\gen-csharp\Flame" "..\..\FlameCC\FlameCC\gen-files" /E /Y /I /R
xcopy ".\gen-cpp" "..\..\Blaze\src\FlameGenFiles" /E /Y /I /R