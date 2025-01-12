/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
