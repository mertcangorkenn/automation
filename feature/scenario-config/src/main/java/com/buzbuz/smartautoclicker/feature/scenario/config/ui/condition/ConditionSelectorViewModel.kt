 
package com.buzbuz.smartautoclicker.feature.scenario.config.ui.condition

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Rect
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

import com.buzbuz.smartautoclicker.core.display.ScreenRecorder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConditionSelectorViewModel(application: Application) : AndroidViewModel(application)  {

    /** Provides screen images. */
    private val screenRecorder: ScreenRecorder = ScreenRecorder.getInstance()

    fun takeScreenshot(area: Rect, resultCallback: (Bitmap) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(200L)
            screenRecorder.takeScreenshot(area) { screenshot ->
                withContext(Dispatchers.Main) {
                    resultCallback(screenshot)
                }
            }
        }
    }
}