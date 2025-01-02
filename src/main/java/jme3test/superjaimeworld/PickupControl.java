/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jme3test.superjaimeworld;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author rickard
 */
public class PickupControl extends AbstractControl {

    private float height;
    private final float rotationSpeed = 0.5f;
    private final Vector3f startPosition = new Vector3f();
    
    @Override
    protected void controlUpdate(float tpf) {
        spatial.setLocalTranslation(startPosition.add(0, FastMath.sin(height) * 0.5f + 0.5f, 0));
        spatial.rotate(0, rotationSpeed * tpf, 0);
        height = (height + tpf) % FastMath.PI;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void pickup() {
        spatial.removeFromParent();
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        startPosition.set(spatial.getLocalTranslation());
        height = FastMath.nextRandomFloat() * 0.5f;
        spatial.rotate(0, FastMath.nextRandomFloat(), 0);
    }
    
    
}
