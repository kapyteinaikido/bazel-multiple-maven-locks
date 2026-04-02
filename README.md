# Bazel Multiple Maven Lock Files Example

This repository demonstrates how to use multiple `maven_install()` declarations in Bazel with separate, uniquely-named lock files.

Supports both **Bzlmod (MODULE.bazel)** for Bazel 7+ and **WORKSPACE** for legacy environments.

## Overview

Instead of having a single monolithic `maven_install()` rule, this example separates dependencies into logical groups:

- **maven_core**: Production/core dependencies (Guava, SLF4J)
- **maven_testing**: Test dependencies (JUnit, Mockito)

Each has its own lock file and unique name for clarity and maintainability.

## Benefits

✅ **Separation of Concerns**: Keep production and test dependencies separate  
✅ **Easier Maintenance**: Update locks independently  
✅ **Clear Dependencies**: Each target explicitly declares which maven repository to use  
✅ **Reduced Build Context**: Only load what you need  
✅ **Better Reproducibility**: Each lock file is independently managed  
✅ **Bzlmod Compatible**: Works with Bazel 7+ MODULE.bazel system  

## File Structure

```
.
├── MODULE.bazel                 # Bzlmod configuration (Bazel 7+)
├── WORKSPACE                    # Legacy WORKSPACE (optional)
├── .bazelrc                      # Bazel configuration
├── BUILD.bazel                  # Example targets using both maven repos
├── maven_core_lock.json         # Lock file for production dependencies
├── maven_testing_lock.json      # Lock file for test dependencies
├── CoreLib.java                 # Example production code
├── ExampleTest.java             # Example test code
└── README.md                    # This file
```

## Getting Started

### For Bazel 7+ (Bzlmod)

The `MODULE.bazel` file is used by default:

```bash
bazel build //...
bazel test //...
```

### For Bazel < 7 (WORKSPACE)

Disable Bzlmod using `.bazelrc`:

```bash
bazel build //... --config=legacy
bazel test //... --config=legacy
```

## MODULE.bazel Configuration

The `MODULE.bazel` file defines two separate maven repositories using the `maven` extension:

```python
module(
    name = "bazel_multiple_maven_locks",
    version = "1.0.0",
)

bazel_dep(name = "rules_jvm_external", version = "6.0")

maven = use_extension("@rules_jvm_external//:extension.bzl", "maven")

maven.install(
    name = "maven_core",
    artifacts = [
        "com.google.guava:guava:31.1-jre",
        "org.slf4j:slf4j-api:1.7.36",
    ],
    lock_file = "//:maven_core_lock.json",
    repositories = ["https://repo1.maven.org/maven2"],
)

maven.install(
    name = "maven_testing",
    artifacts = [
        "junit:junit:4.13.2",
        "org.mockito:mockito-core:4.6.1",
    ],
    lock_file = "//:maven_testing_lock.json",
    repositories = ["https://repo1.maven.org/maven2"],
)

use_repo(maven, "maven_core", "maven_testing")
```

## WORKSPACE Configuration (Legacy)

For older Bazel versions, the `WORKSPACE` file defines the same repositories.

## Using Dependencies in BUILD Files

```python
load("@maven_core//:defs.bzl", maven_core_artifacts = "artifacts")
load("@maven_testing//:defs.bzl", maven_testing_artifacts = "artifacts")

java_library(
    name = "core_lib",
    srcs = ["CoreLib.java"],
    deps = [
        maven_core_artifacts["com.google.guava:guava"],
        maven_core_artifacts["org.slf4j:slf4j-api"],
    ],
)

java_test(
    name = "example_test",
    srcs = ["ExampleTest.java"],
    deps = [
        ":core_lib",
        maven_testing_artifacts["junit:junit"],
        maven_testing_artifacts["org.mockito:mockito-core"],
    ],
)
```

## Updating Lock Files

### For Bzlmod (MODULE.bazel)

```bash
bazel run @maven_core//:pin
bazel run @maven_testing//:pin
```

### For WORKSPACE (Legacy)

```bash
bazel run @maven_core//:pin
bazel run @maven_testing//:pin
```

Each runs independently, keeping the lock files in sync with their respective artifacts.

## Key Differences: Bzlmod vs WORKSPACE

| Feature | Bzlmod (MODULE.bazel) | WORKSPACE |
|---------|----------------------|-----------|
| Bazel Version | 7+ (recommended) | < 7 (legacy) |
| Config File | MODULE.bazel | WORKSPACE |
| Extensions | `use_extension()` | Direct `load()` |
| Repository Setup | `use_repo()` | Implicit |
| Lock File Updates | `bazel run @name//:pin` | `bazel run @name//:pin` |

## When to Use Multiple Lock Files

This pattern is ideal when:

- You have clear separation between production and test dependencies
- Different teams manage different dependency sets
- You want to reduce the blast radius of dependency updates
- You need different resolution strategies for different artifact groups

## When to Use a Single Lock File

A single lock file is simpler when:

- You have a small number of dependencies
- All dependencies follow the same update cadence
- Simplicity is preferred over separation of concerns

## Migration from WORKSPACE to Bzlmod

To migrate an existing project:

1. Create `MODULE.bazel` with your `bazel_dep()` and extension calls
2. Keep `WORKSPACE` for backward compatibility (optional)
3. Use `.bazelrc` to control which system to use
4. Test with Bzlmod before making it default
5. Once validated, optionally remove `WORKSPACE` and legacy configuration

## References

- [Bazel Bzlmod Documentation](https://bazel.build/build/bzlmod)
- [Bazel rules_jvm_external Documentation](https://github.com/bazelbuild/rules_jvm_external)
- [Maven Lock Files](https://github.com/bazelbuild/rules_jvm_external#lock-file-format)
- [Bazel Extensions API](https://bazel.build/build/bzlmod#extensions)