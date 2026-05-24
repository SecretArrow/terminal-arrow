cmake_minimum_required(VERSION 3.22.1)
project("terminalarrow")

add_library( # Sets the name of the library.
        native-lib
        SHARED
        native-lib.cpp)

find_library( # Sets the name of the path variable.
        log-lib
        log)

target_link_libraries( # Specifies the libraries CMake should link to your target library.
        native-lib
        ${log-lib})
