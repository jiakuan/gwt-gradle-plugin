LATEST_TAG  ?= `git tag | grep -E '^v?[0-9]+(\.[0-9]+)*(-[a-zA-Z]+[0-9]*)?$' | sort -V | tail -1`
PROJECT_DIR := $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))
BUILD_DIR   := ${PROJECT_DIR}/build

help:
	cat Makefile.txt

clean:
	./gradlew clean

.PHONY: build
build:
	${PROJECT_DIR}/gradlew build --warning-mode all

version:
	./gradlew currentVersion --warning-mode all

release:
	./gradlew release --warning-mode all

publish-local: build
	rm -rf $$HOME/.m2/repository/org/docstr/gwt
	${PROJECT_DIR}/gradlew publishMavenJavaPublicationToMavenLocal --warning-mode all

publish-maven: build
	rm -rf $$HOME/.m2/repository/org/docstr/gwt
	${PROJECT_DIR}/gradlew publishMavenJavaPublicationToMavenLocal publishMavenJavaPublicationToMavenRepository

publish: build
	rm -rf $$HOME/.m2/repository/org/docstr/gwt
	${PROJECT_DIR}/gradlew publishPlugins --warning-mode all

site:
	cd ${PROJECT_DIR}/doc && docstr site build && cd ${PROJECT_DIR}/../dn-hosting-sites && make deploy-gwtgradle
