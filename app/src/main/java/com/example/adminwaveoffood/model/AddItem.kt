package com.example.adminwaveoffood.model

data class AddItem(
    var key: String? =null,
    var restaurantId: String? = null,
    var itemImage: String? = null,
    var title: String? = null,
    var price: String? = null,
    var description: String? = null
)
