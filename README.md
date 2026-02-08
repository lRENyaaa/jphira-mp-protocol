# jphira-mp-protocol
Java版本的 [phira-mp](https://github.com/TeamFlos/phira-mp) 服务端侧协议库，基于Netty实现

### 相关项目
* [jphira-mp](https://github.com/lRENyaaa/jphira-mp)
* [pyphira-mp](https://github.com/Evi233/pyphira-mp)

## ⚙️ 依赖
[![](https://jitpack.io/v/lRENyaaa/jphira-mp-protocol.svg)](https://jitpack.io/#lRENyaaa/jphira-mp-protocol)  
jphira-mp-protocol 在 [JitPack](https://jitpack.io/) 上可用  

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.lRENyaaa</groupId>
    <artifactId>jphira-mp-protocol</artifactId>
    <version>2.0.0</version>
</dependency>
```

## 🚀 使用
提供一个简单的 `ChannelInitializer` 示例

```java
public class ServerChannelInitializer extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) {

        HandshakeDecoder handshake = new HandshakeDecoder();
        channel.pipeline().addLast(handshake);

        handshake.getClientProtocolVersion().whenComplete((version,throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                if (channel.isActive()) {
                    channel.close();
                }
                return;
            }

            InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();

            System.out.printf("Establishing a connection from %s: %s, client version: %s%n",
                    remoteAddress.getAddress().getHostAddress(),
                    remoteAddress.getPort(),
                    version
            );

            channel.pipeline()
                    .addLast(new FrameDecoder())
                    .addLast(new FrameEncoder())
                    .addLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addLast(new ServerPacketDecoder())
                    .addLast(new ServerPacketEncoder());

            // 在这添加你自己的 handlers
        });

    }
}
```

## 📜 开源协议
项目使用 Apache License 2.0 协议开源，见 [LICENSE](./LICENSE)

Copyright (C) 2026 lRENyaaa
