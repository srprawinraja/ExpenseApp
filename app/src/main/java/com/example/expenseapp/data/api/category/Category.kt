package com.example.expenseapp.data.api.category

import com.example.expenseapp.data.ui.BottomSheetItem

class Category : ArrayList<CategoryItem>()

fun Category.toBottomUiList(categoryItems: ArrayList<CategoryItem>): ArrayList<BottomSheetItem> {
    val bottomSheetItemList = ArrayList<BottomSheetItem>()
    for (item in categoryItems) {
        val bottomSheetItem = item.toBottomUi(item)
        bottomSheetItemList.add(bottomSheetItem)
    }
    return bottomSheetItemList
}
