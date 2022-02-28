package com.example.domo.registration.data

import android.view.View
import entities.PostItem
import com.example.core.domain.tools.constants.EmployeePosts
import javax.inject.Inject

class PostItemsRepository @Inject constructor(){
    fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(com.example.core.domain.tools.constants.EmployeePosts.COOK, View.VISIBLE),
            PostItem(com.example.core.domain.tools.constants.EmployeePosts.WAITER, View.INVISIBLE),
            PostItem(com.example.core.domain.tools.constants.EmployeePosts.ADMINISTRATOR, View.INVISIBLE)
        )
}