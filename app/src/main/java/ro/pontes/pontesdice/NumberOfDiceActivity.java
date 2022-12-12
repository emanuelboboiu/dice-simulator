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

        UsefulThings ut = new UsefulThings(getApplicationContext()); // we need
        // it
        // for
        // saving
        // with
        // SharedPreferences.
        String key = "iNumberOfDice";

        // Check which radio button was clicked:
        switch (view.getId()) {
            case R.id.radio_1:
                if (checked) {
                    MainActivity.iNumberOfDice = 1;
                }
                break;
            case R.id.radio_2:
                if (checked) {
                    MainActivity.iNumberOfDice = 2;
                }
                break;
            case R.id.radio_3:
                if (checked) {
                    MainActivity.iNumberOfDice = 3;
                }
                break;
            case R.id.radio_4:
                if (checked) {
                    MainActivity.iNumberOfDice = 4;
                }
                break;
            case R.id.radio_5:
                if (checked) {
                    MainActivity.iNumberOfDice = 5;
                }
                break;
            case R.id.radio_6:
                if (checked) {
                    MainActivity.iNumberOfDice = 6;
                }
                break;
        } // } // end switch.

        // Save now the setting:
        ut.saveIntSettings(key, MainActivity.iNumberOfDice);
    } // end onRadioButtonClicked.

}
