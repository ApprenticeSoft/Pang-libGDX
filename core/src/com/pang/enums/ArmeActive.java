package com.pang.enums;

import com.badlogic.gdx.math.MathUtils;

public enum ArmeActive {
	CORDE_SIMPLE,
	CORDE_DOUBLE,
	GRAPPIN,
	TROMBLON,
	MITRAILLETTE;
	
	public static ArmeActive getRandom() {
        //return values()[(int) (Math.random() * values().length)];
        return values()[MathUtils.random(1, values().length-1)];
    }
}
