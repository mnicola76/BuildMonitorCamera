package com.example.android.camera2basic;

import android.os.AsyncTask;
import com.jcraft.jsch.*;
import java.io.*;
import android.util.Log;

/**
 * Created by mnicola on 24/11/2015.
 */
public class SFTPConnection extends AsyncTask<Void,Void,Void>
{

    private String myUser;
    private String myPassword;
    private String myHost;
    private File myPhoto;
    private String myRFile;

    public SFTPConnection(String user, String password, String host, File photoFile, String rfile) {
        myUser = user;
        myPassword = password;
        myHost = host;
        myPhoto = photoFile;
        myRFile = rfile;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // TODO Auto-generated method stub
        boolean conStatus = false;
        Session session = null;
        Channel channel = null;
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        Log.i("Session","is"+conStatus);
        try {
            JSch ssh = new JSch();
            session = ssh.getSession(myUser, myHost, 22);
            session.setPassword(myPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            //session.setHostKeyAlias("ac:46:38:fe:83:ad:c1:d2:0c:cb:fe:d7:60:9e:ef:8f");
            session.connect();
            conStatus = session.isConnected();
            Log.i("Session","is"+conStatus);
            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftp = (ChannelSftp) channel;
            FileInputStream fis = new FileInputStream(myPhoto.getAbsolutePath());
            sftp.put(fis, myRFile);
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Session","is"+conStatus);
        } catch (SftpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.i("Session","is"+conStatus);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("Session","is"+conStatus);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Session","is"+conStatus);
        }
        finally {
            if (conStatus == true) {
                channel.disconnect();
                session.disconnect();
            }
        }
        return null;
    }

}