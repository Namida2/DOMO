package com.example.domo.registration.data

import android.view.View
import entities.PostItem
import com.example.waiterCore.domain.tools.constants.EmployeePosts
import javax.inject.Inject

class PostItemsRepository @Inject constructor(){
    fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(EmployeePosts.COOK, View.VISIBLE),
            PostItem(EmployeePosts.WAITER, View.INVISIBLE),
            PostItem(EmployeePosts.ADMINISTRATOR, View.INVISIBLE)
        )
}