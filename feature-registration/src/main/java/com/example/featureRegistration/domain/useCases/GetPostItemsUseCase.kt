package com.example.featureRegistration.domain.useCases

import android.view.View
import com.example.core.domain.tools.constants.EmployeePosts
import com.example.featureRegistration.data.PostItemsRepository
import com.example.featureRegistration.domain.PostItem
import javax.inject.Inject

class GetPostItemsUseCaseImpl @Inject constructor(
    private val repository: PostItemsRepository
) : GetPostItemsUseCase {
    override fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(EmployeePosts.COOK.value, View.VISIBLE),
            PostItem(EmployeePosts.WAITER.value, View.INVISIBLE),
            PostItem(EmployeePosts.ADMINISTRATOR.value, View.INVISIBLE)
        )
}

interface GetPostItemsUseCase {
    fun getPostItems(): MutableList<PostItem>
}