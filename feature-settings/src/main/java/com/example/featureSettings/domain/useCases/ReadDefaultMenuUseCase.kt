package com.example.featureSettings.domain.useCases

import com.example.featureSettings.domain.repositories.MenuRemoteRepository
import javax.inject.Inject

class ReadDefaultMenuUseCase @Inject constructor(
    private val menuRemoteRepository: MenuRemoteRepository
){
    fun readDefaultMenu() {

    }
}