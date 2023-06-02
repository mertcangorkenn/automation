 
package com.buzbuz.smartautoclicker.feature.billing

import android.content.Context

import com.buzbuz.smartautoclicker.feature.billing.domain.BillingRepository

import kotlinx.coroutines.flow.Flow

interface IBillingRepository {

    companion object {

        /** Singleton preventing multiple instances of the repository at the same time. */
        @Volatile
        private var INSTANCE: BillingRepository? = null

        /**
         * Get the repository singleton, or instantiates it if it wasn't yet.
         *
         * @param context the Android context.
         *
         * @return the repository singleton.
         */
        fun getRepository(context: Context): BillingRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = BillingRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }

    val newPurchases: Flow<List<String>>

    /**
     * Returns whether or not the user has purchased ProMode.
     * @return a Flow that observes the product purchase state
     */
    val isProModePurchased: Flow<Boolean>

    /**
     * Returns whether or not the user can purchase a product.
     * @return a Flow that observes the ProMode purchase state
     */
    val canPurchaseProMode: Flow<Boolean>

    /** @return the PlayStore name of the pro mode. */
    val proModeTitle: Flow<String>
    /** @return the PlayStore price of the pro mode. */
    val proModePrice: Flow<String>
    /** @return the PlayStore description of the pro mode. */
    val proModeDescription: Flow<String>

    /**
     * Returns a Flow that reports if a billing flow is in process.
     *
     * @return Flow that indicates the known state of the billing flow.
     */
    val isBillingFlowInProcess: Flow<Boolean>

    /**
     * Launch the billing activity.
     *
     * @param context the Android context.
     * @param requestedAdvantage the pro mode advantage the user is trying to access.
     */
    fun startBillingActivity(context: Context, requestedAdvantage: ProModeAdvantage)
}