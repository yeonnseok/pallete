import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val clayVersion: String by project
val querydslVersion: String by project

object Versions {
    const val armeria = "1.14.0"
    const val opentracing = "0.33.0"
    const val grpc = "1.44.0"
}
object TestVersions {
    const val kotestVersion = "5.4.2"
}

dependencies {
    implementation(project(":database"))

    implementation("com.linecorp.armeria:armeria:${Versions.armeria}")
    implementation("com.linecorp.armeria:armeria-spring-boot2-starter:${Versions.armeria}")
    implementation("com.linecorp.armeria:armeria-tomcat9:${Versions.armeria}")
    implementation("com.linecorp.armeria:armeria-grpc:${Versions.armeria}")

    implementation("io.opentracing:opentracing-api:${Versions.opentracing}")
    implementation("io.opentracing:opentracing-util:${Versions.opentracing}")

    implementation("com.datadoghq:dd-trace-api:0.92.0")

    // gRPC
    implementation("se.ohou.mortar:mortar-libs-kotlin:1.+")
    implementation("io.grpc:grpc-netty-shaded:${Versions.grpc}")
    implementation("io.github.lognet:grpc-spring-boot-starter:4.6.0")
    // protobuf 메시지를 JSON 으로 출력할 때 JsonFormat util을 사용합니다.
    implementation("com.google.protobuf:protobuf-java-util")

    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // clay
    implementation("se.ohou.clay:armeria:$clayVersion")
    implementation("se.ohou.clay:grpc:$clayVersion")

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

sourceSets.main {
    java.srcDirs("src/main/java", "build/generated/java")
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
        kotlin.srcDirs("src/main/kotlin", "build/generated/kotlin")
    }
}

tasks {
    // gRPC descriptor file 을 Armeria DocService가 읽을 수 있는 위치로 이동
    register<Copy>("copyDescriptorSet") {
        dependsOn("mortarPrepare")
        from(layout.buildDirectory.dir("generated/image.bin"))
        into(layout.buildDirectory.dir("resources/main/META-INF/armeria/grpc"))
    }

    register<Exec>("mortarPrepare") {
        workingDir = projectDir
        commandLine = listOf("mortar", "prepare")
        environment("NO_TTY", "1")
    }

    withType<KotlinCompile> {
        dependsOn("mortarPrepare")
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    bootJar {
        archiveFileName.set(archiveBaseName.get() + "." + archiveExtension.get())
    }
}
