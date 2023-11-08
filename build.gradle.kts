import net.raphimc.javadowngrader.gradle.task.DowngradeJarTask
import java.text.SimpleDateFormat
import java.util.*

plugins {
    `java-library`
    `maven-publish`
    id("io.freefair.lombok") version "8.4" // https://plugins.gradle.org/plugin/io.freefair.lombok
    id("com.github.johnrengelman.shadow") version "8.1.1" // https://github.com/johnrengelman/shadow
    id("net.raphimc.java-downgrader") version "1.1.1" // https://github.com/RaphiMC/JavaDowngrader
    id("net.kyori.blossom") version "2.1.0" // https://github.com/KyoriPowered/blossom
}

group = "com.andrew121410.mc"
version = "1.0"
description = "World1-6Essentials"

// Set to Java 17
java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.codemc.io/repository/nms/")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    api("org.bstats:bstats-bukkit:3.0.2")
    api("org.xerial:sqlite-jdbc:3.43.2.2") // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc

    // Paper goes first then CraftBukkit
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.bukkit:craftbukkit:1.12.2-R0.1-SNAPSHOT")
//    compileOnly("org.spigotmc:spigot:1.12.2-R0.1-SNAPSHOT")

    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils_CMI_API:a1c6ad2df7")
}

tasks {
    build {
        dependsOn(shadowJar)
        dependsOn("processResources")
        finalizedBy("java8Jar")
    }

    jar {
        enabled = false
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        archiveFileName.set("World1-6Essentials.jar")

        relocate("org.bstats", "com.andrew121410.mc.world16essentials.bstats")
    }
}

// Downgrade to Java 8
tasks.register<DowngradeJarTask>("java8Jar") {
    input = tasks.shadowJar.get().archiveFile.get().asFile
    outputSuffix.set("+java8")
    // Set compile classpath to the same as the main source set
    compileClassPath = project.sourceSets["main"].compileClasspath
}.configure {
    dependsOn("build")
}

var formattedDate: String = SimpleDateFormat("M/d/yyyy").format(Date())
sourceSets {
    main {
        blossom {
            javaSources {
                property("date_of_build", formattedDate)
            }
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            artifact(tasks.named("shadowJar"))
        }
    }
}
