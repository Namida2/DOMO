package com.example.featureRegistration.data

import android.view.View
import com.example.featureRegistration.domain.PostItem
import com.example.core.domain.tools.constants.EmployeePosts
import javax.inject.Inject

class PostItemsRepository @Inject constructor(){
    fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(EmployeePosts.COOK, View.VISIBLE),
            PostItem(EmployeePosts.WAITER, View.INVISIBLE),
            PostItem(EmployeePosts.ADMINISTRATOR, View.INVISIBLE)
        )
}