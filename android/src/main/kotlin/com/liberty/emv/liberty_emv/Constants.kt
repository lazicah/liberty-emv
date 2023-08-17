package com.liberty.emv.liberty_emv

import com.libertyPay.posSdk.domain.models.AccountType


class Constants {
    companion object {
        val accountTypeMapHorizon = mapOf(
           io.flutter.plugins.LibertyEmv.AccountType.SAVINGS to AccountType.SAVINGS,
                io.flutter.plugins.LibertyEmv.AccountType.CURRENT to AccountType.CURRENT,
                io.flutter.plugins.LibertyEmv.AccountType.CREDIT to AccountType.CREDIT,
                io.flutter.plugins.LibertyEmv.AccountType.DEFAULT_UNSPECIFIED to AccountType.DEFAULT_UNSPECIFIED,
                io.flutter.plugins.LibertyEmv.AccountType.INVESTMENT to AccountType.INVESTMENT,
                io.flutter.plugins.LibertyEmv.AccountType.UNIVERSAL to AccountType.UNIVERSAL,
        )

    }
}