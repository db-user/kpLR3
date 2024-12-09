plugins {
    kotlin("jvm") version "1.8.0" // або поточна версія Kotlin
    application
}

dependencies {
    implementation(kotlin("stdlib"))

    // Залежність для JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.0") // для параметризованих тестів

    // Залежність для плагіна Gradle
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

tasks.test {
    useJUnitPlatform()  // Увімкнути JUnit 5
}

project.configureDiktat()
project.createDiktatTask()
project.configureDetekt()
project.createDetektTask()