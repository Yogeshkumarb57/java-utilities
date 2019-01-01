package com.utility.ftputility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TestFtpUtility {

    public static void main(String[] args) throws IOException{
        FTPUtility ftpUtility = new FTPUtilityImpl();
        File file = new File("D:\\image.jpg");
        InputStream inputStream =new FileInputStream(file);
        ftpUtility.storeFile("/files/",file.getName(),inputStream);
        ftpUtility.downloadFile("/files/"+file.getName(),new File("F:\\image.png").getAbsolutePath());
    }
}
