# GWT Gradle Plugin Justfile

# Variables
latest_tag := `git tag | grep -E '^v?[0-9]+(\.[0-9]+)*(-[a-zA-Z]+[0-9]*)?$' | sort -V | tail -1`
project_dir := justfile_directory()
build_dir := project_dir / "build"

# Show available commands
default:
    @just --list --unsorted

# Clean build directories
clean:
    ./gradlew clean

# Build project and run tests
build:
    {{project_dir}}/gradlew build --warning-mode all

# Build all example projects
examples:
    cd {{project_dir}}/examples/basic-gwt-project && ./gradlew build
    cd {{project_dir}}/examples/gradle-jvm-test-suites-with-gwt && ./gradlew build
    cd {{project_dir}}/examples/basic-gwt-rpc && ./gradlew build
    cd {{project_dir}}/examples/extra-sources-example && gradle build

# Display current plugin version
version:
    ./gradlew currentVersion --warning-mode all

# Create a new release version
release:
    ./gradlew release --warning-mode all

# Publish to local Maven repository
publish-local: build
    rm -rf $HOME/.m2/repository/org/docstr/gwt
    {{project_dir}}/gradlew publishMavenJavaPublicationToMavenLocal --warning-mode all

# Publish to Maven Central
publish-maven: build
    rm -rf $HOME/.m2/repository/org/docstr/gwt
    {{project_dir}}/gradlew publishMavenJavaPublicationToMavenLocal publishMavenJavaPublicationToMavenRepository

# Publish to Gradle Plugin Portal
publish: build
    rm -rf $HOME/.m2/repository/org/docstr/gwt
    {{project_dir}}/gradlew publishPlugins --warning-mode all

# Build and deploy documentation site
site:
    cd {{project_dir}}/doc && docstr site build && cd {{project_dir}}/../dn-hosting-sites && just deploy-gwtgradle