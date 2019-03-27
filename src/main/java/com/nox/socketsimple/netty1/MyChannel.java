package com.nox.socketsimple.netty1;

import io.netty.channel.Channel;

import java.util.Objects;

/**
 * Created by LiuLi on 2019/3/26.
 */
public class MyChannel  {
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    //重写equel方法。保证能够放入list
    @Override
    public boolean equals(Object obj) {
        if( null == channel && obj != null ) return false;
        if( null == channel && obj == null ) return true;
        if (obj instanceof MyChannel) {
            MyChannel b = (MyChannel) obj;
            return this.channel.id().equals(b.channel.id());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.channel.id());
    }
}
