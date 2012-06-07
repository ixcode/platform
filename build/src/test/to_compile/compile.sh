#!/bin/sh

mkdir -p ./target/classes

rm ./target/classes/*.class

#-Werror - terminate compilation if warnings are found

javac -Xlint -g -d target/classes -sourcepath ./src/test/to_compile ./src/test/to_compile/*.java

ls ./target/classes