/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>


/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
jstring
Java_in_manishpathak_videodemo_VideoCapture_stringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
    return (*env)->NewStringUTF(env, "Done NDK....Hello from JNI !");
}

int* rgbData;
int rgbDataSize = 0;

JNIEXPORT
void
JNICALL Java_in_manishpathak_videodemo_OverlayView_YUVtoRBG(JNIEnv * env, jobject obj, jintArray rgb, jbyteArray yuv420sp, jint width, jint height)
{
    int             sz;
    int             i;
    int             j;
    int             Y;
    int             Cr = 0;
    int             Cb = 0;
    int             pixPtr = 0;
    int             jDiv2 = 0;
    int             R = 0;
    int             G = 0;
    int             B = 0;
    int             cOff;
	int w = width;
	int h = height;
    sz = w * h;

    jboolean isCopy;
    jbyte* yuv = (*env)->GetByteArrayElements(env, yuv420sp, &isCopy);
	if(rgbDataSize < sz) {
		int tmp[sz];
		rgbData = &tmp[0];
		rgbDataSize = sz;

	}

	for(j = 0; j < h; j++) {
             pixPtr = j * w;
             jDiv2 = j >> 1;
             for(i = 0; i < w; i++) {
                     Y = yuv[pixPtr];
					 if(Y < 0) Y += 255;
                     if((i & 0x1) != 1) {
                             cOff = sz + jDiv2 * w + (i >> 1) * 2;
                             Cb = yuv[cOff];
                             if(Cb < 0) Cb += 127; else Cb -= 128;
                             Cr = yuv[cOff + 1];
                             if(Cr < 0) Cr += 127; else Cr -= 128;
                     }
                     R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);//1.406*~1.403
                     if(R < 0) R = 0; else if(R > 255) R = 255;
                     G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1) + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);//
                     if(G < 0) G = 0; else if(G > 255) G = 255;
                     B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);//1.765~1.770
                     if(B < 0) B = 0; else if(B > 255) B = 255;
                     rgbData[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
             }
    }
	(*env)->SetIntArrayRegion(env, rgb, 0, sz, ( jint * ) &rgbData[0] );

	(*env)->ReleaseByteArrayElements(env, yuv420sp, yuv, JNI_ABORT);
}
