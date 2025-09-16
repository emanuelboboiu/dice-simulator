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

        UsefulThings ut = new UsefulThings(getApplicationContext());

        int id = view.getId();

        if (id == R.id.onshake_pause_checkbox) {
            MainActivity.isOnShakeInPause = checked;
            ut.saveBooleanSettings("isOnShakeInPause", MainActivity.isOnShakeInPause);
        } else if (id == R.id.onshake_checkbox) {
            MainActivity.isOnShake = checked;
            ut.saveBooleanSettings("isOnShake", MainActivity.isOnShake);
        } else if (id == R.id.iswakelock_checkbox) {
            MainActivity.isWakeLock = checked;
            ut.saveBooleanSettings("isWakeLock", MainActivity.isWakeLock);
        }
    } // end the function called when the check box was clicked.

    // Now for sorting method, radio buttons:
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        String key = "sortMethod";

        int id = view.getId();

        if (id == R.id.radio_none && checked) {
            MainActivity.sortMethod = 0;
        } else if (id == R.id.radio_ascendant && checked) {
            MainActivity.sortMethod = 1;
        } else if (id == R.id.radio_descendant && checked) {
            MainActivity.sortMethod = 2;
        }

        // Save the setting:
        UsefulThings ut = new UsefulThings(getApplicationContext());
        ut.saveIntSettings(key, MainActivity.sortMethod);
    } // end onRadioButtonClicked.

} // end other settings activity class.
