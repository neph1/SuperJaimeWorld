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

import com.jme3.anim.util.AnimMigrationUtils;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * Input handling and camera. Loads player character
 *
 * @author rickard
 */
public class GameState extends BaseAppState {

    private SimpleApplication app;
    private final BulletAppState physicsState;
    private InputManager inputManager;
    private Node rootNode;
    

    private BetterCharacterControl physicsCharacter;
    private Node playerNode;

    private final Vector3f startTranslation = new Vector3f(0f, 2f, 0f);
    private final Vector3f gravity = new Vector3f(0f, -10f, 0f);
    

    public GameState(BulletAppState physicsState) {
        this.physicsState = physicsState;
    }

    @Override
    protected void initialize(Application app) {

        this.app = (SimpleApplication) app;
        this.inputManager = app.getInputManager();
        this.rootNode = ((SimpleApplication) app).getRootNode();

        setupCharacter(app.getAssetManager());
        
        app.getStateManager().getState(CollisionState.class).setPlayer(playerNode);
        
        final CameraState camState = new CameraState(playerNode);
        app.getStateManager().attach(camState);

        setupKeys();

        setupChaseCam();

    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        if (playerNode.getLocalTranslation().y < -20) {
            reset();
        }
    }

    @Override
    protected void cleanup(Application aplctn) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    public void reset() {
        physicsCharacter.warp(startTranslation);
    }

    private void setupKeys() {
        inputManager.addMapping("Strafe Left",
                new KeyTrigger(KeyInput.KEY_A),
                new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Strafe Right",
                new KeyTrigger(KeyInput.KEY_D),
                new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Walk Forward",
                new KeyTrigger(KeyInput.KEY_W),
                new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Walk Backward",
                new KeyTrigger(KeyInput.KEY_S),
                new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("Jump",
                new KeyTrigger(KeyInput.KEY_F),
                new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Reset",
                new KeyTrigger(KeyInput.KEY_R));
        inputManager.addListener(getState(MovementAppState.class), "Strafe Left", "Strafe Right");
        inputManager.addListener(getState(MovementAppState.class), "Walk Forward", "Walk Backward");
        inputManager.addListener(getState(MovementAppState.class), "Jump");
        inputManager.addListener((ActionListener) (String string, boolean bln, float f) -> {
            if (!bln) {
                reset();
            }
        }, "Reset");
    }

    private void setupChaseCam() {
        
    }

    private void setupCharacter(AssetManager assetManager) {

        physicsCharacter = new BetterCharacterControl(0.4f, 1.5f, 3f);

        physicsState.getPhysicsSpace().add(physicsCharacter);
        physicsState.getPhysicsSpace().setGravity(gravity);

        playerNode = (Node) assetManager.loadModel("Models/Jaime/Jaime.j3o");
        playerNode.setLocalTranslation(startTranslation);

        playerNode.addControl(physicsCharacter);
        
        // Since Jaime was created using the old animation system
        // it needs to be converted to the new one.
        AnimMigrationUtils.migrate(playerNode);

        

        rootNode.attachChild(playerNode);
        
        Spatial impactEmitter = assetManager.loadModel("Models/SmokeEmitter.j3o");
        impactEmitter.addControl(new ImpactEmitterControl());
        playerNode.attachChild(impactEmitter);
        Spatial runEmitter = assetManager.loadModel("Models/RunEmitter.j3o");
        playerNode.attachChild(runEmitter);
        
        getState(MovementAppState.class).setPlayer(playerNode);
    }

    public Vector3f getPlayerPosition() {
        return playerNode.getLocalTranslation();
    }
}
