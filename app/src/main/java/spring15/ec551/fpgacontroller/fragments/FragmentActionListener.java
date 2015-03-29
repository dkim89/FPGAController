package spring15.ec551.fpgacontroller.fragments;

import spring15.ec551.fpgacontroller.resources.UserConfigurationObject;

/**
 * Created by davidkim on 3/25/15.
 */
public interface FragmentActionListener {
    /** Listens for the particular settings item clicked
     * @param itemName the string value associated with the settings item */
    public void onSettingsMenuItemClickListener(String itemName);

    /** Implement the back button press by Activity for ExamineAccelFragment */
    public void initializeExamineAccelerometerBackButton();

    /** Implement the back button press by Activity for ControllerSettings Fragment */
    public void initializeControllerSettingsBackButton();


    /** Sends the saved configuration object to activity level and check for validity.
     * @param userConfig the data object containing saved configuration. */
    public void onConfigurationSettingsImplemented(UserConfigurationObject userConfig);
}
