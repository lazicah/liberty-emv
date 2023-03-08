package com.liberty.emv.liberty_emv

import com.libertyPay.horizonSDK.common.AccountTypes
import com.libertyPay.horizonSDK.domain.AccountType

class Constants {
    companion object {
        val transactionTypeMap = mapOf<String, AccountTypes>(
            "SAVINGS" to AccountTypes.SAVINGS,
            "CURRENT" to AccountTypes.CURRENT,
            "CREDIT" to AccountTypes.CREDIT,
            "DEFAULT" to AccountTypes.DEFAULT_UNSPECIFIED,
        )
    }
}