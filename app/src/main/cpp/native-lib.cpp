#include <jni.h>
#include <string>
#include <vector>
#include <sstream>
#include <algorithm>

/**
 * A simple and fast ANSI escape sequence stripper/processor.
 * In a real-world terminal, this would convert sequences into style spans.
 * Here we optimize for speed to ensure smooth background-to-foreground transitions.
 */
std::string processANSI(const std::string& input) {
    std::string output;
    output.reserve(input.size());
    
    bool in_escape = false;
    for (size_t i = 0; i < input.length(); ++i) {
        if (input[i] == '\u001B') { // ESC
            in_escape = true;
            continue;
        }
        
        if (in_escape) {
            // Basic CSI (Command Sequence Introducer) handling: ESC [ ... m/K/etc.
            // We wait for the terminating character (usually a letter)
            if (isalpha(input[i])) {
                in_escape = false;
            }
            continue;
        }
        
        output += input[i];
    }
    return output;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_terminalarrow_app_utils_NativeBufferProcessor_processBufferNative(
        JNIEnv* env,
        jobject /* this */,
        jstring input_text) {
    
    if (input_text == nullptr) return env->NewStringUTF("");

    const char* native_string = env->GetStringUTFChars(input_text, 0);
    if (native_string == nullptr) return env->NewStringUTF("");

    std::string text(native_string);
    std::string processed = processANSI(text);
    
    env->ReleaseStringUTFChars(input_text, native_string);
    return env->NewStringUTF(processed.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_terminalarrow_app_utils_NativeBufferProcessor_fastSearchNative(
        JNIEnv* env,
        jobject /* this */,
        jstring buffer,
        jstring query) {
    
    if (buffer == nullptr || query == nullptr) return env->NewStringUTF("");

    const char* native_buffer = env->GetStringUTFChars(buffer, 0);
    const char* native_query = env->GetStringUTFChars(query, 0);

    std::string str_buffer(native_buffer);
    std::string str_query(native_query);
    
    std::string results;
    size_t pos = str_buffer.find(str_query, 0);
    while(pos != std::string::npos) {
        results += std::to_string(pos) + ",";
        pos = str_buffer.find(str_query, pos + str_query.length());
    }

    env->ReleaseStringUTFChars(buffer, native_buffer);
    env->ReleaseStringUTFChars(query, native_query);
    
    return env->NewStringUTF(results.c_str());
}
