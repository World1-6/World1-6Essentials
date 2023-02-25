import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    `java-library`
    `maven-publish`
}

group = "com.andrew121410.mc"
version = "1.0"
description = "World1-6Essentials"
java.sourceCompatibility = JavaVersion.VERSION_17

tasks {
    build {
        dependsOn("shadowJar")
        dependsOn("processResources")
    }

    jar {
        enabled = false
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://repo.essentialsx.net/releases/")
    }

    maven {
        url = uri("https://repo.essentialsx.net/snapshots/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    api("org.bstats:bstats-bukkit:3.0.0")
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils-Plugin:d7edbd4838")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils_CMI_API:d7edbd4838")
    compileOnly("net.essentialsx:EssentialsX:2.20.0-SNAPSHOT")
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveBaseName.set("World1-6Essentials")
    archiveClassifier.set("")
    archiveVersion.set("")

    relocate("org.bstats", "com.andrew121410.mc.world16essentials.bstats")
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            from(components["java"])
            artifact(tasks.named("shadowJar"))
        }
    }
}

var date = Date()
var formattedDate: String = SimpleDateFormat("MM/dd/yyyy").format(date)

var fileName = "version.txt"

val genOutputDir = file("$buildDir/generated-resources")
val genOutputFile = file("$genOutputDir/$fileName")

// Generate the properties file
tasks.register("generateProperties") {
    outputs.dir(genOutputDir)
    outputs.file(genOutputFile)

    doLast {
        genOutputFile.parentFile.mkdirs()
        genOutputFile.writeText(formattedDate)
    }
}

// Copy the generated properties file to the resources folder
tasks.register("copyProperties") {
    dependsOn("generateProperties")
    inputs.file(genOutputFile)
    outputs.file("$buildDir/resources/main/$fileName")

    doLast {
        genOutputFile.copyTo(file("$buildDir/resources/main/$fileName"), overwrite = true)
    }
}

tasks.named("processResources") {
    dependsOn("copyProperties")
}