plugins {
    `java-library`
    `maven-publish`
}

group = "com.andrew121410.mc"
version = "1.0"
description = "World1-6Essentials"
java.sourceCompatibility = JavaVersion.VERSION_17

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

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
