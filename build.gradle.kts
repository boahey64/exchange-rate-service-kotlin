import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.gradle.api.tasks.testing.logging.*
import com.moowork.gradle.node.yarn.YarnTask

plugins {
	id("org.springframework.boot") version "2.2.4.RELEASE"
	id("io.spring.dependency-management") version "1.0.9.RELEASE"


	id("org.gradle.kotlin.kotlin-dsl") version "1.2.9" apply false
	id("com.moowork.node") version "1.3.1"

	kotlin("jvm") version "1.3.61"
	kotlin("plugin.spring") version "1.3.61"
	kotlin("plugin.jpa") version "1.3.61"
}

group = "com.boahey"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

val developmentOnly by configurations.creating
configurations {
	runtimeClasspath {
		extendsFrom(developmentOnly)
	}
}

repositories {
	mavenCentral()
	jcenter()
}

node {
	// PLEASE use node LTS version or version with even major number
	version = "12.13.0"
	yarnVersion = "1.19.1"
	download = true
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.google.code.gson:gson:2.8.6")
	implementation("io.github.microutils:kotlin-logging:1.7.6")


	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")

	implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.12")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.1.51")


	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("junit:junit:4.12")
	testImplementation("com.nhaarman:mockito-kotlin-kt1.1:1.6.0")

	testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner:2.1.1.RELEASE") {
		exclude(module = "spring-cloud-stream-test-support")
		exclude(module = "spring-boot-starter-test")
		exclude(module = "spring-boot-test-autoconfigure")
		exclude(module = "spring-integration-java-dsl")
	}
	testImplementation("io.projectreactor:reactor-test:3.2.10.RELEASE")

//	val fluenleniumVersion = "3.7.1"

	val fluenleniumVersion = "4.3.1"
	val seleniumVersion = "3.141.59"
	testImplementation("org.fluentlenium:fluentlenium-junit:$fluenleniumVersion") {
		because("4.x requires Java 11")
	}
	testImplementation("org.fluentlenium:fluentlenium-assertj:$fluenleniumVersion") {
		because("4.x requires Java 11")
	}
	testImplementation("io.github.bonigarcia:webdrivermanager:3.3.0")
	testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumVersion")
	testImplementation("net.wuerl.kotlin:assertj-core-kotlin:0.2.1")
	testImplementation("com.nhaarman:mockito-kotlin-kt1.1:1.6.0")

}

tasks.withType<Test> {
	useJUnitPlatform()

	testLogging {
		lifecycle {
			events = mutableSetOf(FAILED, PASSED, SKIPPED)
			exceptionFormat = TestExceptionFormat.FULL
			showExceptions = true
			showCauses = true
			showStackTraces = true
			showStandardStreams = true
		}
		info.events = lifecycle.events
		info.exceptionFormat = lifecycle.exceptionFormat
	}

	// See https://github.com/gradle/kotlin-dsl/issues/836
	addTestListener(object : TestListener {
		override fun beforeSuite(suite: TestDescriptor) {}
		override fun beforeTest(testDescriptor: TestDescriptor) {}
		override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}

		override fun afterSuite(suite: TestDescriptor, result: TestResult) {
			if (suite.parent == null) { // root suite
				logger.lifecycle("----")
				logger.lifecycle("Test result: ${result.resultType}")
				logger.lifecycle("Test summary: ${result.testCount} tests, " +
						"${result.successfulTestCount} succeeded, " +
						"${result.failedTestCount} failed, " +
						"${result.skippedTestCount} skipped")
			}
		}
	})

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}

sourceSets {
	getByName("main") {
		output.dir(mapOf("builtBy" to "bundle"), "build/parcel")
	}
}

tasks {
	val bootRun by getting(org.springframework.boot.gradle.tasks.run.BootRun::class) {
		sourceResources(sourceSets["main"])

		jvmArgs = listOf("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005")
	}

	val bundle by registering(YarnTask::class) {
		description = "bundle the React Application"
		group = "Build"

		dependsOn("yarn_install")
		args = listOf("run", "bundle")

		inputs.dir("$projectDir/src/main/assets").withPathSensitivity(PathSensitivity.RELATIVE)
		inputs.dir("$projectDir/src/main/scss").withPathSensitivity(PathSensitivity.RELATIVE)
		inputs.dir("$projectDir/src/main/react").withPathSensitivity(PathSensitivity.RELATIVE)
		inputs.file("$projectDir/package.json").withPathSensitivity(PathSensitivity.RELATIVE)
		inputs.file("$projectDir/yarn.lock").withPathSensitivity(PathSensitivity.RELATIVE)

		outputs.dir("$buildDir/parcel/static/exchange-rate-service-kotlin/dist").withPropertyName("parcel")
		outputs.cacheIf { true }
	}

}