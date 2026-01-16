package com.example.expenseapp.data.api.accountType

import com.example.expenseapp.data.api.category.Category
import com.example.expenseapp.data.api.category.CategoryItem
import com.example.expenseapp.data.api.category.toBottomUi
import com.example.expenseapp.data.ui.BottomSheetItem

class AccountType : ArrayList<AccountTypeItem>()

fun AccountType.toBottomUiList(accountType: ArrayList<AccountTypeItem>): ArrayList<BottomSheetItem> {
    val bottomSheetItemList = ArrayList<BottomSheetItem>()
    for (item in accountType) {
        val bottomSheetItem = item.toBottomUi(item)
        bottomSheetItemList.add(bottomSheetItem)
    }
    return bottomSheetItemList
}
