#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include "opencv2/core.hpp"
#include "opencv2/imgproc.hpp"

#include "bitmap_mat_helper.h"

#define TAG_GRAPHIC "graphic"

extern "C"
JNIEXPORT jobject JNICALL
Java_com_daydreaminger_android_app_libs_graphic_GraphicLib_testOpenCv(JNIEnv *env, jobject thiz,
                                                                      jobject src_bitmap) {
    // 将 Bitmap 转换为 OpenCV Mat
    cv::Mat mat_in = bitmap_to_mat(env, src_bitmap);

    // 转换为灰度图
    cv::Mat mat_gray;
    cv::cvtColor(mat_in, mat_gray, cv::COLOR_RGBA2GRAY);

    // 将结果写回 Bitmap
    jobject dstBitmap = mat_to_bitmap(env, mat_gray);
    if (dstBitmap == nullptr) {
        LOGD(TAG_GRAPHIC, "result bitmap is null.")
    }
    return dstBitmap;
}