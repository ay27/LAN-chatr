
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


/**
 * Created with IntelliJ IDEA.
 * User: ay27
 * Date: 13-5-30
 * Time: 下午11:21
 */

public class Main extends JFrame {


    private InetAddress brocastIP;
    private final int messagePort = 13145, statusPort = 13146, filePort = 13147;
    private DatagramSocket messageServer, statusServer, fileServer;
    private Map<InetAddress, String> user;
    private Vector<String> message;
    private String myName, myIP;



    public Main() {
        initData();
        initComponents();
    }

    private void initData()
    {
        // 设置server和端口
        try {
            messageServer = new DatagramSocket(messagePort);
            statusServer = new DatagramSocket(statusPort);
            fileServer = new DatagramSocket(filePort);

        } catch (SocketException e) {
            e.printStackTrace();
        }

        // 设置需要存储的消息记录和用户
        user = new Hashtable<InetAddress, String>();
        message = new Vector<String>();

        // 设置自己的名字
        String name = JOptionPane.showInputDialog("输入你的名字");
        myName = ((name == null) || (name.equals("")))? UUID.randomUUID().toString(): name;

        // 设置IP地址
        try {
            myIP = InetAddress.getLocalHost().getHostAddress();
            brocastIP = InetAddress.getByName("255.255.255.255");
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        messageLabel = new JLabel();
        jScrollPane1 = new JScrollPane();
        messageBox = new JEditorPane();
        sendFileButton = new JButton();
        jScrollPane2 = new JScrollPane();
        sendBox = new JEditorPane();
        sendMessageButton = new JButton();
        sendLabel = new JLabel();
        nameLabel = new JLabel();
        jScrollPane3 = new JScrollPane();
        teamArea = new JTextArea();
        teamLabel = new JLabel();


        // 设置消息显示栏
        messageLabel.setText("messageBox");
        jScrollPane1.setViewportView(messageBox);
        messageBox.setEditable(false);


        // 设置发送文件的按扭
        sendFileButton.setText("send file");
        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendFileActionPerformed();
            }
        });

        // 设置写入消息的界面
        jScrollPane2.setViewportView(sendBox);
        sendBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode()==KeyEvent.VK_ENTER)
                {
                    sendMessageButtonActionPerformed();
                }
            }
        });

        // 设置发送消息的按扭
        sendMessageButton.setText("send");
        sendMessageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                sendMessageButtonActionPerformed();
            }
        });

        sendLabel.setText("press Ctrl+Enter to send your message                                                                       ");

        nameLabel.setText(" 我的名字："+myName+"                                     ");

        jScrollPane3.setViewportView(teamArea);
        teamArea.setEditable(false);

        teamLabel.setText("  team                                            ");


        sendOnline();
        new AcceptStatus().start();
        new AcceptMessage().start();
        new AcceptFile().start();

        setlayout();

    }

    //以下是用Netbeans生成的界面代码，写成一个独立的子程序是为了更好的代码折叠
    //下面的代码是针对界面的排版，完全可以忽视
    private void setlayout()
    {

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(sendLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(sendMessageButton))
                                        .addComponent(jScrollPane2)
                                        .addComponent(jScrollPane1)
                                        .addComponent(messageLabel, GroupLayout.Alignment.LEADING)
                                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(sendFileButton)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane3)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(nameLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(teamLabel))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(messageLabel)
                                        .addComponent(nameLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 226, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(sendFileButton))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(sendMessageButton)
                                                        .addComponent(sendLabel)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(teamLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }


    // 已改
    private void sendFileActionPerformed()
    {
        JFileChooser jFileChooser = new JFileChooser("选择需要传从的文件");
        jFileChooser.showOpenDialog(null);

        byte[] buf = new byte[10240];

        File file = jFileChooser.getSelectedFile();
        if (file == null) return;

        DatagramPacket dp;
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;

            // 需要先发送一些消息
            UUID uuid= UUID.randomUUID();
            String uu = myIP+'\0'+uuid.toString()+'\0';
            dp = new DatagramPacket(uu.getBytes(), uu.getBytes().length, brocastIP, filePort);
            fileServer.send(dp);
            Thread.sleep(10000);

            while ((c = fis.read(buf)) != -1)
            {
                dp = new DatagramPacket(buf, c, brocastIP, filePort);
                fileServer.send(dp);
                Thread.sleep(1000);
            }
            byte[] bb = (uuid.toString()+'\0').getBytes();
            System.out.println(uuid);
            dp = new DatagramPacket(bb, bb.length, brocastIP, filePort);
            fileServer.send(dp);
            fis.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // 广播聊天消息，已改
    private void sendMessageButtonActionPerformed()
    {
        String temp = sendBox.getText();
        String str;
        if (temp.equals("")) return;
        str = ""+myName+'\0'+myIP+'\0'+(new Date().toString())+'\0'+temp+'\0';
        try {
            byte[] bytes = str.getBytes("GBK");
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length, brocastIP, messagePort);
            messageServer.send(dp);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        sendBox.setText("");
        message.add(str);
        setTextMessage();
    }

    //修改message显示消息内容， 已改
    private void setTextMessage()
    {
        String str = "";
        for (String aMessage : message) {
            String[] s = aMessage.split("\0");
            str += "\t" + s[0] + "    " + s[2] + '\n' + s[3] + '\n';
        }
        messageBox.setText(str);
    }


    // 不必改
    private void teamReflesh()
    {
        String str = "";
        Set<InetAddress> key = user.keySet();
        for (Iterator it = key.iterator(); it.hasNext(); )
        {
            InetAddress s = (InetAddress) it.next();
            str += user.get(s) + '\n';
        }
        teamArea.setText(str);
    }


    // 已改
    class AcceptMessage extends Thread
    {
        public void run()
        {
            byte[] buf = new byte[10240];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);

            while (true)
                try {
                    messageServer.receive(dp);
                    String str = new String(dp.getData(), "GBK");
                    String[] ss = str.split("\0");

                    if (ss[1].equals(myIP)) continue;

                    message.add(str);
                    setTextMessage();

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
    }

    // 新增
    private void sendOnline()
    {
        String str = ""+myName+'\0'+myIP+'\0';
        try {
            byte[] bytes = str.getBytes("GBK");
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length, brocastIP, statusPort);
            statusServer.send(dp);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //新增
    class AcceptStatus extends Thread
    {
        public void run()
        {
            byte[] buf = new byte[10240];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            while (true)
                try {
                    statusServer.receive(dp);
                    String str = new String(dp.getData(), "GBK");
                    String[] ss = str.split("\0");

                    if (ss[1].equals(myIP)) continue;

                    sendOnline();

                    user.put(InetAddress.getByName(ss[1]), ss[0]);
                    teamReflesh();

                } catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
    }

    //新增
    class AcceptFile extends Thread
    {
        public void run()
        {
            byte[] buf = new byte[10240];
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            while (true)
                try {
                    fileServer.receive(dp);
                    String ip = new String(dp.getData()).split("\0")[0];
                    String uuid = new String(dp.getData()).split("\0")[1];

                    JFileChooser jFileChooser;
                    File file;
                    FileOutputStream fos = null;

                    boolean flag = false;

                    if (!ip.equals(myIP))
                    {
                        jFileChooser = new JFileChooser("选择保存的位置");
                        jFileChooser.showSaveDialog(null);
                        file = jFileChooser.getSelectedFile();
                        fos = new FileOutputStream(file);
                        if (file == null) flag = true;
                    }


                    while (true)
                    {
                        fileServer.receive(dp);
                        if ((new String(dp.getData()).split("\0")[0].equals(uuid))) break;
                        if (!ip.equals(myIP) || flag)
                        {
                            fos.write((new String(dp.getData()).trim().getBytes()));
                            fos.flush();
                        }
                    }
                    if (!ip.equals(myIP))
                    {
                        fos.close();
                        JOptionPane.showMessageDialog(null, "接收完毕");
                    }


                } catch (Exception e)
                {
                    e.printStackTrace();
                }
        }
    }


    //界面元素
    private JLabel sendLabel;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JEditorPane messageBox;
    private JLabel messageLabel;
    private JLabel nameLabel;
    private JEditorPane sendBox;
    private JButton sendFileButton;
    private JButton sendMessageButton;
    private JLabel teamLabel;
    private JTextArea teamArea;






    public static void main(String args[]) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

}