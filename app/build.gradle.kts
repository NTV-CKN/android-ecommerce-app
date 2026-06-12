import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.infix.phukiencongnghe"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.infix.phukiencongnghe"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists() && localPropertiesFile.canRead()) {
            properties.load(localPropertiesFile.inputStream())
        }
        val mapsApiKey = properties.getProperty("MAPS_API_KEY") ?: ""

        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

        buildConfigField("String", "MAPS_KEY", "\"$mapsApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    //google map platform
    implementation(libs.play.services.maps)
    implementation(libs.places)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.fragment)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}