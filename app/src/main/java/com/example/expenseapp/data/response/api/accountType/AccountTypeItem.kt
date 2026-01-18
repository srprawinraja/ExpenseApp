package com.example.expenseapp.data.response.api.accountType

import com.example.expenseapp.data.ui.BottomSheetItem

data class AccountTypeItem(
    val __v: Int,
    val _id: String,
    val accountTypeName: String,
    val createdAt: String,
    val updatedAt: String
)
fun AccountTypeItem.toBottomUi(): BottomSheetItem =
    BottomSheetItem(
        id = _id,
        itemName = accountTypeName
    )
