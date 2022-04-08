package com.example.featureSettings.domain.dto

import com.example.core.domain.entities.tools.SimpleTask
import com.google.firebase.firestore.CollectionReference

data class SaveMenuData(val targetCollectionRef: CollectionReference, val simpleTask: SimpleTask)