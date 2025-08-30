apply plugins {

}


import java.io.File

// 签名配置常量
val keystoreFile = File("${System.getProperty("user.home")}/.android/daye.keystore")
val storePassword = "yoxisinei145"
val keyAlias = "daye"
val keyPassword = "yoxisinei145"

// 将配置添加到 project extra 属性中
project.extra.apply {
    set("keystoreFile", keystoreFile)
    set("storePassword", storePassword)
    set("keyAlias", keyAlias)
    set("keyPassword", keyPassword)
}
