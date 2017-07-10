LATEST_TAG?=`git tag|sort -t. -k 1,1n -k 2,2n -k 3,3n -k 4,4n | tail -1`

help:
	cat Makefile.txt
	cat Makefile

clean:
	./gradlew clean

.PHONY: build
build:
	./gradlew build

build-fast:
	./gradlew build -Pcheck=false

release:
	./gradlew release

publish-snapshot:
	./gradlew build install uploadArchives

publish:
	git checkout tags/${LATEST_TAG}
	./gradlew build install uploadArchives
	git checkout master

.PHONY: doc
doc:
	rm -rf build/site/
	documentnode -i doc -o build/site/
	cp -r gwt-gradle-plugin/build/docs/javadoc build/site/
