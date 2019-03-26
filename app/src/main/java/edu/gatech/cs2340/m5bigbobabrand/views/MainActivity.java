package edu.gatech.cs2340.m5bigbobabrand.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import edu.gatech.cs2340.m5bigbobabrand.Model.GameState;
import edu.gatech.cs2340.m5bigbobabrand.R;
import edu.gatech.cs2340.m5bigbobabrand.entity.Coordinates;
import edu.gatech.cs2340.m5bigbobabrand.entity.Difficulty;
import edu.gatech.cs2340.m5bigbobabrand.entity.Player;
import edu.gatech.cs2340.m5bigbobabrand.entity.SolarSystem;
import edu.gatech.cs2340.m5bigbobabrand.entity.Universe;

import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    /**
     * UI Items
     */
    private Spinner difficultySpinner;
    private EditText nameField;
    private EditText pilotField;
    private EditText fighterField;
    private EditText traderField;
    private EditText engineerField;



    /**
     *Data for player being edited
     *
     */
    private Player player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nameField = findViewById(R.id.player_name_input);
        pilotField = findViewById(R.id.pilot_field);
        fighterField = findViewById(R.id.fighter_field);
        traderField = findViewById(R.id.trader_field);
        engineerField = findViewById(R.id.engineer_field);
        difficultySpinner = findViewById(R.id.difficulty_spinner);
        Difficulty[] difficultyList = Difficulty.values();
        String[] difficultyStrings = new String[difficultyList.length];
        for (int i = 0; i < difficultyStrings.length; i++) {
            difficultyStrings[i] = difficultyList[i].getString();
        }
        ArrayAdapter<String> difficultyArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficultyStrings);
        difficultyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyArrayAdapter);
        /* Check for student being passed in */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * Button handler for the add new student button
     *
     * @param view the button that was pressed
     */
    public void onCreatePressed(View view) {

        try {
            Log.d("Edit", "Create Player Pressed");
            Toast.makeText(this, "asdfasdf", Toast.LENGTH_LONG).show();

            this.player = new Player();

            player.setName(nameField.getText().toString());
            player.setEngineerPts(Integer.parseInt(engineerField.getText().toString()));
            player.setFighterPts(Integer.parseInt(fighterField.getText().toString()));
            player.setPilotPts(Integer.parseInt(pilotField.getText().toString()));
            player.setTraderPts(Integer.parseInt(traderField.getText().toString()));
            String chosenDiffString = (String) difficultySpinner.getSelectedItem();
            Difficulty[] difficultyList = Difficulty.values();
            for (Difficulty d: difficultyList) {
                if(chosenDiffString.equals(d.getString())) {
                    player.setDifficulty(d);
                    break;
                }
            }
            try {
                if (!player.verifySum()) {
                    throw new IllegalArgumentException("Stats must be positive and sum to 16");
                }
                Log.d("Edit", "\nName: " + player.getName() + "\nPilot Points: "
                        + player.getPilotPts() + "\nFighter Points: " + player.getFighterPts()
                        +  "\nTrader Points: " + player.getTraderPts() +
                        "\nEngineer Points: " + player.getEngineerPts()
                        + "\nDifficulty: " + player.getDifficulty().getString()
                        + "\nCredits: " + player.getCredits()
                        + "\nShip Type: " + player.getShip().toString());
                Universe gameUniverse = new Universe();
                ArrayList<Coordinates> coordinatesArrayList = new ArrayList<>();
                while (gameUniverse.size() < 10) {
                    Coordinates coord = new Coordinates();
                    int counter = 0;
                    int differenceThreshold = 7;
                    while (counter < coordinatesArrayList.size()) {
                        Coordinates coordinate = coordinatesArrayList.get(counter);
                        if (Math.abs(coordinate.getX() - coord.getX()) <= differenceThreshold
                                || Math.abs(coordinate.getY() - coord.getY()) <= differenceThreshold) {
                            coord = new Coordinates();
                            counter = -1;
                        }
                        counter++;
                    }
                    coordinatesArrayList.add(coord);
                    gameUniverse.addSolarSystem(new SolarSystem(coord));
                }

                Object[] printMapArr = gameUniverse.getUniverse().values().toArray();

                Log.d("Edit", "Solar Systems:\nPlanet 1: " + printMapArr[0].toString()
                        + "\nPlanet 2: " + printMapArr[1].toString()
                        + "\nPlanet 3: " + printMapArr[2].toString()
                        + "\nPlanet 4: " + printMapArr[3].toString()
                        + "\nPlanet 5: " + printMapArr[4].toString()
                        + "\nPlanet 6: " + printMapArr[5].toString()
                        + "\nPlanet 7: " + printMapArr[6].toString()
                        + "\nPlanet 8: " + printMapArr[7].toString()
                        + "\nPlanet 9: " + printMapArr[8].toString()
                        + "\nPlanet 10: " + printMapArr[9].toString());
                Toast.makeText(this, "Universe and Player Created", Toast.LENGTH_LONG).show();
                player.setSolarSystem(gameUniverse.getUniverse().values().toArray(new SolarSystem[0])[0]);
                GameState.startGame(gameUniverse, player);

                Intent intent = new Intent(MainActivity.this, MarketActivity.class);
                this.startActivity(intent);
            } catch (IllegalArgumentException e) {
                Log.d("Error", e.getMessage());
                Toast.makeText(this, "Skill points must be positive and sum to 16!", Toast.LENGTH_LONG).show();
            }
        } catch (Throwable T) {
            Log.d("Error", T.getMessage(), T);
            Toast.makeText(this,"Enter all required fields", Toast.LENGTH_LONG).show();

        }


    }



}
