package com.nox.socketsimple.multi_channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by LiuLi on 2019/4/8.
 */
public class BaseClient1Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("BaseClient1Handler channelActive  1111 ");

        /*
         * 唤醒下一个Handler的相关方法
         */
        ctx.fireChannelActive();
        System.out.println("------------------------------------------");

        ctx.fireChannelRead("");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("BaseClient1Handler channelInactive");
    }

}


