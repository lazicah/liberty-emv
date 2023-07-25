package com.liberty.emv.liberty_emv

import com.libertyPay.horizonSDK.domain.models.AccountType
import com.libertypay.posclient.api.Environment


class Constants {
    companion object {
        val transactionTypeMap = mapOf(
           io.flutter.plugins.LibertyEmv.AccountType.SAVINGS to AccountType.SAVINGS,
                io.flutter.plugins.LibertyEmv.AccountType.CURRENT to AccountType.CURRENT,
                io.flutter.plugins.LibertyEmv.AccountType.CREDIT to AccountType.CREDIT,
                io.flutter.plugins.LibertyEmv.AccountType.DEFAULT_UNSPECIFIED to AccountType.DEFAULT_UNSPECIFIED,
                io.flutter.plugins.LibertyEmv.AccountType.INVESTMENT to AccountType.INVESTMENT,
                io.flutter.plugins.LibertyEmv.AccountType.UNIVERSAL to AccountType.UNIVERSAL,

        )

        val enviromentTypeMap = mapOf(
                io.flutter.plugins.LibertyEmv.Environment.LIVE to Environment.Live,
                io.flutter.plugins.LibertyEmv.Environment.TEST to Environment.Test,
                )

    }
}