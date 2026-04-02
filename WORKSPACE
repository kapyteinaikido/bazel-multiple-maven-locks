load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "rules_jvm_external",
    sha256 = "cd1a77b7b02e8e008439ca76fd34f5dc91de50f02a6f41846ba3e5374af0e23e",
    strip_prefix = "rules_jvm_external-6.0",
    url = "https://github.com/bazelbuild/rules_jvm_external/releases/download/6.0/rules_jvm_external-6.0.tar.gz",
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")
rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")
rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    name = "maven_core",
    artifacts = [
        "com.google.guava:guava:31.1-jre",
        "org.slf4j:slf4j-api:1.7.36",
    ],
    fetch_sources = True,
    lock_file = "//:maven_core_lock.json",
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)

maven_install(
    name = "maven_testing",
    artifacts = [
        "junit:junit:4.13.2",
        "org.mockito:mockito-core:4.6.1",
    ],
    fetch_sources = True,
    lock_file = "//:maven_testing_lock.json",
    repositories = [
        "https://repo1.maven.org/maven2",
    ],
)