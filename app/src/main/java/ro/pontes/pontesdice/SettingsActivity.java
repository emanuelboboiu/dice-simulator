package ro.pontes.pontesdice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

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
        }
    } // end onCheckboxClicked() method.

} // end settings activity.
