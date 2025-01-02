/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jme3test.superjaimeworld;

import com.jme3.effect.ParticleEmitter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * Wrapping the emitter emitting dust when landing after falling
 * 
 * @author rickard
 */
public class ImpactEmitterControl extends AbstractControl{
    
    private ParticleEmitter emitter;

    @Override
    protected void controlUpdate(float f) {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        
    }
    
    public void emitLarge(float force) {
        emitter.emitAllParticles();
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        emitter = (ParticleEmitter)((Node)getSpatial()).getChild(0);
    }
    
    
    
}
