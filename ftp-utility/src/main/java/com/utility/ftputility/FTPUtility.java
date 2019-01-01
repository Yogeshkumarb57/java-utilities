package com.utility.ftputility;

import java.io.InputStream;

public interface FTPUtility {

    boolean storeFile(String remoteDirPath, String fileName, InputStream inputStream);

    boolean makeDir(String remoteDirPath);

    boolean downloadFile(String remoteFilePath, String filename);
}
