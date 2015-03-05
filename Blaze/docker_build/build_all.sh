bash -c '[ -d mods ] && rm -rf mods'
mkdir mods
cd ..
./gradlew build
cp build/libs/* docker_build/mods
cp libs/* docker_build/mods
cd docker_build
docker build -t athilenius/blaze .
docker puch athilenius/blaze