import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import kotlin.collections.*

plugins {
	id("org.springframework.boot") version "3.0.1"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.jetbrains.kotlin.jvm") version "1.7.22"
	id("org.jetbrains.kotlin.plugin.spring") version "1.7.22"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.7.22"
	"kotlin-dsl"
}

group = "org.swamy"
version = "0.0.1-SNAPSHOT"
java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation ("org.springframework.boot:spring-boot-starter-validation")
	implementation ("org.springframework.boot:spring-boot-starter-web")
	implementation ("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation ("org.jetbrains.kotlin:kotlin-reflect")
	implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation ("io.github.microutils:kotlin-logging-jvm:3.0.4")
	runtimeOnly ("com.h2database:h2")
	runtimeOnly ("org.postgresql:postgresql")
	testImplementation ("org.springframework.boot:spring-boot-starter-test")
	testImplementation ("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("io.mockk:mockk:1.13.3")
	testImplementation("com.ninja-squad:springmockk:4.0.0")
}

/*tasks.withType(KotlinCompile) {
	kotlinOptions {
		freeCompilerArgs = ["-Xjsr305=strict"]
		jvmTarget = "17"
	}
}*/
tasks.withType<KotlinCompile> {
	// kotlinOptions.jvmTarget = "1.8"
	kotlinOptions.jvmTarget = "17"
}

tasks.test {
	useJUnitPlatform()
}

/*sourceSets{
	test {
		java {
			kotlin.setSrcDirs {kotlin.collections.AbstractList("src/test/intg", "src/test/unit")}
		}
	}
}*/
sourceSets {
	test {
		java {
			setSrcDirs(listOf("src/test/intg", "src/test/unit"))
		}
	}
}