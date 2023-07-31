package com.liberty.emv.liberty_emv

import com.libertyPay.horizonSDK.domain.models.AccountType


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

        val accountTypeMapNexgo = mapOf(
                io.flutter.plugins.LibertyEmv.AccountType.SAVINGS to com.libertypay.nexgoemvsdk.domain.models.AccountType.SAVINGS,
                io.flutter.plugins.LibertyEmv.AccountType.CURRENT to com.libertypay.nexgoemvsdk.domain.models.AccountType.CURRENT,
                io.flutter.plugins.LibertyEmv.AccountType.CREDIT to com.libertypay.nexgoemvsdk.domain.models.AccountType.CREDIT,
                io.flutter.plugins.LibertyEmv.AccountType.DEFAULT_UNSPECIFIED to com.libertypay.nexgoemvsdk.domain.models.AccountType.DEFAULT_UNSPECIFIED,
                io.flutter.plugins.LibertyEmv.AccountType.INVESTMENT to com.libertypay.nexgoemvsdk.domain.models.AccountType.INVESTMENT,
                io.flutter.plugins.LibertyEmv.AccountType.UNIVERSAL to com.libertypay.nexgoemvsdk.domain.models.AccountType.UNIVERSAL,
        )
    }
}