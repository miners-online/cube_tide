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