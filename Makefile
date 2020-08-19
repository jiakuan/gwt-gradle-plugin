LATEST_TAG?=`git tag|sort -t. -k 1,1n -k 2,2n -k 3,3n -k 4,4n | tail -1`

help:
	cat Makefile.txt

clean:
	rm -rf build
	./gradlew clean

.PHONY: build
build:
	./gradlew build

build-fast:
	./gradlew build -Pcheck=false

release:
	./gradlew release

publish-snapshot:
	./gradlew build publishToMavenLocal publish

publish:
	git checkout tags/${LATEST_TAG}
	./gradlew build publishToMavenLocal publish
	git checkout master

publish-plugins:
	git checkout tags/${LATEST_TAG}
	./gradlew publishPlugins
	git checkout master

mvn-close:
	./gradlew closeRepository

mvn-release:
	./gradlew releaseRepository

.PHONY: doc
doc: build
	rm -rf doc/javadoc/
	cp -r gwt-gradle-plugin/build/docs/javadoc doc/
