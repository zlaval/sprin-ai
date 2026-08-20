plugins {
    java
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.zlrx"
version = "0.0.1-SNAPSHOT"
description = "spring-ai-course"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

extra["springAiVersion"] = "2.0.0"

dependencies {
    // implementation("org.springframework.boot:spring-boot-starter-actuator")
   // implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    //implementation("org.springaicommunity:mcp-client-security-spring-boot:0.1.14")
    //implementation("org.springaicommunity:mcp-server-security-spring-boot:0.1.14")
    //implementation("org.springframework.ai:spring-ai-starter-mcp-client")
   // implementation("org.springframework.ai:spring-ai-starter-mcp-server-webmvc")
    //implementation("org.springframework.ai:spring-ai-starter-model-chat-memory-repository-jdbc")
    implementation("org.springframework.ai:spring-ai-starter-model-openai")
   //implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")
    //implementation("org.springframework.ai:spring-ai-vector-store-advisor")
    implementation("me.paulschwarz:spring-dotenv:5.1.0")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
