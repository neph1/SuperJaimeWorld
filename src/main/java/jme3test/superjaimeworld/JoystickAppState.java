/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jme3test.superjaimeworld;


import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.MouseInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles joystick / gamepad input.
 * Adapted from TestJoystick.java
 * 
 * @author rickard
 */
public class JoystickAppState extends BaseAppState {

    private Joystick viewedJoystick;
    private float deadZone;
    

    protected void dumpJoysticks( Joystick[] joysticks, PrintWriter out ) {
        for( Joystick j : joysticks ) {
            out.println( "Joystick[" + j.getJoyId() + "]:" + j.getName() );
            out.println( "  buttons:" + j.getButtonCount() );
            for( JoystickButton b : j.getButtons() ) {
                out.println( "   " + b );
            }
            
            out.println( "  axes:" + j.getAxisCount() );
            for( JoystickAxis axis : j.getAxes() ) {
                out.println( "   " + axis );
            }
        }
    }

    @Override
    protected void initialize(Application app) {
        
        final InputManager inputManager = app.getInputManager();
        deadZone = inputManager.getAxisDeadZone();

        Joystick[] joysticks = inputManager.getJoysticks();
        if (joysticks == null)
            throw new IllegalStateException("Cannot find any joysticks!");

        try {
            PrintWriter out = new PrintWriter( new FileWriter( "joysticks-" + System.currentTimeMillis() + ".txt" ) );
            dumpJoysticks( joysticks, out );
            out.close();
        } catch( IOException e ) {
            throw new RuntimeException( "Error writing joystick dump", e );
        }   

        // Add a raw listener because it's easier to get all joystick events
        // this way.
        inputManager.addRawInputListener(new JoystickEventListener(null) );
        
        // add action listener for mouse click 
        // to all easier custom mapping
        inputManager.addMapping("mouseClick", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener((ActionListener) (String name, boolean isPressed, float tpf) -> {
            
        }, "mouseClick");
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
 
    /**
     *  Easier to watch for all button and axis events with a raw input listener.
     */   
    protected class JoystickEventListener implements RawInputListener {

        private final ActionListener actionListener;

        protected JoystickEventListener(com.jme3.input.controls.ActionListener actionListener) {
            this.actionListener = actionListener;
        }

        @Override
        public void onJoyAxisEvent(JoyAxisEvent evt) {
            float value = evt.getValue();
            
            float effectiveDeadZone = Math.max(deadZone, evt.getAxis().getDeadZone());
            System.out.println(evt);
//            final String axisName = evt.getAxis().getName();
//            if (axisName.equals(JoystickAxis.X_AXIS) && value > effectiveDeadZone) {
//                actionListener.onAction("Strafe Right", true, 1f);
//            } else {
//                actionListener.onAction("Strafe Right", false, 1f);
//            }
//            if (axisName.equals(JoystickAxis.X_AXIS) && value < -effectiveDeadZone) {
//                actionListener.onAction("Strafe Left", true, 1f);
//            } else {
//                actionListener.onAction("Strafe Left", false, 1f);
//            }
//            if (axisName.equals(JoystickAxis.Y_AXIS) && value > effectiveDeadZone) {
//                actionListener.onAction("Walk Forward", true, 1f);
//            } else {
//                actionListener.onAction("Walk Forward", false, 1f);
//            }
//            if (axisName.equals(JoystickAxis.Y_AXIS) && value < -effectiveDeadZone) {
//                actionListener.onAction("Walk Backward", true, 1f);
//            } else {
//                actionListener.onAction("Walk Backward", false, 1f);
//            }
            
        }

        @Override
        public void onJoyButtonEvent(JoyButtonEvent evt) {
            System.out.println(evt);
        }

        @Override
        public void beginInput() {}
        @Override
        public void endInput() {}
        @Override
        public void onMouseMotionEvent(MouseMotionEvent evt) {}
        @Override
        public void onMouseButtonEvent(MouseButtonEvent evt) {}
        @Override
        public void onKeyEvent(KeyInputEvent evt) {
            
        }
        @Override
        public void onTouchEvent(TouchEvent evt) {}        
    }
    
}
