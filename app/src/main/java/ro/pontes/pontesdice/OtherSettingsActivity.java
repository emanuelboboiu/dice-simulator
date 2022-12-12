package ro.pontes.pontesdice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class OtherSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_settings);

        // For shake detector:
        CheckBox checkOnShake = (CheckBox) findViewById(R.id.onshake_checkbox);
        checkOnShake.setChecked(MainActivity.isOnShake);

        // For shake detector in pause:
        CheckBox checkOnShakePause = (CheckBox) findViewById(R.id.onshake_pause_checkbox);
        checkOnShakePause.setChecked(MainActivity.isOnShakeInPause);

        // For keeping screen awake:
        CheckBox checkIsWakeLock = (CheckBox) findViewById(R.id.iswakelock_checkbox);
        checkIsWakeLock.setChecked(MainActivity.isWakeLock);

        // Check the radio button depending of sorting method chosen:
        String[] aSorting = new String[]{"radio_none", "radio_ascendant",
                "radio_descendant"};
        int resID = getResources()
                .getIdentifier(aSorting[MainActivity.sortMethod], "id",
                        "ro.pontes.pontesdice");
        RadioButton radioButton = (RadioButton) findViewById(resID);
        radioButton.setChecked(true);

    } // end onCreate other settings.

    // Let's see what happens when a check box is clicked in other settings:
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        UsefulThings ut = new UsefulThings(getApplicationContext()); // to save
        // changes.

        // Check which check box was clicked
        switch (view.getId()) {
            case R.id.onshake_pause_checkbox:
                MainActivity.isOnShakeInPause = checked;
                ut.saveBooleanSettings("isOnShakeInPause",
                        MainActivity.isOnShakeInPause);
                break;
            case R.id.onshake_checkbox:
                MainActivity.isOnShake = checked;
                ut.saveBooleanSettings("isOnShake", MainActivity.isOnShake);
                break;
            case R.id.iswakelock_checkbox:
                MainActivity.isWakeLock = checked;
                ut.saveBooleanSettings("isWakeLock", MainActivity.isWakeLock);
                break;
        } // end switch.
    } // end the function called when the check box was clicked.

    // Now for sorting method, radio buttons:
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        String key = "sortMethod";

        // Check which radio button was clicked:
        switch (view.getId()) {
            case R.id.radio_none:
                if (checked) {
                    MainActivity.sortMethod = 0;
                }
                break;
            case R.id.radio_ascendant:
                if (checked) {
                    MainActivity.sortMethod = 1;
                }
                break;
            case R.id.radio_descendant:
                if (checked) {
                    MainActivity.sortMethod = 2;
                }
                break;
        } // } // end switch.

        // Save now the setting:
        UsefulThings ut = new UsefulThings(getApplicationContext()); // we need
        // it
        // for
        // saving
        // with
        // SharedPreferences.
        ut.saveIntSettings(key, MainActivity.sortMethod);
    } // end onRadioButtonClicked.

} // end other settings activity class.
