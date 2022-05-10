package com.ian.util;

public class MyTimer {

    private Long start;

    private Long end;

    public MyTimer() {
        // this.start = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();  这个也行，不过语句有点复杂
        this.start = System.currentTimeMillis();
    }

    /**
     * 计算持续的时长，单位 毫秒
     * @return
     */
    public long duration() {
        // this.end = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();  这个也行，不过语句有点复杂
        this.end = System.currentTimeMillis();
        return this.end - this.start;
    }
}
