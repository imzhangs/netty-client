#Netty-5.0  SSL  TCP  Simple Client

	可嵌入Android 使用。

##1.1 添加maven依赖
```xml
		<dependency>
			<groupId>io.netty</groupId>
			<artifactId>netty-all</artifactId>
			<version>5.0.0.Alpha2</version>
		</dependency>
```
##1.2 启动类
ChatClient 使用Spring 注入 host,port , 方便后期 业务扩展针对 S2S通信时，独立成一个 Client 工具使用。
``` java
// 使用netty 自带的 SSLContext 容器初始一个SSL实例
final SslContext sslctx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
```
##1.3 测试
1. 启动时用了JSwing 的GUI组件 需要填写昵称；
2. 聊天协议 一对多 @all: textMesage ;  一对一 @to:nick: textMessage。


