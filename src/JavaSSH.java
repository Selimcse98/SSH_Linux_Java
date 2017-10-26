import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.InputStream;

public class JavaSSH {
    private static void sendCommands(Session session, String command){
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    System.out.println("exit-status: " + channel.getExitStatus());
                    break;
                }

                //try{Thread.sleep(1000);}catch(Exception ee){}
            }
            channel.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void sshRemoteHost(String host,String user, String password){

        String command1="ls -ltr",command2="pwd",command3="hostname";
        try{

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            JSch jsch = new JSch();
            Session session=jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            System.out.println("Connected");

            sendCommands(session,command1);
            sendCommands(session,command2);
            sendCommands(session,command3);

            session.disconnect();
            System.out.println("DONE");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        String host="54.206.55.147";
        String user="root";
        String password="Perform@nce";

        sshRemoteHost(host,user,password);
    }
}
