import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Administrator extends Application {

    // Class variables declaration
    private static final int currentDealCard = 0; // Used to denote the top card of the deck
    private DeckOfCards playerOneDeck; // Deck for Player One
    private DeckOfCards playerTwoDeck; // Deck for Player Two
    private Stage window; // Primary stage for the application
    private Scene startScene,sceneOne,sceneTwo,gameScene; // Different scenes for the game stages
    private TextArea game; // Text area to display game messages
    private String playerOneName,playerTwoName; // Names of the players


    // Enumeration to represent card faces with associated values
    public enum Faces {
        Deuce(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8),
        Nine(9), Ten(10), Jack(11), Queen(12), King(13), Ace(14);
        private final int value;

        Faces(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    // Start method to initialize the GUI
    public void start(Stage primaryStage) {
        window = primaryStage;

        // Initial scene layout
        VBox layoutOne = new VBox(20);
        Label startLabel = new Label("Welcome to WAR game");
        Button startButton = new Button("START");
        layoutOne.getChildren().addAll(startLabel,startButton);
        layoutOne.setAlignment(Pos.CENTER);
        startScene = new Scene(layoutOne,300,250);

        // Scene for player one to enter name
        VBox layoutTwo = new VBox(20);
        TextField playerFirstName = new TextField();
        Label labelOne = new Label("Player one, enter your name :");
        Button submitOneButton  = new Button("submit");
        layoutTwo.setAlignment(Pos.CENTER);
        layoutTwo.getChildren().addAll(labelOne,playerFirstName,submitOneButton);
        sceneOne = new Scene(layoutTwo, 300, 250);

        // Scene for player two to enter name
        VBox layoutThree = new VBox(20);
        TextField playerSecondName = new TextField();
        Label labelTwo = new Label("Player two, enter your name :");
        Label labelThree = new Label("Great, by clicking Let`s go, we will roll a game.");
        Button letGoButton  = new Button("Let`s GO");
        layoutThree.setAlignment(Pos.CENTER);
        layoutThree.getChildren().addAll(labelTwo,playerSecondName,labelThree,letGoButton);
        sceneTwo = new Scene(layoutThree, 300, 250);

        // Main game scene layout
        VBox layoutFour = new VBox(10);
        HBox layoutFive = new HBox(10);
        layoutFour.setAlignment(Pos.TOP_CENTER);
        layoutFive.setAlignment(Pos.CENTER);
        Button startNew  = new Button("End game");
        Button startSame  = new Button("Play again");
        game = new TextArea();
        game.setWrapText(true);
        layoutFive.getChildren().addAll(startNew,startSame);
        layoutFour.getChildren().addAll(game,layoutFive);
        game.prefHeightProperty().bind(layoutFour.heightProperty().multiply(0.8));
        gameScene = new Scene(layoutFour, 500, 450);



        // Event handlers for buttons to switch scenes and start game
        startButton.setOnAction(event -> {
            window.setScene(sceneOne);
        });
        submitOneButton.setOnAction(event -> {
            playerOneName = playerFirstName.getText();
            window.setScene(sceneTwo);
        });
        letGoButton.setOnAction(event -> {
            playerTwoName = playerSecondName.getText();
            window.setScene(gameScene);
            startGame();
        });
        startNew.setOnAction(event -> {
            playerFirstName.clear();
            playerSecondName.clear();
            game.clear();
            window.setScene(startScene);
        });
        startSame.setOnAction(event -> {
            game.clear();
            startGame();
            window.setScene(gameScene);
        });


        // Setting the primary stage
        primaryStage.setTitle("War game");
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    // Method to start the game, initialize decks and shuffle them
    public void startGame() {
        playerOneDeck = new DeckOfCards();
        playerTwoDeck = new DeckOfCards();
        playerOneDeck.shuffle();
        playerTwoDeck.shuffle();
        int faceOne, faceTwo, winner, thirdCount = 4, count = 0;
        while (playerOneDeck.getSize() > 0 && playerTwoDeck.getSize() > 0) {
            Card firstCardOne = playerOneDeck.getCard(currentDealCard);
            Card firstCardTwo = playerTwoDeck.getCard(currentDealCard);
            count++;
            game.appendText("\nRound " + count + " :\n" + playerOneName+" holds " + playerOneDeck.getSize() + " cards" + ", " + playerTwoName+" holds " + playerTwoDeck.getSize() + " cards.\n");

            game.appendText(playerOneName+" deals " + firstCardOne.toString() + " VS " + playerTwoName+" deals " + firstCardTwo.toString()+"\n");
            faceOne = intFaceValue(firstCardOne.getFace());
            faceTwo = intFaceValue(firstCardTwo.getFace());
            if (faceOne > faceTwo) {
                game.appendText(playerOneName+" wins the round\n");
                winner = 1;
                addingCardToTheWinner(winner);
            } else if (faceOne < faceTwo) {
                game.appendText(playerTwoName+" wins the round\n");
                winner = 2;
                addingCardToTheWinner(winner);
            } else {
                game.appendText("it`s a WAR\n");
                war(thirdCount);
                thirdCount = 3;
            }
        }
        if (playerOneDeck.getSize() > 0)
            game.appendText("\n"+playerOneName+" wins the game\n\nGAME OVER\n");
        else
            game.appendText("\n"+playerTwoName+" wins the game\n\nGAME OVER\n");

    }

    // War method to handle tie situations
    public void war(int thirdCount) {
        boolean checkOneSize = checkDeckSize(playerOneDeck, thirdCount);
        boolean checkTwoSize = checkDeckSize(playerTwoDeck, thirdCount);
        if (checkOneSize || checkTwoSize) {
            if (checkOneSize)
                game.appendText(playerOneName+" does not hold enough cards\n");
            else
                game.appendText(playerTwoName+" does not hold enough cards\n");
            removeAllCards();
            return;
        }
        Card thirdCardOne = playerOneDeck.getCard(thirdCount);
        Card thirdCardTwo = playerTwoDeck.getCard(thirdCount);
        int thirdFaceOne = intFaceValue(thirdCardOne.getFace());
        int thirdFaceTwo = intFaceValue(thirdCardTwo.getFace());
        int winner;
        if (thirdFaceOne > thirdFaceTwo) {
            winner = 1;
            addingThreeCardToTheWinner(winner, thirdCount);
        } else if (thirdFaceOne < thirdFaceTwo) {
            winner = 2;
            addingThreeCardToTheWinner(winner, thirdCount);
        } else {
            game.appendText("it`s a WAR\n");
            war(thirdCount + 3);
        }

    }

    // A method for passing the redundant cards of the player to another
    public void removeAllCards() {
        if (playerOneDeck.getSize() > playerTwoDeck.getSize()) {
            for (int idx = playerTwoDeck.getSize() - 1; idx >= 0; idx--) {
                playerOneDeck.addCard(playerTwoDeck.getCard(idx));
                playerTwoDeck.removeCard(idx);
            }
        } else {
            for (int idx = playerOneDeck.getSize() - 1; idx >= 0; idx--) {
                playerTwoDeck.addCard(playerOneDeck.getCard(idx));
                playerOneDeck.removeCard(idx);
            }
        }
    }

    // Method to check if a player's deck has enough cards for war
    public boolean checkDeckSize(DeckOfCards playerDeck, int thirdCount) {
        return playerDeck.getSize() < thirdCount + 1;
    }

    // Method to remove a card from the top of the deck
    public void removeFromTheTop(DeckOfCards player, int cardIndex) {
        player.removeCard(cardIndex);
    }

    // Method to add cards to the winner after a war round
    public void addingThreeCardToTheWinner(int winner, int thirdCount) {
        if (winner == 1) {
            game.appendText(playerOneName+" wins the round\n");
            for (int idx = thirdCount - 1; idx >= 0; idx--) {
                playerOneDeck.addCard(playerOneDeck.getCard(idx));
                removeFromTheTop(playerOneDeck, idx);
                playerOneDeck.addCard(playerTwoDeck.getCard(idx));
                removeFromTheTop(playerTwoDeck, idx);
            }
        } else {
            game.appendText(playerTwoName+" wins the round\n");
            for (int idx = thirdCount - 1; idx >= 0; idx--) {
                playerTwoDeck.addCard(playerTwoDeck.getCard(idx));
                removeFromTheTop(playerTwoDeck, idx);
                playerTwoDeck.addCard(playerOneDeck.getCard(idx));
                removeFromTheTop(playerOneDeck, idx);
            }
        }
    }

    // Method to add cards to the winner of a regular round
    public void addingCardToTheWinner(int winner) {
        if (winner == 1) {
            playerOneDeck.addCard(playerOneDeck.getCard(currentDealCard));
            removeFromTheTop(playerOneDeck, currentDealCard);
            playerOneDeck.addCard(playerTwoDeck.getCard(currentDealCard));
            removeFromTheTop(playerTwoDeck, currentDealCard);
        } else {
            playerTwoDeck.addCard(playerTwoDeck.getCard(currentDealCard));
            removeFromTheTop(playerTwoDeck, currentDealCard);
            playerTwoDeck.addCard(playerOneDeck.getCard(currentDealCard));
            removeFromTheTop(playerOneDeck, currentDealCard);
        }
    }

    // Method to convert face string to its corresponding value
    public int intFaceValue(String face) {
        try {
            Faces cardFace = Faces.valueOf(face);
            return cardFace.getValue();

        } catch (IllegalArgumentException e) {
            game.appendText(face + " is not valid card face.\n");
            return -1;
        }
    }
}
