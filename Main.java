import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public String[] mask;
    public String word;
    public String[] words = new String[0];
    public final String PATHTOFILE = "src/main/java/org/example/words.txt";
    public Set<String> usedLetters = new HashSet<>();
    public Set<String> wordUniqueLetters = new HashSet<>();
    public int numberOfGuessedLetters = 0;
    public Scanner scanner = new Scanner(System.in);
    public int numberWrongTriesUser = 6; //Check inside game loop(userLose or not)
    public static void main(String[] args) {
        Main main = new Main();
        main.startGame();
    }
    public void clearAll() {
        numberWrongTriesUser = 6;
        numberOfGuessedLetters = 0;
        wordUniqueLetters.clear();
        usedLetters.clear();
        populateWordsArrayFromFile();
        getRandomSelectedWord();
        setWordMask(getRandomSelectedWord());
    }
    public void startGame() {
        System.out.println("[N]ew Game or [E]xit");
        String scan = scanner.nextLine();
        if (scan.equalsIgnoreCase("N")) {
        clearAll();
        // clear methods etc...
            System.out.println("Word is made up!");
            printMask();
        do {
            if(!userWon()) {
                inputLetterAndCheckIt();
                System.out.println("You already used symbols: " + usedLetters);
            } else if (userWon()) {
                System.out.println("Congratulations! You won!");
                System.out.println("Word is:" + word);
                startGame();
            }
        } while (true);
    } else if(scan.equalsIgnoreCase("E")) {
            System.exit();
        } else {
            System.out.println("Please enter a valid input!");
            startGame();
        }
    }

    public boolean userWon() {
        return numberOfGuessedLetters == wordUniqueLetters.size();
    }

    public void populateWordsArrayFromFile() {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(PATHTOFILE))) {
            br.lines().forEach(sb::append);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("Start IO error");
            throw new RuntimeException();
        }
        String wordsSplitByCommAndSpace = sb.toString();
        words = wordsSplitByCommAndSpace.split(", ");
    }

    public String getRandomSelectedWord() {
        Random random = new Random();
        return words[random.nextInt(words.length)];
    }

    public void setWordMask(String word) {
        this.word = word;
        this.mask = new String[word.length()];
        Arrays.fill(mask, "*");
        Collections.addAll(wordUniqueLetters, word.split(""));
    }

    public void printMask() {
        System.out.println(String.join("", mask));
    }
    
    public void updateMask(String letter) {
        for(int i = 0; i < word.length(); i++) {
            if(Character.toString(word.charAt(i)).equalsIgnoreCase(letter)) {
                mask[i] = letter;
            }
        }
    }

    public void drawHangMan(int val) {
        switch (val) {
            case 6 -> {
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
            } case 5 -> {
                System.out.println("______");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
            } case 4 -> {
                System.out.println("______");
                System.out.println("|    |");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
            } case 3 -> {
                System.out.println("______");
                System.out.println("|    o");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
            } case 2 -> {
                System.out.println("______");
                System.out.println("|    o");
                System.out.println("|   /|\\");
                System.out.println("|");
                System.out.println("|");
                System.out.println("|");
            } case 1 -> {
                System.out.println("______");
                System.out.println("|    o");
                System.out.println("|   /|\\");
                System.out.println("|   /-\\");
                System.out.println("|");
                System.out.println("|");
                System.out.println("You lose! Try next!");
                System.out.println("Word is " + word);
                startGame();
            }
        }
    }
    /*  ______1
       0|    |2
        |    o 3
        |   /|\ 4
        |   /-\ 5
        |
       */

    public void inputLetterAndCheckIt() {
        System.out.println("Input your Letter!");
        String scannerLetter = scanner.nextLine();
        //Проверки: 1. Использовалась ли эта буква ранее? 2. Содержится ли эта буква в слове?
        //1. Если использовалась: предлагать ввод пока не введет ту букву которая не использовалась.
        //2. Если содержится: numberOfGuessedLetters++; количество попыток не уменьшается,updateMask(scannerLetter), printMask();
        //2. Если не содержится: Отрисовывать висилицу, КоличествоОшибок++, Предложить еще один ввод,Вывести маску слова.
//        boolean containsLetters = scannerLetter.matches("^[a-zA-Z]+$");
            //TO DO: make check: if scannerLetter != a-zA-Z or scannerLetter.length() > 1 try next input etc....
            if (usedLetters.contains(scannerLetter)) {
                System.out.println("This Letter is already used!");
                inputLetterAndCheckIt();
            } else if (wordUniqueLetters.contains(scannerLetter)) {
                System.out.println("You guessed!");
                usedLetters.add(scannerLetter);
                numberOfGuessedLetters++;
                updateMask(scannerLetter);
                printMask();
            } else {
                System.out.println("You didn't guess it!");
                printMask();
                numberWrongTriesUser--;
                drawHangMan(numberWrongTriesUser);
                usedLetters.add(scannerLetter);
            }
    }
}
