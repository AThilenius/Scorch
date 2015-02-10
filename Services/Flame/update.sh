# Clean Bin folder
rm -rf bin
mkdir bin && mkdir ./bin/java-gen && mkdir ./bin/cpp-gen

# Build Full Protos
protoc --java_out=./bin/java-gen/ ./BFEProtos.proto

# Build Lite Protos
echo "option optimize_for = LITE_RUNTIME;\n" | cat - ./BFEProtos.proto > ./bin/BFEProtos.proto
cd bin
protoc --cpp_out=./cpp-gen/ ./BFEProtos.proto
cd ..

cp -R -v ./bin/cpp-gen/. ../../Anvil/OSX/Anvil/gen-files
cp -R -v ./bin/cpp-gen/. ../../Anvil/Windows/Anvil/gen-files
cp -R -v ./bin/java-gen/. ../../Blaze/src/main/java