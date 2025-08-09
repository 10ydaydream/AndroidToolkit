//
// Created by daydreaminger on 2025/7/27.
//

#ifndef ANDROIDTOOLKIT_LOG_UTILS_H
#define ANDROIDTOOLKIT_LOG_UTILS_H

#include <android/log.h>

#define DEBUG // 可以通过 CmakeLists.txt 等方式来定义在这个宏，实现动态打开和关闭LOG

#ifdef DEBUG
#define LOGV(tag, ...) __android_log_print(ANDROID_LOG_VERBOSE, tag, __VA_ARGS__);
#define LOGD(tag, ...) __android_log_print(ANDROID_LOG_DEBUG, tag, __VA_ARGS__);
#define LOGI(tag, ...) __android_log_print(ANDROID_LOG_INFO, tag, __VA_ARGS__);
#define LOGW(tag, ...) __android_log_print(ANDROID_LOG_WARN, tag, __VA_ARGS__);
#define LOGE(tag, ...) __android_log_print(ANDROID_LOG_ERROR, tag, __VA_ARGS__);
#else
#define LOGV(format, ...);
#define LOGD(format, ...);
#define LOGI(format, ...);
#define LOGW(format, ...);
#define LOGE(format, ...);
#endif // DEBUG

#endif //ANDROIDTOOLKIT_LOG_UTILS_H
