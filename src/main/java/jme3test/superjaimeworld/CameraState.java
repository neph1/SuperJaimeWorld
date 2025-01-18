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
import com.jme3.input.ChaseCamera;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 *
 * @author rickard
 */
public class CameraState extends BaseAppState{
    
    private final Spatial playerNode;
    private Camera cam;
    private ChaseCamera chaseCam;
    private final float startRotation = FastMath.PI * 0.5f;
    
    public CameraState(Spatial playerNode) {
        this.playerNode = playerNode;
    }

    @Override
    protected void initialize(Application app) {
        this.cam = app.getCamera();
        cam.setLocation(new Vector3f(10f, 6f, -5f));
        chaseCam = new ChaseCamera(cam, playerNode, app.getInputManager());
        chaseCam.setDragToRotate(false);
        chaseCam.setSmoothMotion(true);
        chaseCam.setLookAtOffset(new Vector3f(0, 1f, 0));
        chaseCam.setDefaultDistance(12f);
        chaseCam.setMaxDistance(20f);
        chaseCam.setMinDistance(5f);
        chaseCam.setTrailingSensitivity(50);
        chaseCam.setChasingSensitivity(10);
        chaseCam.setRotationSpeed(5);
        chaseCam.setDefaultHorizontalRotation(startRotation);
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
        chaseCam.setDefaultHorizontalRotation(startRotation);
    }

    @Override
    protected void onDisable() {
    }
    
}
