package com.utility.ftputility;

import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class FTPUtilityImpl implements FTPUtility {

    private static String SERVER_URL = "127.0.0.1";
    private static String USERNAME = "yogesh";
    private static String PASSWORD = "yogesh";
    private static Integer PORTNO = 21;

    private static FTPClient ftpClient = null;

    public FTPUtilityImpl() {
        ftpClient = new FTPClient();
    }

    private FTPClient getFtpClient(String url, Integer portno, String username, String password) throws IOException {
        if (portno == null) {
            portno = 21;
        }
        ftpClient.connect(url, portno);
        ftpClient.login(username, password);
        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        return ftpClient;
    }

    private boolean closeFtpClient(FTPClient ftpClient) {
        boolean isSuccess = false;
        try {
            if (ftpClient.isConnected()) {
                isSuccess = ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    private void printServerReply(final FTPClient ftpClient){
        System.out.println("SERVER REPLY :: " + ftpClient.getReplyCode() + " " + ftpClient.getReplyString());
    }

    public boolean storeFile(String remoteDirPath, String fileName, InputStream inputStream) {
        boolean isSuccess = false;
        FTPClient ftpClient = null;
        try {
            makeDir(remoteDirPath);
            ftpClient = getFtpClient(SERVER_URL, PORTNO, USERNAME, PASSWORD);
            System.out.println("Start uploading file...");
            isSuccess = ftpClient.storeFile(remoteDirPath+fileName, inputStream);
            inputStream.close();
            if (isSuccess) {
                System.out.println("The file is uploaded successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            printServerReply(ftpClient);
            closeFtpClient(ftpClient);
        }
        return isSuccess;
    }

    public boolean makeDir(String remoteDirPath) {
        boolean isSuccess = false;
        FTPClient ftpClient = null;
        try {
            ftpClient = getFtpClient(SERVER_URL, PORTNO, USERNAME, PASSWORD);
            System.out.println("Start making directory...");
            if (!ftpClient.changeWorkingDirectory(remoteDirPath)) {
                ftpClient.makeDirectory(remoteDirPath);
            }
            isSuccess = true;
            if (isSuccess) {
                System.out.println("Directory created successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            printServerReply(ftpClient);
            closeFtpClient(ftpClient);
        }
        return isSuccess;
    }

    public boolean downloadFile(String remoteFilePath, String localFilePath) {
        boolean isSuccess = false;
        FTPClient ftpClient = null;
        try {
            ftpClient = getFtpClient(SERVER_URL, PORTNO, USERNAME, PASSWORD);
            System.out.println("Start downloading file...");
            File localFile = new File(localFilePath);
            if (localFile.exists()){
                localFile.delete();
            }
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(localFile);
            byte buffer[] = new byte[1024];
            while (inputStream.read(buffer) != -1) {
                fileOutputStream.write(buffer);
            }
            isSuccess = true;
            inputStream.close();
            fileOutputStream.close();
            if (isSuccess) {
                System.out.println("The file downloaded successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            printServerReply(ftpClient);
            closeFtpClient(ftpClient);
        }
        return isSuccess;
    }
}
