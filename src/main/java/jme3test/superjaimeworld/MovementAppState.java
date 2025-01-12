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

import com.jme3.anim.AnimComposer;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Receives input and handles player movement and animations
 *
 * @author rickard
 */
public class MovementAppState extends BaseAppState implements ActionListener {

    private BetterCharacterControl physicsCharacter;
    private AnimComposer animControl;

    private final Vector3f walkDirection = new Vector3f(0, 0, 0);
    private final float moveSpeed = 210f;
    private final Vector3f jumpForce = new Vector3f(0, 16, 0);

    private boolean leftPressed = false, rightPressed = false, forwardPressed = false, backwardPressed = false;

    private String currentAnimation = "Idle";

    private float fallingTime = 0f;
    
    private final Vector3f cameraForwardDirection = new Vector3f();    
    private final Vector3f cameraLeftDirection = new Vector3f();
    
    private ParticleEmitter runEmitter;
    private Spatial smokeEmitter;


    private final Camera cam;
    
    private boolean jumping = false;
    private boolean doubleJumping = false;

    public MovementAppState(Camera cam) {
        this.cam = cam;
    }

    @Override
    public void update(float tpf) {
        cameraForwardDirection.set(cam.getDirection().setY(0));
        cameraLeftDirection.set(cam.getLeft().setY(0));
        physicsCharacter.getViewDirection(walkDirection);
        if (leftPressed) {
            walkDirection.interpolateLocal(cameraLeftDirection, 0.75f);
        } else if (rightPressed) {
            walkDirection.interpolateLocal(cameraLeftDirection.negate(), 0.75f);
        }
        if (forwardPressed) {
            walkDirection.interpolateLocal(cameraForwardDirection, 0.75f);
        } else if (backwardPressed) {
            walkDirection.interpolateLocal(cameraForwardDirection.negate(), 0.75f);
        }
        walkDirection.normalizeLocal();
        if (!leftPressed && !rightPressed && !forwardPressed && !backwardPressed) {
            walkDirection.set(Vector3f.ZERO);
            runEmitter.setParticlesPerSec(0);
        }
        physicsCharacter.setWalkDirection(walkDirection.multLocal(moveSpeed * tpf));
        if(walkDirection.length() > 0) {
            physicsCharacter.setViewDirection(walkDirection);
            runEmitter.setParticlesPerSec(10);
        }

        if (!physicsCharacter.isOnGround()) {
            fallingTime += tpf;
            runEmitter.setParticlesPerSec(0);
        } else if (fallingTime > 0) {
            if (fallingTime > 0.5) {
                smokeEmitter.getControl(ImpactEmitterControl.class).emitLarge(fallingTime * 2f);
            }
            fallingTime = 0f;
            jumping = doubleJumping = false;
            
        }

        updateAnimation();
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        
        switch (binding) {
            case "Strafe Left" -> {
                leftPressed = value;
                break;
            }
            case "Strafe Right" -> {
                rightPressed = value;
                break;
            }
            case "Walk Forward" -> {
                forwardPressed = value;
                break;
            }
            case "Walk Backward" -> {
                backwardPressed = value;
                break;
            }
            case "Jump" -> {
                if (!value) {
                    return;
                }
                if (!jumping) {
                    physicsCharacter.jump();
                } else if (!doubleJumping) {
                    doubleJumping = true;
                    physicsCharacter.getRigidBody().applyCentralImpulse(jumpForce);
                }
                animControl.setCurrentAction("JumpStart");
                jumping = true;
                runEmitter.setParticlesPerSec(0);
                break;
            }
            default -> {
            }
        }
    }

    public void setPlayer(Spatial spatial) {
        physicsCharacter = spatial.getControl(BetterCharacterControl.class);
        physicsCharacter.setJumpForce(jumpForce);
        animControl = spatial.getControl(AnimComposer.class);
        runEmitter = (ParticleEmitter) ((Node)((Node)spatial).getChild("RunEmitter")).getChild(0);
        smokeEmitter = ((Node)((Node)spatial).getChild("SmokeEmitter"));
    }

    /**
     * To avoid starting over all the time, animations are only set if they are
     * different from the current one
     */
    private void updateAnimation() {
        if (fallingTime > 0.25f && !currentAnimation.equals("Jumping")) {
            animControl.setCurrentAction(currentAnimation = "Jumping");
            return;
        }
        if (currentAnimation.equals("JumpStart")) {
            return;
        }
        if (leftPressed || rightPressed || forwardPressed || backwardPressed) {
            if (!currentAnimation.equals("Walk")) {
                animControl.setCurrentAction(currentAnimation = "Walk");
            }
            return;
        }
        if (!currentAnimation.equals("Idle")) {
            animControl.setCurrentAction(currentAnimation = "Idle");
        }
    }

    @Override
    protected void initialize(Application aplctn) {
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

}
