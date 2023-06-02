 
package com.buzbuz.smartautoclicker.core.display.shadows;

import android.media.ImageReader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.Resetter;

/** [org.robolectric.shadow.api.Shadow] for [ImageReader] allowing to provide a mock on [ImageReader.newInstance] call. */
@Implements(value = ImageReader.class)
public class ShadowImageReader {

    /** The current mock for the image reader. */
    @Nullable
    private static ImageReader mockImageReader = null;

    /** The number of time [newInstance] have been called before the reset. */
    private static int sInstanceCreationCount = 0;

    /**
     * Method to be called from the tests to set the mock returned by the [ImageReader.newInstance] method.
     *
     * @param mock the mock to be provided to the tested code.
     */
    public static void setMockInstance(@NonNull ImageReader mock) {
        mockImageReader = mock;
    }

    /** @return the number of time [newInstance] have been called before the reset. */
    public static int getInstanceCreationCount() {
        return sInstanceCreationCount;
    }

    @NonNull
    @Implementation
    public static ImageReader newInstance(int width, int height, int format, int maxImages) {
        if (mockImageReader == null) {
            throw new IllegalStateException("Image reader is not mocked");
        }
        sInstanceCreationCount++;
        return mockImageReader;
    }

    /** Call this method between each tests to clean the mock. */
    @Resetter
    public static void reset() {
        mockImageReader = null;
        sInstanceCreationCount = 0;
    }
}
