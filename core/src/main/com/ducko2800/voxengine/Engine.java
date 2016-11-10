package com.ducko2800.voxengine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.Random;

public class Engine implements ApplicationListener {

    Texture brick, sand;
    private Environment env;
    private PerspectiveCamera camera;
    private Model brickBlock, sandBlock;
    private ModelBatch batch;
    private ModelInstance instance;
    private ArrayList<ModelInstance> instances;
    private CameraInputController input;
    private Random rand;

    public void create () {
        brick = new Texture(Gdx.files.internal("brick.png"), true);
        sand = new Texture(Gdx.files.internal("sand.png"), true);

        // trilinear filtering
        brick.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        sand.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        rand = new Random();
        instances = new ArrayList<>();
        batch = new ModelBatch();

        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0, 0, 0);
        camera.near = 1;
        camera.far = 1000;
        camera.update();

        input = new CameraInputController(camera);
        Gdx.input.setInputProcessor(input);

        env = new Environment();
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.f));
        env.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        ModelBuilder modelBuilder = new ModelBuilder();
        brickBlock = modelBuilder.createBox(1f, 1f, 1f, new Material(TextureAttribute.createDiffuse(brick),
                ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        sandBlock = modelBuilder.createBox(1f, 1f, 1f, new Material(TextureAttribute.createDiffuse(sand),
                ColorAttribute.createSpecular(1, 1, 1, 1), FloatAttribute.createShininess(8f)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    if (x*x + y*y + z*z > 16*16) {
                        continue;
                    }

                    instance = new ModelInstance(rand.nextBoolean() ? brickBlock : sandBlock);
                    instance.transform.translate(x, y, z);
                    instances.add(instance);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    public void render () {
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT ); // clears display window

        input.update();
        batch.begin(camera);
        batch.render(instances, env);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}