package com.nox.socketsimple.netty1;

public interface Subject {
    public void register(MyChannel obj);
    public void unregister(MyChannel obj);
    public void notifyObservers(Object msgBody);
}
