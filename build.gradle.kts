plugins {
    id("java")
    kotlin("jvm") version "1.9.10"
    id("dev.architectury.loom") version "1.6-SNAPSHOT"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    application
}

architectury {
    minecraft = "1.20.1"
}

group = "uk.minersonline.cube_tide"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        url = uri("https://repo.spongepowered.org/repository/maven-public/")
    }
    maven {
        url = uri("https://libraries.minecraft.net/")
    }
    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
    }
    mavenCentral()
}

loom {
    silentMojangMappingsLicense()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("net.bytebuddy:byte-buddy:1.12.12")
    implementation("net.bytebuddy:byte-buddy-agent:1.12.12")
    implementation("org.spongepowered:mixin:0.8.7")
    implementation("net.minecraft:launchwrapper:1.12")
    implementation("org.ow2.asm:asm:9.7.1")
    implementation("org.ow2.asm:asm-tree:9.7.1")
    implementation("org.ow2.asm:asm-commons:9.7.1")

    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.17.1")
    implementation("org.slf4j:slf4j-api:2.0.7") // Use the latest 2.x version
    implementation("org.slf4j:slf4j-simple:2.0.16")


    implementation("com.google.guava:guava:33.3.1-jre")

    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")

    "minecraft"("com.mojang:minecraft:1.20.1")
    "mappings"(loom.officialMojangMappings())
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("uk.minersonline.cube_tide.Main")
}