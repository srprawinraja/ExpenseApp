package com.example.expenseapp.data.response.api.accountType

import com.example.expenseapp.data.ui.BottomSheetItem

class AccountType : ArrayList<AccountTypeItem>()

fun AccountType.toBottomUiList(): List<BottomSheetItem> {
    return map { it.toBottomUi() }
}
