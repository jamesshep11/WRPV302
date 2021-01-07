package com.example.segrada.Die;

public class Timer extends Thread {
	private long interval;
	private Runnable tick;
	private boolean While;

	public Timer(long interval, Runnable tick, boolean While) {
		this.interval = interval;
		this.tick = tick;
		this.While = While;
	}

	@Override
	public void run() {
		try {
			while (While) {
				Thread.sleep(interval);
				tick.run();
			}
		} catch (InterruptedException e) {
		}
	}
}
