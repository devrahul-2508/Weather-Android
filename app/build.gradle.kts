import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id("com.android.application")
  id("kotlin-android")
}

android {
  compileSdk = 32
  buildToolsVersion = "30.0.3"

  defaultConfig {
    applicationId = "com.example.weather"
    minSdk = 21
    targetSdk = 32
    versionCode = 1
    versionName = "1.1"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    // TODO: Throw this in some 🔐 vault
    val apiKey: String = gradleLocalProperties(rootDir).getProperty("api_key")

    buildConfigField("String", "API_BASE_URL", "\"https://api.openweathermap.org/data/2.5/\"")
    buildConfigField("String", "API_KEY", apiKey)
  }

  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("test").java.srcDirs("src/test/kotlin")
  }

  buildTypes { }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation("androidx.appcompat:appcompat:1.2.0")
  implementation("androidx.cardview:cardview:1.0.0")
  implementation("androidx.constraintlayout:constraintlayout:2.0.1")
  implementation("androidx.core:core-ktx:1.3.1")
  implementation("com.google.android.material:material:1.2.1")
  implementation("com.squareup.okhttp3:okhttp:4.9.0")
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
  implementation("com.squareup.moshi:moshi-adapters:1.11.0")
  implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.10")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")

  testImplementation("junit:junit:4.+")
}
