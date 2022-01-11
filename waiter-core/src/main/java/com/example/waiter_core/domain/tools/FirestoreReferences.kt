package com.example.waiter_core.domain.tools

import android.annotation.SuppressLint
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.example.waiter_core.domain.tools.constants.FirestoreConstants

object FirestoreReferences {
    @SuppressLint("StaticFieldLeak")
    private var fireStore = FirebaseFirestore.getInstance()
    val employeesCollectionRef: CollectionReference =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_EMPLOYEES)
    val menuDocumentRef =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_DATA)
            .document(FirestoreConstants.DOCUMENT_MENU)
    val menuCollectionRef =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_MENU)
    val ordersCollectionRef =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_ORDERS)
}