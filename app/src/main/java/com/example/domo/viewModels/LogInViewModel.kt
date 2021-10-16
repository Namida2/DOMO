package com.example.domo.viewModels

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domo.views.log

class LogInViewModel: ViewModel() {

    private var _data: MutableLiveData<String> = MutableLiveData("default")
    val data = _data

    fun onClick(view: View, string: String) {
        view.isEnabled = false
        log(string)
    }

}