import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("com.gradleup.shadow") version "8.3.3" // https://github.com/GradleUp/shadow
    id("net.kyori.blossom") version "2.1.0" // https://github.com/KyoriPowered/blossom
    `java-library`
    `maven-publish`
}

group = "com.andrew121410.mc"
version = "1.0"
description = "World1-6Essentials"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

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
        // Since we don't need to publish the shadow jar, we can just set the archiveFileName
        archiveFileName.set("World1-6Essentials.jar")

        // If we want to publish the shadow jar in the future, you can use the following code.
        // Yes all three of these are needed for JitPack to work.
//        archiveBaseName.set("World1-6Essentials")
//        archiveClassifier.set("")
//        archiveVersion.set("")

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
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
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
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils-Plugin:e8e9850ea7")
    compileOnly("com.github.World1-6.World1-6Utils:World1-6Utils_CMI_API:e8e9850ea7")
    compileOnly("net.essentialsx:EssentialsX:2.21.0-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:api:2.2.3-SNAPSHOT")
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