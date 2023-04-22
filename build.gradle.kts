import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    val kotlinVersion = "1.7.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.allopen") version kotlinVersion
    kotlin("plugin.noarg") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.springframework.boot") version "2.7.1"
    id("jacoco")
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("se.ohou.mortar.plugin") version "1.+"
    idea
}

group = "se.ohou.commerce"
version = "1.0-SNAPSHOT"

object TestVersions {
    const val kotestVersion = "5.4.2"
    const val mockkVersion = "1.12.7"
}

tasks {
    test {
        useJUnitPlatform()
    }

    // 단독 실행 가능한 jar 파일 생성 여부
    bootJar {
        enabled = false
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            html.required.set(false)
            xml.required.set(true)
        }
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://nexus.co-workerhou.se/repository/maven-releases")
        maven(url = "https://nexus.co-workerhou.se/repository/maven-snapshots")
        maven(url = "https://nexus.co-workerhou.se/repository/maven-public")
        mavenLocal()
    }

    tasks {
        withType<Assemble> {
            dependsOn("ktlintFormat")
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }

        withType<Test> {
            useJUnitPlatform()
            systemProperty("file.encoding", "UTF-8")
        }
    }
}

subprojects {
    apply {
        plugin("idea")
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.jetbrains.kotlin.plugin.allopen")
        plugin("org.jetbrains.kotlin.plugin.noarg")
        plugin("jacoco")
    }

    // 그룹이 동일하면 같은 이름의 모듈(ex. :common:api-model, :hashtag:api-model)이 dependency에 함께 추가되는 경우에
    // conflict(Circular dependency)가 발생할 수 있어서 그룹을 다르게 만든다.
    group = "se.ohou.commerce.${path.split(":")[1]}"
    version = "0.0.1-SNAPSHOT"

    tasks {
        withType<Jar> {
            // 기본 설정에서는 jar 파일 이름도 동일하게 생성될 수 있다. 동일한 jar 이름을 갖는 여러 dependency를 추가하면 오류가 발생하므로
            // 각각의 이름을 다르게 만들어준다.
            archiveFileName.set(
                project.path.split(":").drop(1).joinToString(separator = "-", postfix = "-") + project.version + ".jar"
            )
        }

        withType<Test> {
            useJUnitPlatform()
        }

        jacocoTestReport {
            dependsOn(test)
            reports {
                html.required.set(false)
                xml.required.set(true)
            }
        }
    }

    dependencies {
        // Kotlin
        implementation(kotlin("reflect"))
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.6.4")

        // kotest
        testImplementation("io.kotest:kotest-runner-junit5:${TestVersions.kotestVersion}")
        testImplementation("io.kotest:kotest-assertions-core:${TestVersions.kotestVersion}")
        testImplementation("io.mockk:mockk:${TestVersions.mockkVersion}")
    }

    configure<KtlintExtension> {
        filter {
            exclude { element -> element.file.path.contains("generated/") }
        }
    }
}
