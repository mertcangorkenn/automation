 
package com.buzbuz.smartautoclicker.feature.billing.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.buzbuz.smartautoclicker.feature.billing.ProModeAdvantage
import com.buzbuz.smartautoclicker.feature.billing.R

class ProModeBillingActivity : AppCompatActivity() {

    companion object {

        /** Intent extra key for the billing reason. */
        private const val EXTRA_BILLING_REASON =
            "com.buzbuz.smartautoclicker.feature.billing.ui.EXTRA_BILLING_REASON"
        /**
         * Intent extra key for limitations. If true, the billing reason is of type [ProModeAdvantage.Limitation].
         * If not, it is of type [ProModeAdvantage.Feature].
         */
        private const val EXTRA_IS_LIMITATION =
            "com.buzbuz.smartautoclicker.feature.billing.ui.EXTRA_IS_LIMITATION"

        /**
         * Get the intent for starting this activity.
         *
         * @param context the Android context.
         * @param advantage the reason for displaying the billing activity.
         *
         * @return the intent, ready to be sent.
         */
        internal fun getStartIntent(context: Context, advantage: ProModeAdvantage): Intent =
            Intent(context, ProModeBillingActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra(EXTRA_BILLING_REASON, advantage.toString())
                .putExtra(EXTRA_IS_LIMITATION, advantage is ProModeAdvantage.Limitation)

        /** Get the billing reason from the intent extra. */
        private fun Intent.getBillingReasonExtra(): ProModeAdvantage {
            val reasonString = getStringExtra(EXTRA_BILLING_REASON)
            if (reasonString == null || !hasExtra(EXTRA_IS_LIMITATION))
                throw IllegalStateException("ProModeBillingActivity started without a billing reason")

            return if (getBooleanExtra(EXTRA_IS_LIMITATION, false)) ProModeAdvantage.Limitation.valueOf(reasonString)
                else ProModeAdvantage.Feature.valueOf(reasonString)
        }
    }

    /** The view model shared between this activity and the dialog fragment. */
    private val billingViewModel: ProModeBillingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pro_mode)

        // Set the billing reason in the view model, it will be propagated to the dialog.
        billingViewModel.setBillingReason(intent.getBillingReasonExtra())

        lifecycle.addObserver(billingViewModel)

        // Creates and show the dialog.
        ProModeBillingDialogFragment()
            .show(supportFragmentManager, ProModeBillingDialogFragment.FRAGMENT_TAG)
    }
}
