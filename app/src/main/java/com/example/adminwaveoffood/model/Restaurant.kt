package com.example.adminwaveoffood.model

data class Restaurant(
    var key: String? = null,
    var restaurantImage: String? = null,
    var restaurantName: String? = null,
    var restaurantPhone: String? = null,
    var restaurantAddress: String? = null,
    var restaurantDeliveryRadius: String? = null,
    val restaurantDeliveryDuration: String? = null,
    var restaurantCategories: Map<String, Boolean>? = null,
    var restaurantOpeningTime: String? = null,
    var restaurantClosingTime: String? = null,
//    val restaurantStatus: Boolean = false

)
