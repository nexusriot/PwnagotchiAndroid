package dev.nexusriot.pwnagotchiandroid;

import com.jcraft.jsch.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SSHManager
{
    private static final Logger LOGGER =
            Logger.getLogger(SSHManager.class.getName());
    private JSch channel;
    private String password;
    private Session session;
    private int intTimeOut;
    private String userName;
    private String address;
    private int port;


    private void doCommonConstructorActions(
            String userName, String password, String connectionIP, String knownHostsFileName)
    {
        channel = new JSch();

        try
        {
            channel.setKnownHosts(knownHostsFileName);
        }
        catch(JSchException jschX)
        {
            logError(jschX.getMessage());
        }

        this.userName = userName;
        this.password = password;
        address = connectionIP;
    }

    public SSHManager(String userName, String password,
                      String connectionIP, String knownHostsFileName)
    {
        doCommonConstructorActions(userName, password,
                connectionIP, knownHostsFileName);
        port = 22;
        intTimeOut = 15000;
    }

    public SSHManager(String userName, String password, String connectionIP,
                      String knownHostsFileName, int connectionPort)
    {
        doCommonConstructorActions(userName, password, connectionIP,
                knownHostsFileName);
        port = connectionPort;
        intTimeOut = 15000;
    }

    public SSHManager(String userName, String password, String connectionIP,
                      String knownHostsFileName, int connectionPort, int timeOutMilliseconds)
    {
        doCommonConstructorActions(userName, password, connectionIP,
                knownHostsFileName);
        port = connectionPort;
        intTimeOut = timeOutMilliseconds;
    }

    public String connect()
    {
        String errorMessage = null;

        try
        {
            session = channel.getSession(userName,
                    address, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(intTimeOut);
        }
        catch(JSchException jschX)
        {
            errorMessage = jschX.getMessage();
        }
        return errorMessage;
    }

    public void setPortForwardingL(int lport, String rhost, int rport)
    // TODO: Just PoC code: need to handle not connected etc.
    {
        try {
            session.setPortForwardingL(lport, rhost, rport);
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    private String logError(String errorMessage)
    {
        if(errorMessage != null)
        {
            LOGGER.log(Level.SEVERE, "{0}:{1} - {2}",
                    new Object[]{address, port, errorMessage});
        }
        return errorMessage;
    }

    private String logWarning(String warnMessage)
    {
        if(warnMessage != null)
        {
            LOGGER.log(Level.WARNING, "{0}:{1} - {2}",
                    new Object[]{address, port, warnMessage});
        }
        return warnMessage;
    }

    public String sendCommand(String command)
    {
        StringBuilder outputBuffer = new StringBuilder();

        try
        {
            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while(readByte != 0xffffffff)
            {
                outputBuffer.append((char)readByte);
                readByte = commandOutput.read();
            }
            channel.disconnect();
        }
        catch(IOException ioX)
        {
            logWarning(ioX.getMessage());
            return null;
        }
        // TODO: looks it it owerlaps IOException
        catch(JSchException jschX)
        {
            logWarning(jschX.getMessage());
            return null;
        }
        return outputBuffer.toString();
    }

    public void close()
    {
        session.disconnect();
    }
}
