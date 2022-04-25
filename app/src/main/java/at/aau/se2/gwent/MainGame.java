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

    ArrayList<ImageView> cards = new ArrayList<ImageView>(10);
    ArrayList<ImageView> placeholder = new ArrayList<ImageView>(10);
    ImageView currentcard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainboard);
        ArrayList<Card> handcards = new ArrayList<>();
        addonclicklistenerfordetailedcardview(handcards);
        getSupportActionBar().hide();
        fillarray();
        fillplaceholder();



    }


    public void fillarray(){
        View include = findViewById(R.id.firstrow);
        cards.add(include.findViewById(R.id.card1));
        cards.add(include.findViewById(R.id.card2));
        cards.add(include.findViewById(R.id.card3));
        cards.add(include.findViewById(R.id.card4));
        cards.add(include.findViewById(R.id.card5));
        cards.add(include.findViewById(R.id.card6));
        cards.add(include.findViewById(R.id.card7));
        cards.add(include.findViewById(R.id.card8));
        cards.add(include.findViewById(R.id.card9));
        cards.add(include.findViewById(R.id.card10));
    }
    public void fillplaceholder(){
        View include = findViewById(R.id.firstrow);
        cards.add(include.findViewById(R.id.rowcard1));
        cards.add(include.findViewById(R.id.rowcard2));
        cards.add(include.findViewById(R.id.rowcard3));
        cards.add(include.findViewById(R.id.rowcard4));
        cards.add(include.findViewById(R.id.rowcard5));
        cards.add(include.findViewById(R.id.rowcard6));
        cards.add(include.findViewById(R.id.rowcard7));
        cards.add(include.findViewById(R.id.rowcard8));
        cards.add(include.findViewById(R.id.rowcard9));

    }

    public void addonclicklistenerfordetailedcardview(ArrayList<Card> handcards){
        int numberofhandcards=handcards.size();
        for(int i=0; i<numberofhandcards; i++){
            cards.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activateonclicklistenertootherelements();
                    currentcard = (ImageView) v;
                    System.out.println("Juhu");
                    activateonclicklistenertootherelements();
                }
            }
            );
        }



    }
    public void activateonclicklistenertootherelements(){
        for(ImageView i:placeholder){
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("karte ");
                }
            }
            );

        }

    }

}