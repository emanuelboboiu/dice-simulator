package ro.pontes.pontesdice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class NumberOfDiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_dice);

        // Check the radio button depending of number of dice chosen:
        String rb = "radio_" + MainActivity.iNumberOfDice;
        int resID = getResources().getIdentifier(rb, "id",
                "ro.pontes.pontesdice");
        RadioButton radioButton = (RadioButton) findViewById(resID);
        radioButton.setChecked(true);

    } // end onCreate method.

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        UsefulThings ut = new UsefulThings(getApplicationContext());
        String key = "iNumberOfDice";

        int id = view.getId();

        if (id == R.id.radio_1 && checked) {
            MainActivity.iNumberOfDice = 1;
        } else if (id == R.id.radio_2 && checked) {
            MainActivity.iNumberOfDice = 2;
        } else if (id == R.id.radio_3 && checked) {
            MainActivity.iNumberOfDice = 3;
        } else if (id == R.id.radio_4 && checked) {
            MainActivity.iNumberOfDice = 4;
        } else if (id == R.id.radio_5 && checked) {
            MainActivity.iNumberOfDice = 5;
        } else if (id == R.id.radio_6 && checked) {
            MainActivity.iNumberOfDice = 6;
        }

        // Save the setting:
        ut.saveIntSettings(key, MainActivity.iNumberOfDice);
    } // end onRadioButtonClicked.

} // end class.
