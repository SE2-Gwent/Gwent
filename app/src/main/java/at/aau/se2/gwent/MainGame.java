package at.aau.se2.gwent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainGame extends AppCompatActivity {

    //TODO: drag and drop der Handkarten ins eigene Spielfeld setzten
    //TODO: Pushup fendster wenn man Handkarte anklickt
    //TODO: Karten gemäß ihrer Anzahl sortieren (nicht von links nach rechts sondern zentriert)
    //TODO: Auf der Seite Anzeige der Punkte, Möglichkeit Zug rückgängig zu machen/OK für den nächsten Zug/Sonderfunktion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainboard);
        ArrayList<Card> handcards = new ArrayList<>();

        getSupportActionBar().hide();

        ImageView teo = findViewById(R.id.card1);
        teo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Test hat funktioniert");
            }
        });


    }
}