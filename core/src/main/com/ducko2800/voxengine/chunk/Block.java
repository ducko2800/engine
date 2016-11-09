package com.ducko2800.voxengine.chunk;

import com.badlogic.gdx.graphics.Color;

import java.util.Random;

/**
 * Created by josh on 07/11/2016.
 */
public class Block {
    public byte visibleFaces = 0;
    public static final byte SOLID = 0b0100_0000;
    public static final byte FACE_TOP = 0b0010_0000;
    public static final byte FACE_BOTTOM = 0b0001_0000;
    public static final byte FACE_NORTH = 0b0000_1000;
    public static final byte FACE_SOUTH = 0b0000_0100;
    public static final byte FACE_EAST = 0b0000_0010;
    public static final byte FACE_WEST = 0b0000_0001;

    public void setFlag(byte flag) {
        visibleFaces = (byte)(visibleFaces | flag);
    }

    public boolean hasFlag(byte flag) {
        return (flag & visibleFaces) == flag;
    }

    public Color getTopColor() {
        return getSideColor(); // yay hardcoding
    }

    public Color getSideColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        Color colour = new Color(r, g, b, 1);
        return colour;
    }
}