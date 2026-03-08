package com.example.adminwaveoffood.model

data class AddItem(
    var key: String? ="",
    var restaurantId: String? = "",
    var itemImage: String? = "",
    var title: String? = "",
    var price: String? = "",
    var description: String? = ""
)
