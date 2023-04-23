import com.google.protobuf.gradle.id
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val querydslVersion: String by project
val redissonVersion: String by project

object Versions {
    const val armeria = "1.14.0"
    const val grpc = "1.44.0"
    const val grpcKotlin = "1.1.0"
    const val protobuf = "3.18.1"
    const val protobuf_gradle = "0.8.17"
}
object TestVersions {
    const val kotestVersion = "5.4.2"
}

protobuf {
    generatedFilesBaseDir = "$projectDir/build/generated/source"
    protoc {
        artifact = "com.google.protobuf:protoc:${Versions.protobuf}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${Versions.grpc}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${Versions.grpcKotlin}:jdk7@jar"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.generateDescriptorSet = true
            it.descriptorSetOptions.includeSourceInfo = true
            it.descriptorSetOptions.includeImports = true
            it.descriptorSetOptions.path = "$buildDir/resources/META-INF/armeria/grpc/service-name.dsc"
        }
    }
}

sourceSets.main {
    proto.srcDir("$projectDir/protos")
    java.srcDirs("src/main/java", "build/generated/java")
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
        kotlin.srcDirs("src/main/kotlin", "build/generated/kotlin")
    }
}

dependencies {
    implementation(project(":database"))

    // armeria
    implementation("com.linecorp.armeria:armeria:${Versions.armeria}")
    implementation("com.linecorp.armeria:armeria-spring-boot2-starter:${Versions.armeria}")
    implementation("com.linecorp.armeria:armeria-tomcat9:${Versions.armeria}")
    implementation("com.linecorp.armeria:armeria-grpc:${Versions.armeria}")

    // gRPC
    implementation("io.github.lognet:grpc-spring-boot-starter:4.6.0")
    implementation("io.grpc:grpc-kotlin-stub:1.1.0")
    implementation("io.grpc:grpc-stub:${Versions.grpc}")
    implementation("io.grpc:grpc-netty-shaded:${Versions.grpc}")
    implementation("com.google.protobuf:protobuf-java-util:${Versions.protobuf}")
    implementation("com.google.protobuf:protobuf-java")
    implementation("com.google.api.grpc:proto-google-common-protos")
    implementation("com.google.protobuf:protobuf-gradle-plugin:${Versions.protobuf_gradle}")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

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
        exclude("org.mongodb", "mongo-java-driver") // Compile error in MongoMetricsConnectionPoolListener
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
