package spring15.ec551.fpgacontroller.fragments;

/**
 * Created by davidkim on 3/28/15.
 */
public interface ControllerInterfaceListener {

    public void onBaseChangedListener(float valueX, float valueY, float valueZ);

    public void onFilterChangedListener(float valueX, float valueY, float valueZ);
}
