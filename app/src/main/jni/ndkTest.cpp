#include <string.h>
#include <jni.h>
#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <stdio.h>
#include <android/log.h>

using namespace std;
using namespace cv;



/*
 * replace com_example_whatever with your package name
 *
 * HelloJni should be the name of the activity that will
 * call this function
 *
 * change the returned string to be one that exercises
 * some functionality in your wrapped library to test that
 * it all works
 *
 */

extern "C" {
    JNIEXPORT jstring JNICALL
    Java_com_example_ashik_photopandabeta_MainActivity_testfun(JNIEnv *env,
                                                     jobject thiz,jstring examp)
    {
     const char* cStr = (env)->GetStringUTFChars( examp,NULL);
        return ((env)->NewStringUTF(cStr));    }

    JNIEXPORT jboolean
    JNICALL
    Java_com_example_ashik_photopandabeta_MainActivity_isDark(
            JNIEnv *env, jobject thiz,
            jint width, jint height,
                    jbyteArray yuv, jintArray bgra) {
                    jbyte *_yuv = env->GetByteArrayElements(yuv, 0);
                    jint *_bgra = env->GetIntArrayElements(bgra, 0);
                    cv:Mat matImage(height, width, CV_8UC4, (unsigned char *) _yuv);
                    uint8_t* pixelPtr = (uint8_t*)matImage.data;
                    int cn = matImage.channels();
                    double no_pixels = matImage.cols * matImage.rows;
                    double no_dark_pixels = 0;

                    for(int i = 0; i < matImage.rows; i++)
                    {
                            for(int j = 0; j < matImage.cols; j++)
                            {
                                    int B = pixelPtr[i*matImage.cols*cn + j*cn + 0]; // B
                                    int G = pixelPtr[i*matImage.cols*cn + j*cn + 1]; // G
                                    int R = pixelPtr[i*matImage.cols*cn + j*cn + 2]; // R


                                    if (R <= 60 && G <= 60 && B <= 60) {
                                        no_dark_pixels++;
                                    }

                            }

                    }
                    //    }

                    double ratio = no_dark_pixels / no_pixels;
                    //    Change the ratio to optimize the dark images.


                    env->ReleaseByteArrayElements(yuv, _yuv, 0);
                    env->ReleaseIntArrayElements(bgra, _bgra, 0);
                     __android_log_print(ANDROID_LOG_INFO, "ratio", "test int = %f", ratio);

                    if (ratio >= .7) {
                        return JNI_TRUE;
                    } else {
                        return JNI_FALSE;
                    }


    }



           JNIEXPORT jboolean
           JNICALL
           Java_com_example_ashik_photopandabeta_MainActivity_isBlur(
                   JNIEnv *env, jobject thiz,
                   jint width, jint height,
                           jbyteArray yuv, jintArray bgra) {
                    jbyte *_yuv = env->GetByteArrayElements(yuv, 0);
                    jint *_bgra = env->GetIntArrayElements(bgra, 0);
                    cv:Mat matImage(height, width, CV_8UC4, (unsigned char *) _yuv);

                   cv::Mat lap;
                   cv::Laplacian(matImage, lap, CV_8U);

                   cv::Scalar mu, sigma;
                   cv::meanStdDev(lap, mu, sigma);


                   double focusMeasure = sigma.val[0]*sigma.val[0];
                   //NSLog(@"%f", focusMeasure);

                   // Change the value 55 here to optimise the blurry detection

                     __android_log_print(ANDROID_LOG_INFO, "focusmeasure", "test int = %f", focusMeasure);

                   env->ReleaseByteArrayElements(yuv, _yuv, 0);
                   env->ReleaseIntArrayElements(bgra, _bgra, 0);

                   if (focusMeasure < 55) {
                       return JNI_TRUE;
                   } else {
                       return JNI_FALSE;
                   }

           }


    }


