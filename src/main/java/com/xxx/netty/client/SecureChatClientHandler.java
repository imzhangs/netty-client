/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.xxx.netty.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.xxx.netty.run.ChatClient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handles a client-side channel.
 */
public class SecureChatClientHandler extends SimpleChannelInboundHandler<String> {

	public void channelRead(ChannelHandlerContext ctx, String msg) throws Exception {

		// String msg2=processMsg(msg);
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		System.out.println("error:"+cause.getMessage());
		ctx.close();
	}

	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		// TODO Auto-generated method stub
		ChatClient.txtShow.append("\r\n\r\n["+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"]\r\n" + msg);
		if(msg.contains("@Welcome")){
			ctx.channel().writeAndFlush("@login:"+ChatClient.getName()+"\n");
		}
//		System.out.println("client received =>> "+arg1);
	}
}
