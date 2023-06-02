 

#include <jni.h>
#include <opencv2/imgproc/imgproc.hpp>

namespace smartautoclicker {

    class DetectionResult {
    public:
        bool isDetected;
        double centerX;
        double centerY;

        double minVal;
        double maxVal;
        cv::Point minLoc;
        cv::Point maxLoc;

        void reset() {
            isDetected = false;
            centerX = 0;
            centerY = 0;
            minVal = 0;
            maxVal = 0;
            minLoc.x = 0;
            minLoc.y = 0;
            maxLoc.x = 0;
            maxLoc.y = 0;
        }
    };

    class Detector {

    private:
        double scaleRatio = 1;

        std::unique_ptr<cv::Mat> currentImage = nullptr;
        std::unique_ptr<cv::Mat> currentImageScaled = std::make_unique<cv::Mat>();

        DetectionResult detectionResult;

        static std::unique_ptr<cv::Mat> bitmapRGBA888ToMat(JNIEnv *env, jobject bitmap);

        static std::unique_ptr<cv::Mat> matchTemplate(const cv::Mat& image, const cv::Mat& condition);

        static void locateMinMax(const cv::Mat& matchingResult, DetectionResult& results);

        static bool isValidMatching(const DetectionResult& results, const int threshold);

        static double getColorDiff(const cv::Mat& image, const cv::Mat& condition);

    public:

        Detector() = default;

        void setScreenMetrics(JNIEnv *env, jobject screenImage, double detectionQuality);

        void setScreenImage(JNIEnv *env, jobject screenImage);

        DetectionResult detectCondition(JNIEnv *env, jobject conditionImage, int threshold);

        DetectionResult detectCondition(JNIEnv *env, jobject conditionImage, int x, int y, int width, int height, int threshold);
    };
}


