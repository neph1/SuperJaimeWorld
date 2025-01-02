/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jme3test.superjaimeworld;

import com.jme3.anim.AnimComposer;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * Enemy with a simple movement pattern
 * 
 * @author rickard
 */
public class EnemyControl extends AbstractControl {

    private BetterCharacterControl physicsCharacter;
    private final float moveSpeed = 20f;
    private float direction = 0f;
    private final Vector3f viewDirection = new Vector3f();
    private final float turnSpeed = 0.15f;
    boolean alive = true;
    private Spatial body;
    private Spatial footL;
    private Spatial footR;
    
    @Override
    protected void controlUpdate(float tpf) {
        if (alive) {
            viewDirection.set(FastMath.cos(direction), 0f, FastMath.sin(-direction));
            physicsCharacter.setViewDirection(viewDirection);
            physicsCharacter.setWalkDirection(viewDirection.mult(moveSpeed * tpf));
            direction += turnSpeed * tpf;
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        physicsCharacter = spatial.getControl(BetterCharacterControl.class);
        body = ((Node)((Node)spatial).getChild(0)).getChild("Body");
        footL = ((Node)((Node)spatial).getChild(0)).getChild("Foot.L");
        footR = ((Node)((Node)spatial).getChild(0)).getChild("Foot.R");

        body.getControl(AnimComposer.class).setCurrentAction("Walk.Body");        
        footL.getControl(AnimComposer.class).setCurrentAction("Walk.Foot.L");
        footR.getControl(AnimComposer.class).setCurrentAction("Walk.Foot.R");

    }
    
    public void kill() {
        alive = false;
        spatial.removeControl(BetterCharacterControl.class);
        spatial.scale(1, 0.1f, 1);
        body.getControl(AnimComposer.class).setEnabled(false);
        footL.getControl(AnimComposer.class).setEnabled(false);
        footR.getControl(AnimComposer.class).setEnabled(false);
        physicsCharacter.setWalkDirection(Vector3f.ZERO.clone());
    }
    
}
