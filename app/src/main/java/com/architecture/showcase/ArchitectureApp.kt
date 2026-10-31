package com.architecture.showcase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Android Architecture Showcase
 * Annotated with @HiltAndroidApp to trigger Hilt's code generation
 */
@HiltAndroidApp
class ArchitectureApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize app-wide components here if needed
    }
}
