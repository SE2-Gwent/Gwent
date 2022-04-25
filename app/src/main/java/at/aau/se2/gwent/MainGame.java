package at.aau.se2.gwent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainGame extends AppCompatActivity {

    //TODO: karten von gegenüber verstecke nur anzahl soll ersichtlich sein
    //TODO: wenn done gedrückt wird wird die karte aus den Handkarten entfernt und ein platzhalter muss verschwinden
    //TODO: die richtige Bildressource muss hineingeladen werden

    ArrayList<ImageView> cards = new ArrayList<ImageView>(10);
    ArrayList<ImageView> placeholder = new ArrayList<ImageView>(18);
    ImageView currentcard;
    ImageView currentplaceholder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainboard);
        ArrayList<Card> handcards = new ArrayList<>();

        getSupportActionBar().hide();
        fillarray();
        fillplaceholder();

        addonclicklistenerfordetailedcardview(handcards);

    }


    public void fillarray(){
        View include = findViewById(R.id.handdeck);
        include.findViewById(R.id.card1).setTag("Hallo");
        String test = (String) include.findViewById(R.id.card1).getTag();
        System.out.println(test);

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

        View firstrow = findViewById(R.id.firstrow);
        placeholder.add(firstrow.findViewById(R.id.rowcard1));
        placeholder.add(firstrow.findViewById(R.id.rowcard2));
        placeholder.add(firstrow.findViewById(R.id.rowcard3));
        placeholder.add(firstrow.findViewById(R.id.rowcard4));
        placeholder.add(firstrow.findViewById(R.id.rowcard5));
        placeholder.add(firstrow.findViewById(R.id.rowcard6));
        placeholder.add(firstrow.findViewById(R.id.rowcard7));
        placeholder.add(firstrow.findViewById(R.id.rowcard8));
        placeholder.add(firstrow.findViewById(R.id.rowcard9));
        View secondrow = findViewById(R.id.secondrow);
        placeholder.add(secondrow.findViewById(R.id.rowcard1));
        placeholder.add(secondrow.findViewById(R.id.rowcard2));
        placeholder.add(secondrow.findViewById(R.id.rowcard3));
        placeholder.add(secondrow.findViewById(R.id.rowcard4));
        placeholder.add(secondrow.findViewById(R.id.rowcard5));
        placeholder.add(secondrow.findViewById(R.id.rowcard6));
        placeholder.add(secondrow.findViewById(R.id.rowcard7));
        placeholder.add(secondrow.findViewById(R.id.rowcard8));
        placeholder.add(secondrow.findViewById(R.id.rowcard9));



    }

    public void addonclicklistenerfordetailedcardview(ArrayList<Card> handcards){
        int numberofhandcards=handcards.size();
        for(int i=0; i<10; i++){
            cards.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    currentcard = (ImageView) v;
                    activateplaceholders();
                    activateonclicklistenertoplaceholders();
                }
            }
            );
        }



    }

    public void activateplaceholders(){
        for(ImageView i:placeholder){
            i.setVisibility(View.VISIBLE);
        }
    }
    public void activateonclicklistenertoplaceholders(){
        for(ImageView i:placeholder){
            i.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentplaceholder!=null) {
                        currentplaceholder.setImageResource(R.drawable.card_back);
                    }

                    ImageView test = (ImageView) v ;
                    currentplaceholder = test;


                }
            }
            );

        }

    }

}