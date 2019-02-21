package com.wanzhs.mqtt;

import com.wanzhs.mqtt.handle.DetailHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Slf4j
@Component
public class MqttBoot implements ApplicationContextAware {

    @Value("${netty.port}")
    private int NETTY_PORT;

    // io线程和工作线程
    NioEventLoopGroup group = new NioEventLoopGroup(4);
    NioEventLoopGroup workGroup = new NioEventLoopGroup(8);
    Channel channel = null;

    @PostConstruct
    public void start() {
        //启动辅助类
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture f = null;
        try {
            final DetailHandler detailHandler = new DetailHandler();
            bootstrap.group(group, workGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.localAddress(new InetSocketAddress(NETTY_PORT));
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(MqttEncoder.INSTANCE)
                            .addLast(new MqttDecoder())
                            .addLast(detailHandler);

                }
            });
            f = bootstrap.bind().syncUninterruptibly();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            channel = f.channel();// 应用程序会一直等待，直到channel关闭
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (f != null && f.isSuccess()) {
                System.out.println("netty server was start");
            } else {
                System.out.println("netty server was error");
            }
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("Shutdown Netty Server...");
        if (channel != null) {
            channel.close();
        }
        workGroup.shutdownGracefully();
        group.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}
