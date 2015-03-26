package spring15.ec551.fpgacontroller;

/**
 * Created by davidkim on 3/26/15.
 */
public interface ControllerInterfaceListener {
    public void onJoystickUp();
    public void onJoystickDown();
    public void onJoystickLeft();
    public void onJoyStickRight();

    public void onButtonA();

    public void onLeftBumper();
    public void onRightBumper();
}
