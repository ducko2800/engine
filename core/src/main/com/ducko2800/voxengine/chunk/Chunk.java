package com.ducko2800.voxengine.chunk;

import com.badlogic.gdx.graphics.Mesh;

/**
 * Created by Tiget on 29/06/2015.
 */
public class Chunk {
    public static final byte CHUNK_SIZE = 16;
    public final Block[][][] blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
    public int visibleFaces = 0;
    public Mesh mesh;
    public int vertexCount;
    public boolean hasMesh = false;

    public Chunk() {
        for (int j = 0; j < CHUNK_SIZE; j++)
            for (int i = 0; i < CHUNK_SIZE; i++)
                for (int k = 0; k < CHUNK_SIZE; k++) {
                    if (j == 0) {
                        blocks[i][j][k] = new Block();
                        blocks[i][j][k].setFlag(Block.FACE_TOP);
                        blocks[i][j][k].setFlag(Block.SOLID);
                        visibleFaces++;
                        if (i == 0) {
                            blocks[i][j][k].setFlag(Block.FACE_WEST);
                            visibleFaces++;
                        }
                        if (k == 0) {
                            blocks[i][j][k].setFlag(Block.FACE_NORTH);
                            visibleFaces++;
                        }
                    } else {
                        blocks[i][j][k] = new Block();
                    }
                }

        ChunkMeshGenerator.createMesh(this);
    }
}
