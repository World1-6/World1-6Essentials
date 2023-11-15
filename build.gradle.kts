import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1" // https://github.com/johnrengelman/shadow
    id("net.kyori.blossom") version "2.1.0" // https://github.com/KyoriPowered/blossom
    `java-library`
    `maven-publish`
}

group = "com.andrew121410.mc"
version = "1.0"
description = "World1-6Essentials"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

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

    shadowJar {
        archiveFileName.set("World1-6Essentials.jar")

        relocate("org.bstats", "com.andrew121410.mc.world16essentials.bstats")
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

    maven {
        url = uri("https://repo.opencollab.dev/main/")
    }
}

dependencies {
    api("org.bstats:bstats-bukkit:3.0.2")
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils-Plugin:ebc08bc7e8")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils_CMI_API:ebc08bc7e8")
    compileOnly("net.essentialsx:EssentialsX:2.21.0-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:api:2.2.2-SNAPSHOT")
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