rm -rf java-gen && rm -rf cpp-gen
mkdir java-gen && mkdir cpp-gen

protoc --java_out=java-gen ./BFEProtos.proto
protoc --cpp_out=cpp-gen ./BFEProtos.proto