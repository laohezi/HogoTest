#include <jni.h>
#include <string>
#include <android/log.h>
#include <stdio.h>



#define TAG "log"
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, TAG, fmt, ##args)
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject thiz /* this */) {
    std::string hello = "Hello from C++";
    jstring jHello = env->NewStringUTF("Hello from C++");
    jclass  jc = env->GetObjectClass(thiz);
    jmethodID  jmethodId = env->GetMethodID(jc,"kFunc", "(Ljava/lang/String;)V");
    env->CallVoidMethod(thiz,jmethodId,jHello);
    jmethodID sjm = env->GetStaticMethodID(jc,"stat","(Ljava/lang/String;)V");
    env->CallStaticVoidMethod(jc,sjm,jHello);




    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_stringFromJNI2(JNIEnv *env, jobject thiz) {
    return  env->NewStringUTF("Hello from c++2");
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_stringFromJNI3(JNIEnv *env, jobject thiz) {
   jclass  jc = env->FindClass("com/example/nativelib/NativeObject");
   jmethodID  constructId = env->GetMethodID(jc,"<init>","()V");
   jobject  obj = env->NewObject(jc,constructId);
   jfieldID  jfieldId = env->GetFieldID(jc,"name","Ljava/lang/String;");
    jstring ss = static_cast<jstring> (env->GetObjectField(obj,jfieldId));

    return  ss;
}




void test(){

}

struct Test{
    int16_t  a;
    int32_t  b;
};
int jniCheckException(JNIEnv *env ,std::string msg){
    jthrowable  ex =env-> ExceptionOccurred();
    if(ex){
        env->ExceptionDescribe();
        env->ExceptionClear();
        env->DeleteLocalRef(ex);
        jclass jc = env->FindClass("java/lang/Exception");
        env->ThrowNew(jc,msg.c_str());
        env ->DeleteLocalRef(jc);
        return 0;
    }

    return 0;

}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_nativelib_NativeLib_getSecondNumberFromJNI(JNIEnv *env, jobject thiz,
                                                            jobjectArray array) {
    jint size = env->GetArrayLength(array);

    jclass  eclz = env->FindClass("java/lang/Exception") ;
    jstring ret = env->NewStringUTF("jni origin");
    try {
      /*  for (int i = 0; i < size; ++i) {
            jstring  *js = reinterpret_cast<jstring *>(env->GetObjectArrayElement(array, i));

        }*/
      ret= static_cast<jstring>(env->GetObjectArrayElement(array,1));
    }catch (std::exception exception){
        env->ThrowNew(eclz,exception.what());
    }


    env->ExceptionCheck();
   // env->ThrowNew(env->FindClass("java/lang/Exception"),"jni exception");

    return ret;
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_nativelib_NativeLib_errorInJava(JNIEnv *env, jobject thiz) {
    jclass  eclz = env->FindClass("java/lang/Exception") ;
    jclass  jc = env->FindClass("com/example/nativelib/NativeObject");
    jmethodID  constructId = env->GetMethodID(jc,"<init>","()V");
    jobject  obj = env->NewObject(jc,constructId);
    jmethodID errorMethodId = env->GetMethodID(jc, "kError", "(Ljava/lang/String;)V");
    env->CallVoidMethod(obj, errorMethodId, env->NewStringUTF("error from java"));
    if (env->ExceptionCheck() ){
        env->ExceptionDescribe();
        env->ExceptionClear();
       // env->ThrowNew(eclz,"haha,我从java 绕道c 又回来了");
        return;
    }
    return;
}

typedef struct {
    const char* name;
    const char* signature;
    void*       fnPtr;
} JNINativeMethod1;

JNIEXPORT jint  JNICALL
JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    jint result = -1;
    if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
        return result;
    }

    jclass clazz = env->FindClass("com/example/nativelib/NativeObject");
    if (clazz == NULL) {
        return result;
    }

    JNINativeMethod1 methods[] = {
            {"kFunc", "(Ljava/lang/String;)V", (void*)Java_com_example_nativelib_NativeLib_stringFromJNI},
            {"kError", "(Ljava/lang/String;)V", (void*)Java_com_example_nativelib_NativeLib_errorInJava},
    };

    if (env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0])) < 0) {
        return result;
    }

    return JNI_VERSION_1_6;
}