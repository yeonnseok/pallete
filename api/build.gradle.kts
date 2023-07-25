import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val querydslVersion: String by project
val redissonVersion: String by project
val jjwtVersion: String by project

object TestVersions {
    const val kotestVersion = "5.4.2"
}

dependencies {
    implementation(project(":database"))

    // spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt:$jjwtVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    compileOnly("javax.servlet:javax.servlet-api")
    compileOnly("io.projectreactor:reactor-core")

    // redis
    implementation("org.redisson:redisson-spring-boot-starter:$redissonVersion")

    // mongodb
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")

    // mongodb memory db for test
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.5.0")

    // querydsl
    kapt("com.querydsl:querydsl-apt:$querydslVersion:general")
    implementation("com.querydsl:querydsl-mongodb:$querydslVersion") {
        exclude("org.mongodb", "mongo-java-driver")
    }

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
