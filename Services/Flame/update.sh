rm -rf java-gen && rm -rf cpp-gen
mkdir java-gen && mkdir cpp-gen

protoc --java_out=java-gen ./BFEProtos.proto
protoc --cpp_out=cpp-gen ./BFEProtos.proto

cp -R -v ./cpp-gen/. ../../Anvil/Anvil/gen-files
cp -R -v ./java-gen/. ../../Flame/src/main/java