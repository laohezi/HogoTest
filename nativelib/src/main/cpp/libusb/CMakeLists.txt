cmake_minimum_required(VERSION 3.10) # 根据你的系统和CMake版本调整
project(libusb)

# 设置源代码目录
set(LIBUSB_SOURCE_DIR "${CMAKE_CURRENT_SOURCE_DIR}")

# 添加源文件
set(LIBUSB_SOURCES
        core.c
        descriptor.c
        hotplug.c
        io.c
        strerror.c
        sync.c
        os/linux_usbfs.c
        os/events_posix.c
        os/threads_posix.c
        os/linux_netlink.c
)

# 添加头文件目录
include_directories("${LIBUSB_SOURCE_DIR}" "${LIBUSB_SOURCE_DIR}/os")

# 设置编译选项
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fvisibility=hidden -pthread")

# 创建共享库
add_library(libusb SHARED ${LIBUSB_SOURCES})

# 链接必要的系统库
target_link_libraries(libusb PRIVATE log)

# 设置库的版本和SO版本
set_target_properties(libusb PROPERTIES
        VERSION 1.0.0
        SOVERSION 1
)

# 安装规则
install(TARGETS libusb
        LIBRARY DESTINATION lib
        ARCHIVE DESTINATION lib
        RUNTIME DESTINATION bin)

# 安装头文件
install(FILES
        libusb.h
        version.h
        version_nano.h
        DESTINATION include/libusb)

# 如果需要，还可以添加其他文件或目录的安装规则