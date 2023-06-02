 
package com.buzbuz.smartautoclicker.feature.billing.data

import android.os.Handler
import android.os.Looper
import android.util.Log

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult

import kotlin.math.min

internal class BillingServiceConnection(
    private val billingClient: BillingClient,
    private val onBillingClientConnected: () -> Unit,
): BillingClientStateListener {

    /** Main thread handler. */
    private val handler = Handler(Looper.getMainLooper())
    /** How long before the data source tries to reconnect to Google play. */
    private var reconnectMilliseconds = RECONNECT_TIMER_START_MILLISECONDS

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        val responseCode = billingResult.responseCode
        val debugMessage = billingResult.debugMessage

        Log.d(LOG_TAG, "onBillingSetupFinished: $responseCode $debugMessage")

        when (responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                // The billing client is ready. You can query purchases here.
                // This doesn't mean that your app is set up correctly in the console -- it just
                // means that you have a connection to the Billing service.
                reconnectMilliseconds = RECONNECT_TIMER_START_MILLISECONDS
                onBillingClientConnected()
            }
            else -> retryBillingServiceConnectionWithExponentialBackoff()
        }
    }

    override fun onBillingServiceDisconnected() {
        retryBillingServiceConnectionWithExponentialBackoff()
    }

    /**
     * Retries the billing service connection with exponential backoff, maxing out at the time
     * specified by RECONNECT_TIMER_MAX_TIME_MILLISECONDS.
     */
    private fun retryBillingServiceConnectionWithExponentialBackoff() {
        handler.postDelayed(
            { billingClient.startConnection(this@BillingServiceConnection) },
            reconnectMilliseconds,
        )
        reconnectMilliseconds = min(reconnectMilliseconds * 2, RECONNECT_TIMER_MAX_TIME_MILLISECONDS)
    }
}

private const val LOG_TAG = "BillingServiceConnection"
private const val RECONNECT_TIMER_START_MILLISECONDS = 1000L
private const val RECONNECT_TIMER_MAX_TIME_MILLISECONDS = 1000L * 60L * 15L // 15 minutes
