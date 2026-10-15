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

        CheckBox checkHapticFeedback = findViewById(R.id.haptic_feedback_checkbox);
        checkHapticFeedback.setChecked(MainActivity.isHapticFeedback);

        CheckBox checkHapticResult = findViewById(R.id.haptic_result_checkbox);
        checkHapticResult.setChecked(MainActivity.isHapticResult);

        int cooldownButtonId;
        if (MainActivity.rollCooldownSeconds == 1) cooldownButtonId = R.id.radio_cooldown_1;
        else if (MainActivity.rollCooldownSeconds == 3) cooldownButtonId = R.id.radio_cooldown_3;
        else if (MainActivity.rollCooldownSeconds == 5) cooldownButtonId = R.id.radio_cooldown_5;
        else cooldownButtonId = R.id.radio_cooldown_2;
        ((RadioButton) findViewById(cooldownButtonId)).setChecked(true);

        // Check the radio button depending of sorting method chosen:
        int[] sortingButtons = {R.id.radio_none, R.id.radio_ascendant, R.id.radio_descendant};
        int resID = sortingButtons[Math.max(0, Math.min(2, MainActivity.sortMethod))];
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
        } else if (id == R.id.haptic_feedback_checkbox) {
            MainActivity.isHapticFeedback = checked;
            ut.saveBooleanSettings("isHapticFeedback", MainActivity.isHapticFeedback);
        } else if (id == R.id.haptic_result_checkbox) {
            MainActivity.isHapticResult = checked;
            ut.saveBooleanSettings("isHapticResult", MainActivity.isHapticResult);
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

    public void onCooldownRadioButtonClicked(View view) {
        if (!((RadioButton) view).isChecked()) return;

        int id = view.getId();
        if (id == R.id.radio_cooldown_1) MainActivity.rollCooldownSeconds = 1;
        else if (id == R.id.radio_cooldown_3) MainActivity.rollCooldownSeconds = 3;
        else if (id == R.id.radio_cooldown_5) MainActivity.rollCooldownSeconds = 5;
        else MainActivity.rollCooldownSeconds = 2;

        new UsefulThings(getApplicationContext())
                .saveIntSettings("rollCooldownSeconds", MainActivity.rollCooldownSeconds);
    }

} // end other settings activity class.
