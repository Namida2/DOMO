package com.example.core.domain.entities

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class Settings @Inject constructor() {
    var tablesCount by Delegates.notNull<Int>()
    var guestsCount by Delegates.notNull<Int>()
}