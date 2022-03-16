package com.example.core.domain.tools

import android.annotation.SuppressLint
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.example.core.domain.tools.constants.FirestoreConstants

object FirestoreReferences {
    @SuppressLint("StaticFieldLeak")
    var fireStore = FirebaseFirestore.getInstance()
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
    val newOrdersListenerDocumentRef =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_LISTENERS)
            .document(FirestoreConstants.DOCUMENT_NEW_ORDERS_LISTENER)
    val orderItemsStateListenerDocumentRef =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_LISTENERS)
            .document(FirestoreConstants.DOCUMENT_ORDER_ITEMS_STATE_LISTENER)
    val newPermissionDocumentRef =
        fireStore.collection(FirestoreConstants.COLLECTION_RESTAURANTS)
            .document(FirestoreConstants.DOCUMENT_DOMO)
            .collection(FirestoreConstants.COLLECTION_LISTENERS)
            .document(FirestoreConstants.DOCUMENT_PERMISSION_LISTENER)
}