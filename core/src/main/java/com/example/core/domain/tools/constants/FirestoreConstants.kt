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
    const val DOCUMENT_ORDER_ITEMS_STATE_LISTENER = "orderItemStateListener"
    const val DOCUMENT_PERMISSION_LISTENER = "permissionListener"
    const val DOCUMENT_NEW_EMPLOYEE_LISTENER = "newEmployeeListener"
    //Fields
    const val FIELD_MENU_VERSION = "menuVersion"
    const val FIELD_GUESTS_COUNT = "guestsCount"
    const val FIELD_ORDER_ID = "orderId"
    const val FIELD_ORDER_ITEM_ID = "orderItemId"
    const val FIELD_ORDER_INFO= "orderInfo"
    const val FIELD_ORDER_ITEM_INFO= "orderItemInfo"
    const val FIELD_ORDER_IS_READY= "ready"
    const val FIELD_PERMISSION= "permission"
    const val FIELD_EMAIL = "email"
    const val FIELD_NEW_PERMISSION= "newPermission"
    //Tools
    const val ORDER_ITEM_ID_DELIMITER = "_"
    const val EMPTY_COMMENTARY = ""
}