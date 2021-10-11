package com.example.domo.viewModels

import androidx.lifecycle.ViewModel
import com.example.domo.modeles.SplashScreenModel
import javax.inject.Inject

class SplashScreenViewModel(
    val splashScreenModel: SplashScreenModel
): ViewModel() {
    var data: String = "name"
}