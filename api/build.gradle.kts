plugins {
    `java-library`
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
    compileOnly("me.unoprojects.unocore:api:1.0.0")
    compileOnly("dev.jorel:commandapi-paper-core:11.2.0")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}
