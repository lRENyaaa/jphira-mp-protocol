# jphira-mp-protocol
Java版本的 [phira-mp](https://github.com/TeamFlos/phira-mp) 服务端侧协议库，基于Netty实现

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
    <version>1.1.2</version>
</dependency>
```

## 🚀 使用
提供一个简单的 `ChannelInitializer` 示例

```java
public class ServerChannelInitializer extends ChannelInitializer<Channel> {
    
    @Override
    protected void initChannel(Channel channel) {

        FrameDecoder decoder = new FrameDecoder();
        channel.pipeline().addLast(decoder);

        decoder.getClientProtocolVersion().whenComplete((version,throwable) -> {
            if (throwable != null) {
                return;
            }

            InetSocketAddress remoteAddress = (InetSocketAddress) channel.remoteAddress();
            String ipPort = remoteAddress.getAddress().getHostAddress() + ":" + remoteAddress.getPort();

            System.out.printf("Establishing a connection from %s, client version: %s%n",ipPort,version);

            channel.pipeline()
                    .addLast(new FrameEncoder())
                    .addLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addLast(new PacketDecoder())
                    .addLast(new PacketEncoder());

            // 在这添加你自己的 handlers
        });

    }
}
```

## 📜 开源协议
项目使用 Apache License 2.0 协议开源，见 [LICENSE](./LICENSE)