 
package com.buzbuz.smartautoclicker.feature.billing.domain

import android.content.Context

import com.buzbuz.smartautoclicker.feature.billing.IBillingRepository
import com.buzbuz.smartautoclicker.feature.billing.ProModeAdvantage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class BillingRepository(applicationContext: Context): IBillingRepository {

    override val newPurchases: Flow<List<String>> = flowOf(emptyList())

    override val isProModePurchased: Flow<Boolean> = flowOf(true)
    override val canPurchaseProMode: Flow<Boolean> = flowOf(false)

    override val proModeTitle: Flow<String> = flowOf("")
    override val proModePrice: Flow<String> = flowOf("")
    override val proModeDescription: Flow<String> = flowOf("")

    override val isBillingFlowInProcess: Flow<Boolean> = flowOf(false)
    override fun startBillingActivity(context: Context, requestedAdvantage: ProModeAdvantage) {}
}