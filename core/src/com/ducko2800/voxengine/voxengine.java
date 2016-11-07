package com.ducko2800.voxengine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.ducko2800.voxengine.chunk.Chunk;

public class voxengine extends ApplicationAdapter implements InputProcessor {

    private PerspectiveCamera camera;

    private static String getShader(String path) {
        return Gdx.files.local(path).readString();
    }

    protected static ShaderProgram createMeshShader() {
        ShaderProgram.pedantic = false;
        ShaderProgram shader = new ShaderProgram(getShader("VertShader.glsl"), getShader("FragShader.glsl"));

        String log = shader.getLog();

        if (!shader.isCompiled()) {
            throw new GdxRuntimeException(log);
        }
        if (log != null && log.length() != 0) {
            System.out.println("Shader Log: "+log);
        }

        return shader;
    }

    ShaderProgram shader;
    private Chunk chunk;

    @Override
    public void create () {
        Gdx.input.setInputProcessor(this);
        shader = createMeshShader();
        camera = new PerspectiveCamera(75f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(-3f, 2f, -3f);
        camera.lookAt(0f, 0f, 0f);
        camera.near = 0;
        camera.far = 100;

        chunk = new Chunk();
    }

    @Override
    public void render () {
        Gdx.gl.glEnable(GL20.GL_BLEND); // enable and setup blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE); // enable culling (i.e. not drawing faces that can't see)
        Gdx.gl.glCullFace(GL20.GL_BACK);

        camera.update();

        shader.begin();

        shader.setUniformMatrix("u_projTrans", camera.combined);

        chunk.mesh.render(shader, GL20.GL_TRIANGLES, 0, chunk.vertexCount);

        shader.end();
    }

    /* Camera Stuff */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    private int dragX, dragY;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        dragX = screenX;
        dragY = screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float dX = (float)(screenX - dragX) / (float)Gdx.graphics.getWidth();
        float dY = (float)(dragY - screenY) / (float)Gdx.graphics.getHeight();
        dragX = screenX;
        dragY = screenY;
        camera.position.add(dX * 10f, dY * 10f, 0f);
        camera.update();
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }




}