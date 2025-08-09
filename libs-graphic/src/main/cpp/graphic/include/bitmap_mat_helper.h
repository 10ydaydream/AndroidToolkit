//
// Created by daydreaminger on 2025/7/27.
//

#ifndef ANDROIDTOOLKIT_BITMAP_MAT_HELPER_H
#define ANDROIDTOOLKIT_BITMAP_MAT_HELPER_H

#include <jni.h>
#include <android/bitmap.h>
#include "opencv2/core.hpp"
#include "opencv2/imgproc.hpp"
#include <android/log.h>
#include <algorithm>
#include "log_utils.h"

// log define

#define LOG_TAG_BMH "OpenCV_JNI"

// ===== create bitmap
jobject create_bitmap_default(JNIEnv *env, int width, int height);

jobject create_bitmap(JNIEnv *env, int width, int height, char *config_name);

// 创建 Java Bitmap 对象
jobject create_bitmap_default(JNIEnv *env, int width, int height) {
    return create_bitmap(env, width, height, "ARGB_8888");
}

jobject create_bitmap(JNIEnv *env, int width, int height, char *config_name) {
    // 获取 Bitmap 类
    jclass bitmapClass = env->FindClass("android/graphics/Bitmap");
    if (bitmapClass == nullptr) {
        LOGE(LOG_TAG_BMH, "Failed to find Bitmap class");
        return nullptr;
    }

    // 获取 createBitmap 方法 ID
    jmethodID createBitmapMethod = env->GetStaticMethodID(
            bitmapClass,
            "createBitmap",
            "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;"
    );

    if (createBitmapMethod == nullptr) {
        LOGE(LOG_TAG_BMH, "Failed to find createBitmap method");
        return nullptr;
    }

    // 获取 Bitmap.Config 类
    jclass configClass = env->FindClass("android/graphics/Bitmap$Config");
    if (configClass == nullptr) {
        LOGE(LOG_TAG_BMH, "Failed to find Bitmap.Config class");
        return nullptr;
    }

    // 获取 ARGB_8888 字段
    jfieldID configField = env->GetStaticFieldID(
            configClass,
            config_name,
            "Landroid/graphics/Bitmap$Config;"
    );

    if (configField == nullptr) {
        LOGE(LOG_TAG_BMH, "Failed to find %s field", config_name);
        return nullptr;
    }

    // 获取 ARGB_8888 配置对象
    jobject argbConfig = env->GetStaticObjectField(configClass, configField);
    if (argbConfig == nullptr) {
        LOGE(LOG_TAG_BMH, "Failed to get %s config", config_name);
        return nullptr;
    }

    // 创建 Bitmap 对象
    jobject bitmap = env->CallStaticObjectMethod(
            bitmapClass,
            createBitmapMethod,
            width,
            height,
            argbConfig
    );

    return bitmap;
}

/**
 * 将 Android Bitmap 转换为 OpenCV Mat
 * */
cv::Mat bitmap_to_mat(JNIEnv *env, jobject bitmap) {
    AndroidBitmapInfo info;
    void *pixels = nullptr;
    // 获取 Bitmap 信息
    AndroidBitmap_getInfo(env, bitmap, &info);
    // 锁定像素
    AndroidBitmap_lockPixels(env, bitmap, &pixels);
    // 创建 Mat 对象
    cv::Mat mat(info.height, info.width, CV_8UC4, pixels);
    // 解锁像素
    AndroidBitmap_unlockPixels(env, bitmap);
    return mat.clone(); // 返回深拷贝
}

/**
 * 将 OpenCV Mat 转换为 Android Bitmap
 * */
jobject mat_to_bitmap(JNIEnv *env, cv::Mat &srcMat) {
    if (srcMat.empty()) {
        return nullptr;
    }

    // 获取Bitmap类和方法
    jclass bitmapClass = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapMethod = env->GetStaticMethodID(bitmapClass,
                                                          "createBitmap",
                                                          "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");

    // 获取Bitmap.Config类
    jclass configClass = env->FindClass("android/graphics/Bitmap$Config");
    jfieldID argb8888Field = env->GetStaticFieldID(configClass, "ARGB_8888",
                                                   "Landroid/graphics/Bitmap$Config;");
    jobject argb8888Config = env->GetStaticObjectField(configClass, argb8888Field);

    // 创建Bitmap对象
    jobject bitmap = env->CallStaticObjectMethod(bitmapClass, createBitmapMethod,
                                                 srcMat.cols, srcMat.rows, argb8888Config);

    // 锁定Bitmap像素
    void *pixels;
    if (AndroidBitmap_lockPixels(env, bitmap, &pixels) < 0) {
        return nullptr;
    }

    // 根据源Mat格式进行转换
    cv::Mat tmp;
    switch (srcMat.channels()) {
        case 1: // 灰度图 → RGBA
            cv::cvtColor(srcMat, tmp, cv::COLOR_GRAY2RGBA);
            break;
        case 3: // BGR → RGBA
            cv::cvtColor(srcMat, tmp, cv::COLOR_BGR2RGBA);
            break;
        case 4: // BGRA → RGBA
            cv::cvtColor(srcMat, tmp, cv::COLOR_BGRA2RGBA);
            break;
        default:
            AndroidBitmap_unlockPixels(env, bitmap);
            return nullptr;
    }

    // 获取Bitmap信息
    AndroidBitmapInfo info;
    if (AndroidBitmap_getInfo(env, bitmap, &info) < 0) {
        AndroidBitmap_unlockPixels(env, bitmap);
        return nullptr;
    }

    // 检查尺寸
    if (tmp.cols != info.width || tmp.rows != info.height) {
        AndroidBitmap_unlockPixels(env, bitmap);
        return nullptr;
    }

    // 逐行拷贝数据（处理可能的stride对齐）
    int bytesPerLine = info.stride;
    uchar *dst = (uchar *) pixels;
    uchar *src = tmp.data;
    const int rowBytes = tmp.cols * 4; // RGBA每像素4字节

    if (bytesPerLine == rowBytes) {
        // 直接整块拷贝
        memcpy(dst, src, bytesPerLine * tmp.rows);
    } else {
        // 逐行拷贝
        for (int y = 0; y < tmp.rows; y++) {
            memcpy(dst, src, rowBytes);
            dst += bytesPerLine;
            src += rowBytes;
        }
    }

    // 解锁并返回
    AndroidBitmap_unlockPixels(env, bitmap);
    return bitmap;
}


#endif //ANDROIDTOOLKIT_BITMAP_MAT_HELPER_H
