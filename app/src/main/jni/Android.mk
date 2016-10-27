TOP_LOCAL_PATH:=$(call my-dir)
include $(call all-subdir-makefiles)
LOCAL_PATH := $(TOP_LOCAL_PATH)  

include $(CLEAR_VARS)
OPENCV_LIB_TYPE:=STATIC
include /home/ashik/Android/OpenCV-android-sdk/sdk/native/jni/OpenCV.mk

LOCAL_MODULE:=ndkTest
LOCAL_SRC_FILES:=ndkTest.cpp
LOCAL_LDLIBS    += -lz # Compression library



include $(BUILD_SHARED_LIBRARY)
