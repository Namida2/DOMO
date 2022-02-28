package com.example.core.domain.tools.constants


object FirestoreConstants {
    //Collections
    const val COLLECTION_DATA = "data"
    const val COLLECTION_MENU = "menu"
    const val COLLECTION_ORDERS= "orders"
    const val COLLECTION_DISHES = "dishes"
    const val COLLECTION_EMPLOYEES = "employees"
    const val COLLECTION_RESTAURANTS = "restaurants"
    const val COLLECTION_ORDER_ITEMS = "order_items"
    const val COLLECTION_LISTENERS = "listeners"
    //Documents
    const val DOCUMENT_DOMO = "domo"
    const val DOCUMENT_MENU = "menu"
    const val DOCUMENT_NEW_ORDERS_LISTENER = "newOrdersListener"
    //Fields
    const val FIELD_MENU_VERSION = "menuVersion"
    const val FIELD_GUESTS_COUNT = "guestsCount"
    const val FIELD_ORDER_ID = "orderId"
    const val FIELD_ORDER_INFO= "orderInfo"
    //Tools
    const val DOCUMENT_ORDER_ITEM_DELIMITER = "_"
}