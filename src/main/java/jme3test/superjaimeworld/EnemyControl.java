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
            direction += turnSpeed * tpf;
            
            physicsCharacter.setViewDirection(viewDirection);
            physicsCharacter.setWalkDirection(viewDirection.mult(moveSpeed * tpf));
            
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
