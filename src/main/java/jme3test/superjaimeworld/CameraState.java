/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
