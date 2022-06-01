package at.aau.se2.gwent;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.appcompat.app.AppCompatActivity;
import at.aau.se2.gamelogic.models.Card;
import at.aau.se2.gwent.views.common.CardView;

public class MainGameActivity extends AppCompatActivity {

  // ToDo Karten die angecklickt wurden markieren
  // PopUp Fenster bei Heroaktivity spotten oder bei sofortigem passen

  // Handkarte, Karten, Platzhalter erstellt

  ArrayList<Card> handcards = new ArrayList<Card>(8);
  ArrayList<CardView> cards = new ArrayList<CardView>(10);
  ArrayList<CardView> placeholder = new ArrayList<CardView>(18);
  // Zwischenspeicher für Kartenauswahl auf dem Feld
  CardView currentcard;
  CardView currentplaceholder;

  // Wird aus MainActivity abgeleitet
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    setContentView(R.layout.activity_main_game);
    // erzeugt Testkarten
    generateTestEntities();
    // Balken - mit Appnamen auf hidegesetzt
    getSupportActionBar().hide();
    // Einzelne Imageviews holen
    fillHandCardArray();
    // Nach dem holen der Imageview die Container mit Bildern befüllen (Handkarten)
    showPicturesForHandCards(handcards);
    // Hole mir die Imageviews für die Placeholder
    fillPlaceholderImageviewArray();
    // OnClickListener Aktivieren - Handkarten wird in Zwischenspeicher gespeichert um sie dann
    // durch einen Platzhalter auszutauschen
    addOnClickListenerForHandCards(handcards);
    // Done Buttom - Damit der Zug beendet wird.
    activateDoneButton();

