package com.example.adminwaveoffood.model

data class CategoryModel(
    var foodCategoryName: String? = null,
    var foodCategoryImage: String? = null,
    var key: String? = null,
    var isSelected: Boolean = false
)