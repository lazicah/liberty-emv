package com.liberty.emv.liberty_emv

import com.libertyPay.horizonSDK.domain.models.AccountType


class Constants {
    companion object {
        val transactionTypeMap = mapOf(
            "SAVINGS" to AccountType.SAVINGS,
            "CURRENT" to AccountType.CURRENT,
            "CREDIT" to AccountType.CREDIT,
            "DEFAULT" to AccountType.DEFAULT_UNSPECIFIED,
        )

    }
}