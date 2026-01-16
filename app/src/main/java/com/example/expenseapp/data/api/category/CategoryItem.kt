package com.example.expenseapp.data.api.category

import com.example.expenseapp.data.ui.BottomSheetItem

data class CategoryItem(
    val __v: Int,
    val _id: String,
    val categoryName: String,
    val createdAt: String,
    val isIncome: Boolean,
    val updatedAt: String
)

fun CategoryItem.toBottomUi(categoryItem: CategoryItem) = BottomSheetItem(
    id = categoryItem._id,
    itemName = categoryItem.categoryName
)