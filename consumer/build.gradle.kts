plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":database"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.kafka:spring-kafka")

    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
}

tasks {
    bootJar {
        archiveFileName.set(archiveBaseName.get() + "." + archiveExtension.get())
        enabled = true
    }

    register("prepareKotlinBuildScriptModel")

    jar {
        enabled = false
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    clean {
        delete = setOf("build", "out")
    }
}

idea {
    // setting for queryDsl
    module {
        val kaptMain = file("build/generated/source/kapt/main")
        sourceDirs.add(kaptMain)
        generatedSourceDirs.add(kaptMain)
    }
}
