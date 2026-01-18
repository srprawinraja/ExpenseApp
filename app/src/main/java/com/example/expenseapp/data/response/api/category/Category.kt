package com.example.expenseapp.data.response.api.category

import com.example.expenseapp.data.ui.BottomSheetItem

class Category : ArrayList<CategoryItem>()

fun List<CategoryItem>.toBottomUiList(): List<BottomSheetItem> =
    map { it.toBottomUi() }
