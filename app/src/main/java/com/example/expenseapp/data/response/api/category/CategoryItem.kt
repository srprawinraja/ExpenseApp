package com.example.expenseapp.data.response.api.category

import com.example.expenseapp.data.ui.BottomSheetItem

data class CategoryItem(
    val __v: Int,
    val _id: String,
    val categoryName: String,
    val createdAt: String,
    val isIncome: Boolean,
    val updatedAt: String
)

fun CategoryItem.toBottomUi(): BottomSheetItem =
    BottomSheetItem(
        id = _id,
        itemName = categoryName
    )
