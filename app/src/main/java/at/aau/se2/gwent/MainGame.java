package at.aau.se2.gwent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainGame extends AppCompatActivity {

    //TODO: karten von gegenüber verstecke nur anzahl soll ersichtlich sein
    //TODO: wenn done gedrückt wird wird die karte aus den Handkarten entfernt und ein platzhalter muss verschwinden

    //testdaten
    Card one = new Card(1, R.drawable.hearts2);
    Card two = new Card(2, R.drawable.hearts3);
    Card three = new Card( 3, R.drawable.hearts4);
    Card four = new Card(4, R.drawable.hearts5);
    Card five = new Card(5, R.drawable.hearts6);
    Card six = new Card(6, R.drawable.hearts7);
    Card seven = new Card(7, R.drawable.hearts7);
    ArrayList<Card> handcards = new ArrayList<Card>(8);
    Card current;

    ArrayList<ImageView> cards = new ArrayList<ImageView>(10);
    ArrayList<ImageView> placeholder = new ArrayList<ImageView>(18);
    ImageView currentcard;
    ImageView currentplaceholder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainboard);
        //testdaten
        handcards.add(one);
        handcards.add(four);
        handcards.add(six);
        handcards.add(three);
        handcards.add(one);
        handcards.add(two);
        handcards.add(four);
        handcards.add(seven);
        activatedonebutton();
        getSupportActionBar().hide();
        fillhandcardarray();
        showpicturesforhandcards(handcards);
        fillplaceholder();
        addonclicklistenerfordetailedcardview(handcards);

    }

    public void activatedonebutton(){

        View forbutton = findViewById(R.id.sidebar);
        Button test = forbutton.findViewById(R.id.donebutton);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("testitii");
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("testitii");
                for(int i=0; i<cards.size(); i++) {
                    if (cards.get(i).getId() == currentcard.getId()) {
                        System.out.println("test12222");
                        handcards.remove(handcards.get(i));
                        fillhandcardarray();
                        showpicturesforhandcards(handcards);
                    }
                }
            }
        });
    }


    public void fillhandcardarray(){
        View include = findViewById(R.id.handdeck);
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

        for(ImageView i:cards){
            i.setVisibility(View.INVISIBLE);
        }
    }
    public void showpicturesforhandcards(ArrayList<Card> handcards){
        for(int i = 0; i<handcards.size(); i++){
            cards.get(i).setImageResource(handcards.get(i).resource);
            cards.get(i).setTag(handcards.get(i).resource);
            cards.get(i).setVisibility(View.VISIBLE);
        }
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
                    if(currentcard!=null){
                        currentplaceholder.setImageResource(R.drawable.card_back);
                        currentcard.setVisibility(View.VISIBLE);
                    }
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
                    currentplaceholder.setImageResource((Integer) currentcard.getTag());
                    currentcard.setVisibility(View.INVISIBLE);



                }
            }
            );

        }

    }

}