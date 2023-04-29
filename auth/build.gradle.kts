dependencies {
    implementation(project(":database"))

    implementation("org.springframework.boot:spring-boot-starter-security")
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}
