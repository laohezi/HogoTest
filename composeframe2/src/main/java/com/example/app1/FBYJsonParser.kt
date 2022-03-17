package com.example.app1

import android.content.Context
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

object FBYJsonParser {
    fun getJSONFormFile(filepath: String?): JSONObject? {
        var jObj: JSONObject? = null
        var json: String? = null
        var inputstreamreader: InputStreamReader? = null
        try {
            val file = File(filepath)
            if (file.exists() && file.isFile) {
                val finputstream = FileInputStream(file)
                inputstreamreader = InputStreamReader(finputstream)
                val reader = BufferedReader(inputstreamreader)
                val sb = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line).append("\n")
                }
                json = sb.toString()
                jObj = JSONObject(json)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            if (inputstreamreader != null) {
                try {
                    inputstreamreader.close()
                } catch (e: IOException) {
                }
            }
        }

        // return JSON String
        return jObj
    }

    fun getJSONFormAssets(fileName: String): JSONObject? {
        var jObj: JSONObject? = null
        var json: String? = null
        var inputstreamreader: InputStreamReader? = null
        try {
            var finputstream: InputStream = appContext.assets.open(fileName)
            inputstreamreader = InputStreamReader(finputstream)
            val reader = BufferedReader(inputstreamreader)
            val sb = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                sb.append(line).append("\n")
            }
            json = sb.toString()
            jObj = JSONObject(json)
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            if (inputstreamreader != null) {
                try {
                    inputstreamreader.close()
                } catch (e: IOException) {
                }
            }
        }

        // return JSON String
        return jObj
    }

    fun getJSONFormInternalStorage(context: Context, fileName: String?): JSONObject? {
        var jObj: JSONObject? = null
        var json: String? = null
        var inputstreamreader: InputStreamReader? = null
        try {
            val finputstream = context.openFileInput(fileName)
            if (finputstream != null) {
                inputstreamreader = InputStreamReader(finputstream)
                val reader = BufferedReader(inputstreamreader)
                val sb = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line).append("\n")
                }
                json = sb.toString()
                jObj = JSONObject(json)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            if (inputstreamreader != null) {
                try {
                    inputstreamreader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        // return JSON String
        return jObj
    }

    fun saveJSONToInternalStorage(context: Context, fileName: String?, jObj: JSONObject?): Boolean {
        if (jObj == null) {
            return false
        }
        var fos: FileOutputStream? = null
        try {
            context.deleteFile(fileName)
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            if (fos != null) {
                val content = jObj.toString()
                fos.write(content.toByteArray(charset("UTF-8")))
                fos.close()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return true
    }

    fun saveBackupJSONToInternalStorage(
        dir: String?,
        fileName: String?,
        jObj: JSONObject?
    ): Boolean {
        if (jObj == null) {
            return false
        }
        var result = false
        var fos: FileOutputStream? = null
        try {
            val file = File(dir, fileName)
            if (file.exists()) {
                file.delete()
            }
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            if (file.createNewFile()) {
                fos = FileOutputStream(file)
                val content = jObj.toString()
                fos.write(content.toByteArray(charset("UTF-8")))
                fos.flush()
                result = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return result
    }

    fun getBackupJSONFromInternalStorage(filePath: String?): JSONObject? {
        var file: File? = null
        try {
            file = File(filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var jObj: JSONObject? = null
        if (file != null && file.exists()) {
            var json: String? = null
            var inputstreamreader: InputStreamReader? = null
            try {
                val finputstream = FileInputStream(file)
                inputstreamreader = InputStreamReader(finputstream)
                val reader = BufferedReader(inputstreamreader)
                val sb = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line).append("\n")
                }
                json = sb.toString()
                jObj = JSONObject(json)
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                if (inputstreamreader != null) {
                    try {
                        inputstreamreader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return jObj
    }
}