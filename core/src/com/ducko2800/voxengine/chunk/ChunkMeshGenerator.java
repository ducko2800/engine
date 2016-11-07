package com.ducko2800.voxengine.chunk;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by josh on 07/11/2016.
 */
public final class ChunkMeshGenerator {
    private static final float CUBE_SIZE = 1f;
    private static final int POSITION_COMPONENTS = 3;
    private static final int COLOUR_COMPONENTS = 4;
    private static final int NUM_COMPONENTS = POSITION_COMPONENTS + COLOUR_COMPONENTS;
    private static final int VERTICES_PER_TRIANGLE = 3;
    private static final int TRIANGLES_PER_FACE = 2;
    private static final int FACES_PER_CUBE = 6;
    private static final int MAX_VERTICES = VERTICES_PER_TRIANGLE * TRIANGLES_PER_FACE * FACES_PER_CUBE * Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE * Chunk.CHUNK_SIZE  * NUM_COMPONENTS;

    private static final VertexAttribute a_position = new VertexAttribute(VertexAttributes.Usage.Position, POSITION_COMPONENTS, "a_position");
    private static final VertexAttribute a_color = new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, COLOUR_COMPONENTS, "a_color");

    private static final float[] verts = new float[MAX_VERTICES];
    private static int numFloats;
    private static Block block;
    //private static byte x;
    //private static byte y;
    //private static byte z;
    private static int verticesCount;

    public static void createMesh(Chunk chunk) {
        if (chunk.hasMesh)
        {
            chunk.mesh.dispose();
        }

        numFloats = chunk.visibleFaces * TRIANGLES_PER_FACE * VERTICES_PER_TRIANGLE * NUM_COMPONENTS;
        chunk.mesh = new Mesh(true, numFloats, 0, a_position, a_color);

        verticesCount = 0;
        for (byte i = 0; i < Chunk.CHUNK_SIZE; i++) {
            for (byte j = 0; j < Chunk.CHUNK_SIZE; j++) {
                for (byte k = 0; k < Chunk.CHUNK_SIZE; k++) {

                    if (chunk.blocks[i][j][k].visibleFaces > 0) {
                        block = chunk.blocks[i][j][k];

                        if (block.hasFlag(Block.FACE_TOP)) {
                            addFace(i, j, k, Block.FACE_TOP, block.getTopColor());
                        }
                        if (block.hasFlag(Block.FACE_NORTH)) {
                            addFace(i, j, k, Block.FACE_NORTH, block.getSideColor());
                        }
                        if (block.hasFlag(Block.FACE_SOUTH)) {
                            addFace(i ,j, k, Block.FACE_SOUTH, block.getSideColor());
                        }
                        if (block.hasFlag(Block.FACE_EAST)) {
                            addFace(i, j, k, Block.FACE_EAST, block.getSideColor());
                        }
                        if (block.hasFlag(Block.FACE_WEST)) {
                            addFace(i, j, k, Block.FACE_WEST, block.getSideColor());
                        }
                        if (block.hasFlag(Block.FACE_BOTTOM)) {
                            addFace(i, j, k, Block.FACE_BOTTOM, block.getSideColor());
                        }
                    }
                }
            }
        }
        chunk.mesh.setVertices(verts, 0, verticesCount);
        chunk.vertexCount = verticesCount;
        System.out.println(verticesCount + " vertices");
        chunk.hasMesh = true;

    }

    private static final Vector3 south = new Vector3(1f, 1f, 0f).scl(CUBE_SIZE);
    private static final Vector3 west = new Vector3(0f, 1f, 1f).scl(CUBE_SIZE);
    private static final Vector3 east = new Vector3(0f, 1f, -1f).scl(CUBE_SIZE);
    private static final Vector3 north = new Vector3(-1f, 1f, 0f).scl(CUBE_SIZE);
    private static final Vector3 top = new Vector3(1f, 0f, -1f).scl(CUBE_SIZE);
    private static final Vector3 bottom = new Vector3(1f, 0f, 1f).scl(CUBE_SIZE);
    private static Vector3 d;

    private static void addFace(byte i, byte j, byte k, byte face, Color colour) {
        switch (face) {
            case Block.FACE_SOUTH:
                d = south;
                break;
            case Block.FACE_WEST:
                k--;
                d = west;
                break;
            case Block.FACE_TOP:
                j++;
                d = top;
                break;
            case Block.FACE_EAST:
                i++;
                d = east;
                break;
            case Block.FACE_NORTH:
                i++;
                k--;
                d = north;
                break;
            case Block.FACE_BOTTOM:
                // fall through
            default:
                k--;
                d = bottom;
                break;
        }

        //bottom left
        verts[verticesCount++] = i;
        verts[verticesCount++] = j;
        verts[verticesCount++] = k;
        addColour(colour);

        //top right
        verts[verticesCount++] = i + d.x;
        verts[verticesCount++] = j + d.y;
        verts[verticesCount++] = k + d.z;
        addColour(colour);

        //top left
        verts[verticesCount++] = i;
        verts[verticesCount++] = j + d.y;
        verts[verticesCount++] = (face & Block.FACE_BOTTOM) + (face & Block.FACE_TOP) > 0 ? k + d.z: k;
        addColour(colour);

        //bottom left
        verts[verticesCount++] = i;
        verts[verticesCount++] = j;
        verts[verticesCount++] = k;
        addColour(colour);

        //bottom right
        verts[verticesCount++] = i + d.x;
        verts[verticesCount++] = j;
        verts[verticesCount++] = (face & Block.FACE_BOTTOM) + (face & Block.FACE_TOP) > 0 ? k : k + d.z;
        addColour(colour);

        //top right
        verts[verticesCount++] = i + d.x;
        verts[verticesCount++] = j + d.y;
        verts[verticesCount++] = k + d.z;
        addColour(colour);
    }

    private static void addColour(Color colour) {
        verts[verticesCount++] = colour.r;
        verts[verticesCount++] = colour.g;
        verts[verticesCount++] = colour.b;
        verts[verticesCount++] = colour.a;
    }
}