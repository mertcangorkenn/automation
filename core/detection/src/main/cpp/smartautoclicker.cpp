 

#include <android/log.h>
#include <android/bitmap.h>
#include <jni.h>
#include <opencv2/imgproc/imgproc.hpp>
#include <string>

#include "detector.hpp"

using namespace smartautoclicker;

/**
 * This function is a helper providing the boiler plate code to return the native object from Java object.
 * The "nativePtr" is reached from this code, casted to Detector's pointer and returned. This will be used in
 * all our native methods wrappers to recover the object before invoking it's methods.
 */
static Detector *getObject(JNIEnv *env, jobject self) {
    jclass cls = env->GetObjectClass(self);
    if (!cls)
        env->FatalError("GetObjectClass failed");

    jfieldID nativeObjectPointerID = env->GetFieldID(cls, "nativePtr", "J");
    if (!nativeObjectPointerID)
        env->FatalError("GetFieldID failed");

    jlong nativeObjectPointer = env->GetLongField(self, nativeObjectPointerID);
    return reinterpret_cast<Detector *>(nativeObjectPointer);
}

static void setDetectionResult(JNIEnv *env, jobject self, DetectionResult result) {
    jclass cls = env->GetObjectClass(self);
    if (!cls)
        env->FatalError("GetObjectClass failed");

    jmethodID methodId = env->GetMethodID(cls, "setResults", "(ZIID)V");

    env->CallVoidMethod(self, methodId, result.isDetected, (int) result.centerX, (int) result.centerY, result.maxVal);
}

extern "C" {
    JNIEXPORT jlong JNICALL Java_com_buzbuz_smartautoclicker_core_detection_NativeDetector_newDetector(
            JNIEnv *env,
            jobject self
    ) {
        return reinterpret_cast<jlong>(new Detector());
    }

    JNIEXPORT void JNICALL Java_com_buzbuz_smartautoclicker_core_detection_NativeDetector_updateScreenMetrics(
            JNIEnv *env,
            jobject self,
            jobject screenBitmap,
            jdouble detectionQuality
    ) {
        getObject(env, self)->setScreenMetrics(env, screenBitmap, detectionQuality);
    }

    JNIEXPORT void JNICALL Java_com_buzbuz_smartautoclicker_core_detection_NativeDetector_setScreenImage(
            JNIEnv *env,
            jobject self,
            jobject screenBitmap
    ) {
        getObject(env, self)->setScreenImage(env, screenBitmap);
    }

    JNIEXPORT void JNICALL Java_com_buzbuz_smartautoclicker_core_detection_NativeDetector_detect(
            JNIEnv *env,
            jobject self,
            jobject conditionBitmap,
            jint threshold,
            jobject result
    ) {
        setDetectionResult(env, result, getObject(env, self)->detectCondition(env, conditionBitmap, threshold));
    }

    JNIEXPORT void JNICALL Java_com_buzbuz_smartautoclicker_core_detection_NativeDetector_detectAt(
            JNIEnv *env,
            jobject self,
            jobject conditionBitmap,
            jint x,
            jint y,
            jint width,
            jint height,
            jint threshold,
            jobject result
    ) {
        setDetectionResult(env, result, getObject(env, self)->detectCondition(env, conditionBitmap, x, y, width, height, threshold));
    }

    JNIEXPORT void JNICALL Java_com_buzbuz_smartautoclicker_core_detection_NativeDetector_deleteDetector(
            JNIEnv *env,
            jobject self
    ) {
        delete getObject(env, self);
    }
}
