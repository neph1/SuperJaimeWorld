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
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rickard
 */
public class CollisionState extends BaseAppState implements PhysicsCollisionListener {

    private Spatial player;
    private final List<Spatial> pickups = new ArrayList<>();
    
    
    @Override
    protected void initialize(Application app) {
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }
    
    public void setPlayer(Spatial player) {
        this.player = player;
    }
    
    @Override
    public void collision(PhysicsCollisionEvent event) {
        final Spatial nodeA = event.getNodeA();
        final Spatial nodeB = event.getNodeB();
        if (nodeA == null || nodeB == null) {
            return;
        }
        final Spatial playerNode = nodeA == player ? nodeA : nodeB == player ? nodeB : null;
        if (playerNode == null) {
            return;
        }
        final Spatial enemyNode = nodeA.getControl(EnemyControl.class) != null ? nodeA : nodeB.getControl(EnemyControl.class) != null ? nodeB : null;
        if (enemyNode == null) {
            return;
        }
        
        final Vector3f directionVector = event.getNormalWorldOnB();
        if(directionVector.y > 0.5) {
            enemyNode.getControl(EnemyControl.class).kill();
            playerNode.getControl(BetterCharacterControl.class).getRigidBody().applyCentralImpulse(new Vector3f(0, 46f, 0));
        } else {
            getState(GameState.class).reset();
        }
    }
    
}
