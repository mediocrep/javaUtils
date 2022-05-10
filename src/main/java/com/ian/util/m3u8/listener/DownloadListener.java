package com.ian.util.m3u8.listener;

import java.io.IOException;

public interface DownloadListener {

    void start();

    void process(String downloadUrl, int finished, int sum, float percent);

    void speed(String speedPerSecond);

    void end();

}
