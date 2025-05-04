import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
}

//val keystorePropertiesFile: File = rootProject.file("keystore.properties")
//val keystoreProperties = Properties()
//if (keystorePropertiesFile.exists()) {
//    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
//}

android {
    namespace = "com.mckimquyen.gallery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mckimquyen.gallery"
        minSdk = 23
        targetSdk = 35
        versionName = "2025.05.04"
        versionCode = 20250504
        setProperty("archivesBaseName", "Gallery-$versionCode")
    }

//    signingConfigs {
//        if (keystorePropertiesFile.exists()) {
//            register("release") {
//                keyAlias = keystoreProperties.getProperty("keyAlias")
//                keyPassword = keystoreProperties.getProperty("keyPassword")
//                storeFile = file(keystoreProperties.getProperty("storeFile"))
//                storePassword = keystoreProperties.getProperty("storePassword")
//            }
//        }
//    }
    signingConfigs {
        register("release") {
            storeFile = file("keystores.jks")
            storePassword = "27072000"
            keyAlias = "mckimquyen"
            keyPassword = "27072000"
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        debug {
//            applicationIdSuffix = ".debug"
            buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-3940256099942544/6300978111\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID", "\"ca-app-pub-3940256099942544/9257395921\"")
        }
        release {
            //nho check APPLICATION_ID trong manifest
            buildConfigField("String", "ADMOB_BANNER_ID", "\"ca-app-pub-3612191981543807/5998696431\"")
            buildConfigField("String", "ADMOB_INTERSTITIAL_ID", "\"ca-app-pub-3612191981543807/2275073843\"")
            buildConfigField("String", "ADMOB_APP_OPEN_ID", "\"ca-app-pub-3612191981543807/7022620604\"")

            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//            if (keystorePropertiesFile.exists()) {
//                signingConfig = signingConfigs.getByName("release")
//            }
            signingConfig = signingConfigs.getByName("release")
        }
    }

    flavorDimensions.add("type")
    productFlavors {
        create("dev") {
            dimension = "type"
            buildConfigField("Boolean", "build_debug", "true")
            buildConfigField("String", "FLAVOR_buildEnv", "\"dev\"")
//        resValue("string", "app_name", "DEV")

            resValue("string", "SDK_KEY", "e75FnQfS9XTTqM1Kne69U7PW_MBgAnGQTFvtwVVui6kRPKs5L7ws9twr5IQWwVfzPKZ5pF2IfDa7lguMgGlCyt")
            resValue("string", "BANNER", "d7e35316c9287aec")
            resValue("string", "INTER", "e8473d63389dd0ae")

            resValue("string", "EnableAdInter", "true")
            resValue("string", "EnableAdBanner", "true")
        }

        create("prod") {
            dimension = "type"
            buildConfigField("Boolean", "build_debug", "false")
            buildConfigField("String", "FLAVOR_buildEnv", "\"prod\"")
//        resValue("string", "app_name", "PROD")

            resValue("string", "SDK_KEY", "e75FnQfS9XTTqM1Kne69U7PW_MBgAnGQTFvtwVVui6kRPKs5L7ws9twr5IQWwVfzPKZ5pF2IfDa7lguMgGlCyt")
            resValue("string", "BANNER", "d7e35316c9287aec")
            resValue("string", "INTER", "e8473d63389dd0ae")

            resValue("string", "EnableAdInter", "true")
            resValue("string", "EnableAdBanner", "true")
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    compileOptions {
        val currentJavaVersionFromLibs = JavaVersion.valueOf(libs.versions.app.build.javaVersion.get())
        sourceCompatibility = currentJavaVersionFromLibs
        targetCompatibility = currentJavaVersionFromLibs
    }

    dependenciesInfo {
        includeInApk = false
    }

    tasks.withType<KotlinCompile> {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    packaging {
        resources {
            excludes += "META-INF/library_release.kotlin_module"
        }
    }
}

dependencies {
//    api(libs.fossify.commons)
    //noinspection UseTomlInstead
    api("com.github.gj-loitp:lib_Commons:1.0.1")
    api(libs.android.image.cropper)
    api(libs.exif)
    api(libs.android.gif.drawable)
    api(libs.androidx.constraintlayout)
    api(libs.androidx.media3.exoplayer)
    api(libs.sanselan)
//    api(libs.imagefilters)
    api(libs.androidphotofilters)
    api(libs.androidsvg.aar)
    api(libs.gestureviews)
    api(libs.subsamplingscaleimageview)
    api(libs.androidx.swiperefreshlayout)
    api(libs.awebp)
    api(libs.apng)
    api(libs.avif.integration)
    api(libs.okio)
    api(libs.picasso) {
        exclude(group = "com.squareup.okhttp3", module = "okhttp")
    }
    compileOnly(libs.okhttp)
    ksp(libs.glide.compiler)
    api(libs.zjupure.webpdecoder)
    api(libs.bundles.room)
    //noinspection UseTomlInstead
//    api("com.applovin:applovin-sdk:13.1.0")
    implementation("com.google.android.gms:play-services-ads:24.2.0")
    ksp(libs.androidx.room.compiler)
    //noinspection UseTomlInstead
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
//    api("io.github.kakyire:app-review:2.0.1")
    implementation("com.google.android.play:review:2.0.2")
    implementation("com.google.android.play:review-ktx:2.0.2")
}
