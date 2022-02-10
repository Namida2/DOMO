package com.example.featureOrder.domain.di.modules

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

//TODO: Remove this module and provide the deps from the main module
@Module
class FirebaseModule {
    @Provides
    @Singleton
    fun bindFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    @Provides
    @Singleton
    fun bindFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
}