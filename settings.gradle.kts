pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

include (":app")
include (":composeframe2")
include (":aidlmodule")
include (":musicplayer")
rootProject.name = "HogoTest"

/*
setBinding(new Binding([gradle: this]))
evaluate(new File(
        settingsDir.parentFile,
        'flutter_module/.android/include_flutter.groovy'
))*/

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
include("aldlserver")
include(":nativelib")
//include(":filemanager")
include(":mycommon")
include(":plugins")
//include(":annotation")
