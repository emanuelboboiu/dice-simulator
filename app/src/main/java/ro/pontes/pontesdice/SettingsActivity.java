package ro.pontes.pontesdice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        // Check or check the check boxes, depending of current boolean values:

        // For dice sound:
        CheckBox checkDiceSound = (CheckBox) findViewById(R.id.checkbox_dice_sound);
        checkDiceSound.setChecked(MainActivity.isSoundDice);

        // For numbers sound:
        CheckBox checkNumbersSound = (CheckBox) findViewById(R.id.checkbox_numbers_sound);
        checkNumbersSound.setChecked(MainActivity.isNumberSpoken);

        RadioButton voiceType = findViewById(MainActivity.voiceMode == 1
                ? R.id.radio_system_voice : R.id.radio_recorded_voice);
        voiceType.setChecked(true);
        updateVoiceTypeAvailability();

    } // end onCreate settings activity.

    // Let's see what happens when a check box is clicked in General settings:
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        UsefulThings ut = new UsefulThings(getApplicationContext()); // to save changes.

        int id = view.getId();

        if (id == R.id.checkbox_dice_sound) {
            MainActivity.isSoundDice = checked;
            ut.saveBooleanSettings("isSoundDice", MainActivity.isSoundDice);
        } else if (id == R.id.checkbox_numbers_sound) {
            MainActivity.isNumberSpoken = checked;
            ut.saveBooleanSettings("isNumberSpoken", MainActivity.isNumberSpoken);
            updateVoiceTypeAvailability();
        }
    } // end onCheckboxClicked() method.

    public void onVoiceTypeClicked(View view) {
        if (!((RadioButton) view).isChecked()) return;
        MainActivity.voiceMode = view.getId() == R.id.radio_system_voice ? 1 : 0;
        new UsefulThings(getApplicationContext()).saveIntSettings("voiceMode", MainActivity.voiceMode);
    }

    private void updateVoiceTypeAvailability() {
        boolean enabled = MainActivity.isNumberSpoken;
        findViewById(R.id.voice_type_label).setEnabled(enabled);
        findViewById(R.id.radio_recorded_voice).setEnabled(enabled);
        findViewById(R.id.radio_system_voice).setEnabled(enabled);
    }

} // end settings activity.
