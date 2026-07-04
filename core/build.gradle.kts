plugins {
    `java-library`
    id("com.gradleup.shadow")
}

dependencies {
    implementation(project(":api"))

    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("me.unoprojects.unocore:api:1.0.0")
    compileOnly("dev.jorel:commandapi-paper-core:11.2.0")
    compileOnly("me.clip:placeholderapi:2.12.3")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    processResources {
        val props = mapOf("version" to version)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    jar {
        enabled = false
    }

    shadowJar {
        archiveBaseName.set("UnoEconomy")
        archiveClassifier.set("")
    }
}
