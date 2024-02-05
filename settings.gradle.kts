include (":app")
include (":composeframe2")
rootProject.name = "HogoTest"

/*
setBinding(new Binding([gradle: this]))
evaluate(new File(
        settingsDir.parentFile,
        'flutter_module/.android/include_flutter.groovy'
))*/

dependencyResolutionManagement {

}
include("aldlserver")
//include(":nativelib")
//include(":filemanager")
include(":mycommon")
include(":plugins")
//include(":annotation")
