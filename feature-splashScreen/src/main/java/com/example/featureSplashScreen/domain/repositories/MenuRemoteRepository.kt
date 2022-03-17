package com.example.featureSplashScreen.domain.repositories

interface MenuRemoteRepository {
    fun readNewMenu(onComplete: () -> Unit)
    fun readMenuVersion(onComplete: (version: Long) -> Unit)
}