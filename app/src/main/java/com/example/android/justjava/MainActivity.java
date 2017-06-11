package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    //declaring a global quantity variable
    public int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display(quantity);
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {

        if (quantity != 100) {
            display(++quantity);
        } else {
            Toast.makeText(this, "You cannot order more than 100 cups of coffee",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (quantity != 1) {
            display(--quantity);
        } else {
            Toast.makeText(this, "You cannot order less than 1 cup of coffee",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {


        /*Is Whipped Cream Checked?*/
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.checkBox_whipped_cream);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        /*Is Chocolate Checked?*/
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.checkBox_chocolate);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        /*Calculate Price*/

        int price = calculatePrice(hasWhippedCream, hasChocolate);

        /*Get the name from the EditText field*/
        EditText nameText = (EditText) findViewById(R.id.name_input_field);
        String name = nameText.getText().toString();

        /*Capitalizing the first letter of each word in the name*/
        StringBuilder formattedName = new StringBuilder("");//A new StringBuilder
        if (!name.isEmpty()) {
            for (String flname : name.split(" ")) {               //Split int works by space
                char[] c = flname.toCharArray();                //word to character array
                StringBuilder sb = new StringBuilder(flname);   //add word to the StringBuilder
                sb.setCharAt(0, Character.toUpperCase(c[0]));    //Change first letter of that word to a capital
                formattedName.append(sb).append(" ");           //Append a space to the String Builder
            }
        }

        String orderSummary = createOrderSummary(price, hasWhippedCream, hasChocolate, formattedName.toString());
        composeEmail(orderSummary, formattedName.toString());
//        displayMessage(orderSummary);

    }

    /*
    * This method calculates and returns the price
    */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int basePrice = 5;
        if (hasWhippedCream) basePrice += 1;
        if (hasChocolate) basePrice += 2;
        return basePrice * quantity;
    }

    /*
    * Creates and returns an order summary
    */
    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate,
                                      String name) {
        String returnMessage = "";
        if (!name.isEmpty()) {
            returnMessage += getString(R.string.order) + "\n";
        }
        if (hasWhippedCream) {
            returnMessage += "Whipped Cream Topping : $1";
        }
        if (hasChocolate) {
            returnMessage += "\nChocolate Topping: $2";
        }
        returnMessage += "\nQuantity: " + quantity;
        returnMessage += "\nTotal: $" + price;
        returnMessage += "\n" + getString(R.string.thank_you);
        return returnMessage;

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int quantity) {
        /*Get the text view whose content we want changed*/
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        String textToDisplay = "" + Integer.toString(quantity);
        quantityTextView.setText(textToDisplay);
    }
//
//    private void displayMessage(String message) {
//        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_vew);
//        orderSummaryTextView.setText(message);
//
//    }

    private void composeEmail(String orderSummary, String fullName) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Just Java Order for " + fullName);
        emailIntent.putExtra(Intent.EXTRA_TEXT, orderSummary);
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }

    }
}