import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val querydslVersion: String by project
val redissonVersion: String by project

object Versions {
    const val armeria = "1.20.3"
    const val grpcProto = "1.44.0"
    const val grpc = "3.19.6"
    const val grpcKotlin = "1.2.1"
    const val protobuf = "3.18.1"
}
object TestVersions {
    const val kotestVersion = "5.4.2"
}

protobuf {
    generatedFilesBaseDir = "$projectDir/build/generated"
    protoc {
        artifact = "com.google.protobuf:protoc:${Versions.grpc}"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${Versions.grpcProto}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${Versions.grpcKotlin}:jdk7@jar"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
            }

            it.generateDescriptorSet = true
            it.descriptorSetOptions.includeSourceInfo = true
            it.descriptorSetOptions.includeImports = true
            it.descriptorSetOptions.path = "$buildDir/resources/META-INF/armeria/grpc/service-name.dsc"
        }
    }

    sourceSets.main {
        proto.srcDir("$projectDir/protos")
        java {
            srcDirs(
                "build/generated/source/proto/main/java",
                "build/generated/source/proto/main/kotlin",
            )
        }
    }
}

dependencies {
    implementation(project(":database"))

    // armeria
    implementation("com.linecorp.armeria:armeria:${Versions.armeria}")
    implementation("com.linecorp.armeria:armeria-spring-boot2-webflux-starter:${Versions.armeria}")
    implementation("com.linecorp.armeria:armeria-grpc:${Versions.armeria}")

    // gRPC
    implementation(platform("io.grpc:grpc-bom:${Versions.grpcProto}"))
    implementation("io.grpc:grpc-kotlin-stub:${Versions.grpcKotlin}")
    implementation("io.grpc:grpc-stub:${Versions.grpcProto}")
    implementation("io.grpc:grpc-protobuf:${Versions.grpcProto}")
    implementation("io.grpc:grpc-netty-shaded:${Versions.grpcProto}")
    implementation("io.github.lognet:grpc-spring-boot-starter:4.6.0")
    implementation("com.google.protobuf:protobuf-java-util:${Versions.grpc}")
    implementation("com.google.protobuf:protobuf-kotlin:${Versions.grpc}")
    implementation("com.google.protobuf:protobuf-java")
    implementation("com.google.api.grpc:proto-google-common-protos")
    implementation("com.google.protobuf:protobuf-gradle-plugin:0.8.17")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    // redis
    implementation("org.redisson:redisson-spring-boot-starter:$redissonVersion")

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

configurations.forEach {
    if (it.name.toLowerCase().contains("proto")) {
        it.attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, "java-runtime"))
    }
}

tasks {

    register<Copy>("copyDescriptorSet") {
        from(layout.buildDirectory.dir("generated/image.bin"))
        into(layout.buildDirectory.dir("resources/main/META-INF/armeria/grpc"))
    }

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
