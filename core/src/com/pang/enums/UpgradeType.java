package com.pang.enums;

import com.badlogic.gdx.math.MathUtils;

public enum UpgradeType {
	BALLE_PAUSE,
	DYNAMITE,
	BOMBE,
	BOUCLIER; 

	public static UpgradeType getRandom() {
        //return values()[(int) (Math.random() * values().length)];
        return values()[MathUtils.random(0, values().length-1)];
    }
}
