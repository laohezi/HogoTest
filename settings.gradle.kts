include (":app")
include (":composeframe2")
rootProject.name = "HogoTest"

/*
setBinding(new Binding([gradle: this]))
evaluate(new File(
        settingsDir.parentFile,
        'flutter_module/.android/include_flutter.groovy'
))*/
include("aldlserver")
include(":nativelib")
include(":filemanager")
