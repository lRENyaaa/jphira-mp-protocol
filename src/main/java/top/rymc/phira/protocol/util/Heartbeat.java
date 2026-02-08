package top.rymc.phira.protocol.util;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.ScheduledFuture;
import top.rymc.phira.protocol.packet.serverbound.ServerBoundPingPacket;

import java.util.concurrent.TimeUnit;

public final class Heartbeat {

    private static final AttributeKey<Heartbeat> KEY =
            AttributeKey.valueOf("PHIRA_HEARTBEAT");

    private final Channel channel;
    private final long interval;
    private final TimeUnit unit;
    private ScheduledFuture<?> task;

    private Heartbeat(Channel channel, long interval, TimeUnit unit) {
        this.channel = channel;
        this.interval = interval;
        this.unit = unit;
    }

    public static void start(Channel channel, long interval, TimeUnit unit) {
        if (channel.hasAttr(KEY) && channel.attr(KEY).get() != null) {
            throw new IllegalStateException("Heartbeat already started on this channel");
        }

        Heartbeat heartbeat = new Heartbeat(channel, interval, unit);
        channel.attr(KEY).set(heartbeat);

        heartbeat.doStart();

        channel.closeFuture().addListener(future -> heartbeat.stop());
    }

    public static Heartbeat get(Channel channel) {
        return channel.attr(KEY).get();
    }

    @SuppressWarnings("resource")
    private void doStart() {
        this.task = channel.eventLoop().scheduleAtFixedRate(this::sendPing, 0, interval, unit);
    }

    private void sendPing() {
        if (!channel.isActive()) {
            stop();
            return;
        }
        channel.writeAndFlush(ServerBoundPingPacket.INSTANCE);
    }

    public void stop() {
        if (task != null) {
            task.cancel(false);
            task = null;
        }
        channel.attr(KEY).set(null);
    }

    public boolean isRunning() {
        return task != null && !task.isDone();
    }
}
