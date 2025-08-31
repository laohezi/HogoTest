// IFileInterface.aidl
package com.hugo.mylibrary;

// Declare any non-default types here with import statements
import android.os.ParcelFileDescriptor;

interface IFileInterface {

  void shareFd(in ParcelFileDescriptor pfd);

}