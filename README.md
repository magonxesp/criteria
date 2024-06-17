# Criteria

The Criteria design pattern helps us to maintain a single form to perform search queries on our database.

It helps to implement the repository design pattern, avoiding to create a bunch of methods to create the same query with different filters.

## Installation

Add the dependency on your `build.gradle.kts`

```kotlin
dependencies {
    implementation("io.github.magonxesp:criteria-core:0.3.0")
}
```

For Spring Boot and Spring Data JPA add the spring-boot dependency.

```kotlin
dependencies {
    implementation("io.github.magonxesp:criteria-spring-boot:0.3.0")
}
```

## Usage

[Read the docs](https://magonxesp.gitbook.io/criteria/).

## Development

[See the development guide](./docs/development.md)
