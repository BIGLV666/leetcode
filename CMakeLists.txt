cmake_minimum_required(VERSION 3.28)
project(leetcode CXX)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
include_directories(${CMAKE_CURRENT_SOURCE_DIR})

# 每题的 *_test.cpp 自带 main() 并 #include 对应题解,一题一个可执行目标
file(GLOB_RECURSE LEETCODE_TESTS CONFIGURE_DEPENDS ${CMAKE_CURRENT_SOURCE_DIR}/*_test.cpp)
foreach(test_src ${LEETCODE_TESTS})
    if(test_src MATCHES "/out/")
        continue()
    endif()
    get_filename_component(tgt ${test_src} NAME_WE)
    string(REGEX REPLACE "_test$" "" tgt ${tgt})
    add_executable(${tgt} ${test_src})
endforeach()

# cpp/ 公共测试设施(list_node/tree_node/codec)自带 main
add_executable(test_infra cpp/test_infra.cpp)
