package spring15.ec551.fpgacontroller.fragments;

import spring15.ec551.fpgacontroller.accelerometer.ControllerObject;

/**
 * Created by davidkim on 3/25/15.
 */
public interface FragmentActionListener {
    /** Listens for the particular settings item clicked
     * @param itemName the string value associated with the settings item */
    public void onSettingsMenuItemClickListener(String itemName);

    // TODO
    /** Sends the controller object to activity level and check for validity.
     * @param savedSettings the data object containing saved configuration. */
    public void onSaveControllerConfiguration(ControllerObject savedSettings);

    public void enterMainMenuFragment();

    public void exitMainMenuFragment();

}
