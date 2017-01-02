package com.xxx.netty.run;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;

import javax.net.ssl.SSLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.xxx.netty.client.SecureChatClientInitializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

@Component
public class ChatClient {

	@Value("${server.host}")
	String HOST;

	@Value("${server.port}")
	int PORT;

	static private String loginName = "";

	public static String getName() {
		return loginName;
	}

	public static javax.swing.JTextArea txtShow = new javax.swing.JTextArea();
	JScrollPane jScrollPanel=new JScrollPane(txtShow);
	JTextField chatField = new JTextField(35);
	JLabel label = new JLabel("input msg");
	JButton btnSend = new JButton("send");

	static Channel channel = null;
	static EventLoopGroup clientGroup = null;

	/*
	 * 生成窗体
	 */

	void showFrame() {
		try {
			String lookAndFeel="com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
			 UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		} 	
		
		JFrame cFrame = new JFrame("CallMe");
		cFrame.setSize(600, 500);
		cFrame.setVisible(true);
		cFrame.setLocationRelativeTo(null);
		cFrame.setResizable(false);
		
		cFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				clientGroup.shutdownGracefully();
				System.exit(0);
			}

		});

		cFrame.add(jScrollPanel, "Center");
		JPanel p = new JPanel();
		//p.setLayout(new BorderLayout());
		p.add(label);
		p.add(chatField);
		p.add(btnSend);
		cFrame.add(p, "South");
		chatField.setText("1");
		chatField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {

					if (!chatField.getText().trim().equals("")) {
						String line = "[" + loginName + "]" + chatField.getText();

						ChannelFuture lastWriteFuture = null;
						lastWriteFuture = channel.writeAndFlush(line + "\r\n");

						chatField.setText("");

						if (line.equalsIgnoreCase("bey")) {
							channel.closeFuture().sync();
						}

						if (lastWriteFuture != null) {
							lastWriteFuture.sync();
						}
					}
				} catch (InterruptedException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} finally {
					// clientGroup.shutdownGracefully();
					
				}

			}
		});

		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				sendMsg();
			}
		});

	}

	public void sendMsg(){
		try {
//			for (int i = 0; i < 2100000000; i++) {
//				
//			}
			if (!chatField.getText().trim().equals("")) {
				String line = "[" + loginName + "]" + chatField.getText();
				ChannelFuture lastWriteFuture = null;
				lastWriteFuture = channel.writeAndFlush(line + "\r\n");

				//chatField.setText(System.currentTimeMillis() + "");

				if (line.equalsIgnoreCase("bey")) {
					channel.closeFuture().sync();
				}
				if (lastWriteFuture != null) {
					lastWriteFuture.sync();
				}
			}
		} catch (InterruptedException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} finally {
			// clientGroup.shutdownGracefully();
		}
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws SSLException, InterruptedException {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:root-context.xml");
		ChatClient client = context.getBean(ChatClient.class);
		try {
			// 负责接受一个用户默认的用户名
			String a =JOptionPane.showInputDialog("Input your nickname please！【用户名】");
			if (a != null) {
				if (!a.trim().equals(""))
					loginName = a;
				else {
					loginName = InetAddress.getLocalHost().toString();
				}
			}

			final SslContext sslctx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
			clientGroup = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(clientGroup)
					.channel(NioSocketChannel.class)
					.handler(new SecureChatClientInitializer(sslctx));
			channel = bootstrap.connect(client.HOST, client.PORT).sync().channel();
			client.showFrame();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