    for (CardView card : placeholder) {
      card.getCardImageView().setVisibility(View.INVISIBLE);
      card.getCardImageView().setImageResource(R.drawable.card_background);
    }
  }

  // erzeugt die testkarten muss im weiteren verlauf ausgetauscht werden
  public void generateTestEntities() {
    Card one = new Card(1);
    Card two = new Card(2);
    Card three = new Card(3);
    Card four = new Card(4);
    Card six = new Card(6);
    Card seven = new Card(7);

    handcards.add(one);
    handcards.add(four);
    handcards.add(six);
    handcards.add(three);
    handcards.add(one);
    handcards.add(two);
    handcards.add(four);
    handcards.add(seven);
  }

  public void activateDoneButton() {
    View testview = findViewById(R.id.cardRowsLayout);

    // Aus View holen
    Button test = findViewById(R.id.roundDoneButton);
    // Sichtbargemacht weil durchsichtig
    test.setVisibility(View.VISIBLE);
    test.setOnClickListener(
        new View.OnClickListener() {
          // Wenn man draufklickt - ist eine Karte ausgewählt?
          @Override
          public void onClick(View v) {
            showPopupWindow(testview);
            // Wenn man keine Karte ausspielen möchte
            if (currentcard == null) {
              // deleteonclicklistenerofallelements();
              v.setVisibility(View.INVISIBLE);
            } else if (currentcard != null) {
              // Placeholder ist belegt und kann damit kein weiteres Mal belegt werden.
              // setTag = Notiz
              currentplaceholder.setTag("full");
              // Aktualisert Handkarten, indem die Karten alle Optisch aufgerückt werden
              // Ebenso werden die Zwischenspeicher neu geordnet und alle wird gepasst

            }
          }
        });
  }

  public void showPopupWindow(View view) {

    // Layout vom Popupwindow wird freigegeben (Platz)
    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    View popupView = inflater.inflate(R.layout.popup_maingame, null);
    View testbutton = findViewById(R.id.roundDoneButton);

    // Erstellt das Popupfenster
    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
    // lässt Tabs außerhalb des Popupwindows schließen
    boolean focusable = true;

    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

    // Zeigt das Popupwindow
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    popupView
        .findViewById(R.id.yesbutton)
        .setOnClickListener(
            new View.OnClickListener() {
              // Wenn man draufklickt - ist eine Karte ausgewählt?
              @Override
              public void onClick(View v) {
                System.out.println("Yes button wurde gedruckt");
                deleteOnClickListenerOfAllElements();
                popupWindow.dismiss();

                if (currentcard != null) {
                  currentplaceholder.setTag("full");
                  for (int i = 0; i < handcards.size(); i++) {
                    if (cards.get(i).getId() == currentcard.getId()) {
                      handcards.remove(handcards.get(i));
                      fillHandCardArray();
                      showPicturesForHandCards(handcards);
                      deleteOnClickListenerOfAllElements();
                      currentplaceholder = null;
                      currentcard = null;
                      v.setVisibility(View.INVISIBLE);
                      testbutton.setVisibility(View.INVISIBLE);
                      break;
                    }
                  }
                }
                /*deleteonclicklistenerofallelements();
                currentplaceholder = null;
                currentcard = null;
                v.setVisibility(View.INVISIBLE);

                 */
              }
            });
    popupView
        .findViewById(R.id.nobutton)
        .setOnClickListener(
            new View.OnClickListener() {
              // Wenn man draufklickt - ist eine Karte ausgewählt?
              @Override
              public void onClick(View v) {
                System.out.println("No button wurde gedruckt");
                popupWindow.dismiss();
                findViewById(R.id.roundDoneButton).setVisibility(View.VISIBLE);
              }
            });

    // Wenn das Popupwindow gedürckt wird verschwindet es
    popupView.setOnTouchListener(
        new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            popupWindow.dismiss();
            return true;
          }
        });
  }

  // Handkarten anzeigen
  public void showPicturesForHandCards(ArrayList<Card> handcards) {
    for (int i = 0; i < handcards.size(); i++) {
      cards.get(i).setVisibility(View.VISIBLE);
    }
  }

  // Jede einzelne Handkarte bekommt einen OnClickListener
  public void addOnClickListenerForHandCards(ArrayList<Card> handcards) {
    for (int i = 0; i < 10; i++) {
      cards
          .get(i)
          .setOnClickListener(
              new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  currentcard = (CardView) v;
                  // Platzhalter die Frei sind Sichtbarmachen und OnClick freigeben
                  activatePlaceholders();

                  activateOnClickListenerToPlaceholders();
                }
              });
    }
  }

  // macht die freien kartenplätze ersichtlich
  public void activatePlaceholders() {
    // Placeholder werden sichtbar
    for (CardView i : placeholder) {
      i.getCardImageView().setVisibility(View.VISIBLE);
    }
  }

  public void activateOnClickListenerToPlaceholders() {
    for (CardView i : placeholder) {
      i.setOnClickListener(
          new View.OnClickListener() {

            // Bei Kartenwechsel unter den Placeholder wird die gewechselte Karte wieder mit einem
            // Placeholder befüllt
            @Override
            public void onClick(View v) {
              // Beim Ersten Ausspielen wird der Placeholder mit der Handkarte ausgetauscht.
              CardView test = (CardView) v;
              currentplaceholder = test;
              currentplaceholder.setAlpha(1.0F);
              currentplaceholder
                  .getCardImageView()
                  .setImageResource(R.drawable.an_craite_amorsmith);
              currentplaceholder.setTag("full");

              // ausgespielte Handkarte unsichtbar
              currentcard.setVisibility(View.GONE);
              deleteOnClickListenerOfAllElements();
            }
          });
    }
  }

  // Done buttom entfernt alle OnClicklistener
  public void deleteOnClickListenerOfAllElements() {
    for (CardView placeholder : placeholder) {
      placeholder.setOnClickListener(null);
      if (placeholder.getTag() != "full") {
        placeholder.getCardImageView().setVisibility(View.INVISIBLE);
      }
    }
  }

  public void fillPlaceholderImageviewArray() {
    View firstrow = findViewById(R.id.playersRangeRowLayout);
    placeholder.add(firstrow.findViewById(R.id.rowcard1));
    placeholder.add(firstrow.findViewById(R.id.rowcard2));
    placeholder.add(firstrow.findViewById(R.id.rowcard3));
    placeholder.add(firstrow.findViewById(R.id.rowcard4));
    placeholder.add(firstrow.findViewById(R.id.rowcard5));
    placeholder.add(firstrow.findViewById(R.id.rowcard6));
    placeholder.add(firstrow.findViewById(R.id.rowcard7));
    placeholder.add(firstrow.findViewById(R.id.rowcard8));
    placeholder.add(firstrow.findViewById(R.id.rowcard9));
    View secondrow = findViewById(R.id.playersMeleeRowLayout);
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

  public void fillHandCardArray() {
    View include = findViewById(R.id.playersHandLayout);
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
}
