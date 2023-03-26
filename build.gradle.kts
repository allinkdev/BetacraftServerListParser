plugins {
    id("java-library")
    id("checkstyle")
    id("maven-publish")
}

group = "com.github.allinkdev"
version = "1.2.0"

java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.15.4")
}

tasks {
    assemble {
        dependsOn(checkstyleMain)
    }

    jar {
        from("UNLICENSE")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}