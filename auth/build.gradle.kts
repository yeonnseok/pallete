
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val querydslVersion: String by project
val redissonVersion: String by project

object TestVersions {
    const val kotestVersion = "5.4.2"
}

dependencies {
    implementation(project(":database"))

    // spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // mongodb
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    // mongodb memory db for test
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.5.0")

    // querydsl
    kapt("com.querydsl:querydsl-apt:$querydslVersion:general")
    implementation("com.querydsl:querydsl-mongodb:$querydslVersion") {
        exclude("org.mongodb", "mongo-java-driver")
    }

    implementation("io.projectreactor.tools:blockhound:1.0.6.RELEASE")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.kotest:kotest-runner-junit5:${TestVersions.kotestVersion}")
    testImplementation("io.kotest:kotest-assertions-core:${TestVersions.kotestVersion}")
}

tasks {

    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    bootJar {
        archiveFileName.set(archiveBaseName.get() + "." + archiveExtension.get())
    }
}
