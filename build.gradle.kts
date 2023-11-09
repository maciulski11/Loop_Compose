buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}

plugins {
    id("com.android.application") version "8.2.0-rc01" apply false
    id("com.android.library") version "8.2.0-rc01" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}