package com.example.domo.models.remoteRepository

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class OrderMenuDialogRemoteRepository @Inject constructor(
    private val fireStore: FirebaseFirestore,
) {

}