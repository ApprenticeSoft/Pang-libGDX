package com.pang.utils;

public class Blinker {
	private float BLINK_TIME = 1f;
	private int BLINKING_FRAMES = 2;

	private boolean isBlinking;
	private int blinkFrameCounter;
	private float blinkTimer;

	public Blinker() {
	    this.blinkTimer = 0;
	    this.blinkFrameCounter = 0;
	    this.isBlinking = false;
	}

	public boolean shouldBlink(float delta) {
	    if (isBlinking) {
	        blinkTimer += delta;
	        blinkFrameCounter++;
	        if (blinkTimer < BLINK_TIME) {
	            if (blinkFrameCounter % BLINKING_FRAMES == 0) {
	                return true;
	            }
	        } else {
	            blinkTimer = 0;
	            isBlinking = false;
	        }
	    }
	    return false;
	}

	public boolean shouldBlink() {
	    if (isBlinking) {
	        blinkFrameCounter++;
	        if ((blinkFrameCounter/10) % BLINKING_FRAMES == 0)
	        	return true;
	    }
	    return false;
	}

	public boolean isBlinking() {
	    return isBlinking;
	}

	public void setBlinking(boolean isBlinking) {
	    this.isBlinking = isBlinking;
	}
	
	public void init(){
		isBlinking = true;
		blinkTimer = 0;
	    blinkFrameCounter = 0;
	}
	
}
