import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.gradleup.shadow") version "9.4.0" // https://github.com/GradleUp/shadow
    id("net.kyori.blossom") version "2.2.0" // https://github.com/KyoriPowered/blossom
    id("xyz.jpenilla.run-paper") version "3.0.2" // https://github.com/jpenilla/run-task
    `java-library`
    `maven-publish`
}

group = "com.andrew121410.mc"
version = "1.0"
description = "World1-6Essentials"

java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25

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
        options.release.set(25)
    }

    shadowJar {
        // Since we don't need to publish the shadow jar, we can just set the archiveFileName
        archiveFileName.set("World1-6Essentials.jar")

        // If we want to publish the shadow jar in the future, you can use the following code.
        // Yes all three of these are needed for JitPack to work.
//        archiveBaseName.set("World1-6Essentials")
//        archiveClassifier.set("")
//        archiveVersion.set("")

        relocate("org.bstats", "com.andrew121410.mc.world16essentials.bstats")
    }

    runServer {
        minecraftVersion("1.21.11")

        // Automatically download and install these plugins on the test server
        downloadPlugins {
            url("https://github.com/World1-6/World1-6Utils/releases/download/latest/World1-6Utils.jar")
        }
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
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
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

    maven {
        url = uri("https://repo.opencollab.dev/main/")
    }

    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    api("org.bstats:bstats-bukkit:3.0.2")
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils-Plugin:cbfa5ab713")
    compileOnly("org.geysermc.floodgate:api:2.2.5-SNAPSHOT")
    // Needed for data translator
    compileOnly("com.github.Zrips:CMILib:e4fc1e4e5c")
    compileOnly("com.github.Zrips:CMI-API:9a2c899c3d")
    compileOnly("net.essentialsx:EssentialsX:2.22.0-SNAPSHOT") {
        exclude(group = "org.spigotmc", module = "spigot-api")
    }
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            artifact(tasks.named("shadowJar"))
        }
    }
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