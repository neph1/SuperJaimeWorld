/*
 * Copyright (c) 2025 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jme3test.superjaimeworld;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphIterator;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This is the 'level'. Handles scene loading and spatials in the scene (except
 * player).
 *
 * @author rickard
 */
public class SceneState extends BaseAppState {

    private final BulletAppState physicsState;
    private Node rootNode;

    private Node scene;

    private Spatial statics;

    private Spatial ballGeometry;
    
    private final List<Spatial> pickups = new ArrayList<>();

    public SceneState(BulletAppState physicsState) {
        this.physicsState = physicsState;
    }

    @Override
    protected void initialize(Application app) {
        final AssetManager assetManager = app.getAssetManager();
        rootNode = ((SimpleApplication) app).getRootNode();

        scene = (Node) assetManager.loadModel("Scenes/jaime_world.j3o");

        statics = scene.getChild("Statics");
        statics.addControl(new RigidBodyControl(0));
        statics.getControl(RigidBodyControl.class).setFriction(0.01f);
        
        final Node pickupNode = (Node) scene.getChild("Pickups");
        for(Spatial pickup: pickupNode.getChildren()) {
            pickup.addControl(new PickupControl());
            ((Node)pickup).attachChild(assetManager.loadModel("Models/Banana/Banana.j3o"));
            pickups.add(pickup);
        }
         
        DirectionalLight light = new DirectionalLight(new Vector3f(0.5f, -0.5f, 0f));
        rootNode.addLight(light);

        FilterPostProcessor processor = assetManager.loadFilter("Filters/SceneFilter.j3f");
        DirectionalLightShadowFilter filter = new DirectionalLightShadowFilter(assetManager, 2048, 1);
        filter.setLight(light);
        processor.addFilter(filter);
        app.getViewPort().addProcessor(processor);

        SceneGraphIterator it = new SceneGraphIterator(rootNode);
        it.forEach(spatial -> spatial.setShadowMode(ShadowMode.CastAndReceive));
        
        loadEnemy(assetManager);

    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        final Vector3f playerPosition = getStateManager().getState(GameState.class).getPlayerPosition();
        for(Spatial pickup: pickups) {
            if (playerPosition.distance(pickup.getLocalTranslation()) < 1f) {
                pickup.getControl(PickupControl.class).pickup();
            }
        }
    }

    @Override
    protected void cleanup(Application aplctn) {
    }

    @Override
    protected void onEnable() {
        physicsState.getPhysicsSpace().add(statics);
        rootNode.attachChild(scene);
    }

    @Override
    protected void onDisable() {
        if (physicsState.getPhysicsSpace() == null) {
            // no physicsState when exiting app
            return;
        }
        physicsState.getPhysicsSpace().remove(statics);
        rootNode.detachChild(scene);
        rootNode.detachChild(ballGeometry);
        physicsState.getPhysicsSpace().remove(ballGeometry);
    }

    private void loadEnemy(AssetManager assetManager) {
        BetterCharacterControl enemy = new BetterCharacterControl(0.24f, 0.51f, 5.5f);

        physicsState.getPhysicsSpace().add(enemy);

        Node enemyNode = (Node) assetManager.loadModel("Models/Enemy/enemy.j3o");
        enemyNode.setLocalScale(0.5f);
        enemyNode.setLocalTranslation(new Vector3f(15f, 5f, -10f));

        enemyNode.addControl(enemy);
        enemyNode.addControl(new EnemyControl());
        scene.attachChild(enemyNode);
    }
}
