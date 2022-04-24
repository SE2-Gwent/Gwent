package at.aau.se2.gwent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainGame extends AppCompatActivity {

    //TODO: drag and drop der Handkarten ins eigene Spielfeld setzten
    //TODO: Karten gemäß ihrer Anzahl sortieren (nicht von links nach rechts sondern zentriert)
    //TODO: Auf der Seite Anzeige der Punkte, Möglichkeit Zug rückgängig zu machen/OK für den nächsten Zug/Sonderfunktion

    ArrayList<ImageView> cards;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainboard);
        ArrayList<Card> handcards = new ArrayList<>();
        addonclicklistenerfordetailedcardview(handcards);
        getSupportActionBar().hide();
        fillarray();
        ImageView teo = findViewById(R.id.card1);
        teo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Test hat funktioniert");
            }
        });


    }
    public void fillarray(){
        cards.add(findViewById(R.id.card1));
        cards.add(findViewById(R.id.card2));
        cards.add(findViewById(R.id.card3));
        cards.add(findViewById(R.id.card4));
        cards.add(findViewById(R.id.card5));
        cards.add(findViewById(R.id.card6));
        cards.add(findViewById(R.id.card7));
        cards.add(findViewById(R.id.card8));
        cards.add(findViewById(R.id.card9));
        cards.add(findViewById(R.id.card10));
    }

    public void addonclicklistenerfordetailedcardview(ArrayList<Card> handcards){
        int numberofhandcards=handcards.size();
        for(int i=0; i<numberofhandcards; i++){
            cards.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //detailed cardview
                }
            }
            );
        }


    }

}