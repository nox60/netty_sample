package com.nox.socketsimple.netty1;

import io.netty.channel.ChannelHandlerContext;

public interface Subject {
    public void register(ChannelHandlerContext obj);
    public void unregister(ChannelHandlerContext obj);
    public void notifyObservers(Object msgBody);
}
