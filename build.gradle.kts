import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

plugins {
    id("java")
    id("io.freefair.lombok") version "8.4"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "cn.nukkit"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.fusesource.jansi:jansi:2.4.0")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("org.yaml:snakeyaml:2.2")

    implementation("org.iq80.leveldb:leveldb:0.12") {
        exclude(group = "com.google.guava", module = "guava")
    }

    implementation("com.google.guava:guava:33.0.0-jre")
    implementation("io.netty:netty-all:4.1.104.Final")

    implementation("net.minecrell:terminalconsoleappender:1.3.0") {
        exclude(group = "org.apache.logging.log4j", module = "log4j-core")
        exclude(group = "org.jline", module = "jline-reader")
    }

    implementation("org.jline:jline:3.25.0") {
        exclude(group = "org.jline", module = "jline-terminal-jansi")
    }

    implementation("org.apache.logging.log4j:log4j-api:2.22.1")
    implementation("org.apache.logging.log4j:log4j-core:2.22.1")

    implementation("org.jetbrains:annotations:24.1.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType(JavaCompile::class.java) {
    options.encoding = "UTF-8"
}

tasks {
    shadowJar {
        transform(Log4j2PluginsCacheFileTransformer::class.java)
        manifest {
            attributes["Main-Class"] = "cn.nukkit.Nukkit"
            attributes["Multi-Release"] = true
        }
    }
}
