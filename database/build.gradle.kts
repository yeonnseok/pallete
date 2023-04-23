val queryDslVersion = "5.0.0"

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}

allOpen {
    // 특정 annotation 이 붙은 class 에 한하여 open class 로 만들어줍니다. (ORM 지연로딩)
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("org.springframework.data.mongodb.core.mapping.Document")
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:general")
    implementation("com.querydsl:querydsl-mongodb:$queryDslVersion") {
        exclude("org.mongodb", "mongo-java-driver")
    }
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}
