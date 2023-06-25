package com.manofsteel.gd.util;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

public class Throttler {
    private final int maxRequestsPerSecond; // 초당 최대 허용 요청 수
    private final Queue<Instant> requestQueue; // 요청 큐

    public Throttler(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
        this.requestQueue = new LinkedList<>();
    }

    public synchronized void throttle() throws InterruptedException {
        // 현재 시간 기준으로 1초 이내의 요청들만 유지
        long currentTimeMillis = System.currentTimeMillis();
        Instant currentTime = Instant.ofEpochMilli(currentTimeMillis);
        while (!requestQueue.isEmpty() && requestQueue.peek().isBefore(currentTime.minusSeconds(1))) {
            requestQueue.poll();
        }

        // 초당 최대 허용 요청 수를 초과한 경우 대기
        if (requestQueue.size() >= maxRequestsPerSecond) {
            long waitTime = 1000 - (currentTimeMillis - requestQueue.peek().toEpochMilli());
            if (waitTime > 0) {
                Thread.sleep(waitTime);
            }
        }

        // 요청 큐에 현재 시간 추가
        requestQueue.offer(currentTime);
    }
}
