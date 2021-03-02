package com.alliance.radish;

public class T005_synchronized {

    private int count = 10;

    public void m(){
        synchronized (this){
            count --;
            System.out.println(Thread.currentThread().getName() + " count = " + count);
        }
    }
}
