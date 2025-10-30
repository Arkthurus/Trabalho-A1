// build.gradle.kts (app)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // 1. KSP - Mantido o mais recente
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 36
    // ... (restante do bloco android)
    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- DEPENDÊNCIAS PADRÃO ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.ui.tooling)
    implementation(libs.protolite.well.known.types)
    implementation(libs.androidx.constraintlayout)

    // --- CORREÇÃO DO FIREBASE ---
    // 1. Adiciona o Firebase BOM para gerenciar versões de forma estável (VERSÃO ATUALIZADA)
    val firebaseBom = platform("com.google.firebase:firebase-bom:33.1.0")
    implementation(firebaseBom)

    // 2. Importa o Firestore SEM especificar a versão (o BOM cuidará disso)
    implementation(libs.google.firebase.firestore.ktx)

    // REMOVIDA: implementação(libs.firebase.firestore.ktx) - para evitar conflito com o Catalog e a versão instável

    // --- DEPENDÊNCIAS DO ROOM (OBRIGATÓRIAS) ---
    // REMOVIDA: val roomVersion = "2.8.1" (Variável não utilizada)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // --- DEPENDÊNCIAS DE NAVEGAÇÃO E TERCEIROS ---
    implementation(libs.accompanist.pager)

    // REMOVIDA: val navVersion = "2.9.5" (Variável não utilizada)
    implementation(libs.androidx.navigation.compose)

    // --- TESTES ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}