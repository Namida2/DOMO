package com.example.core.domain.listeners

import kotlinx.coroutines.flow.Flow

interface MenuVersionListener {
    val menuVersionChanges: Flow<Long>
}