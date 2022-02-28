package com.example.domo.registration.domain

import android.view.View
import com.example.domo.registration.data.PostItemsRepository
import entities.PostItem
import com.example.core.domain.tools.constants.EmployeePosts
import javax.inject.Inject

class GetPostItemsUseCaseImpl @Inject constructor (
    private val repository: PostItemsRepository
): GetPostItemsUseCase {
    override fun getPostItems(): MutableList<PostItem> =
        mutableListOf(
            PostItem(com.example.core.domain.tools.constants.EmployeePosts.COOK, View.VISIBLE),
            PostItem(com.example.core.domain.tools.constants.EmployeePosts.WAITER, View.INVISIBLE),
            PostItem(com.example.core.domain.tools.constants.EmployeePosts.ADMINISTRATOR, View.INVISIBLE)
        )
}
interface GetPostItemsUseCase {
    fun getPostItems(): MutableList<PostItem>
}