package mu.inovus.entity;

import java.util.*;

/**
 * Created by maratgatin on 13/03/2019.
 */
public final class RegistrationNumber {

    private final int numberPart; //final fields for thread safety, jmm freeze
    private final int lettersPart;

    private static final int LETTERS_COUNT = 3;
    private static final int NUMBERS_POS = 1;

    private static final String SUFFIX = " 116 RUS";

    public static final int MAX_NUMBER_BOUND = 1000;
    public static final int MAX_LETTERS_BOUND = (int) (Math.pow(Letter.values().length, LETTERS_COUNT) + 1);



    public RegistrationNumber(int numberPart, int lettersPart) {
        if (numberPart >= MAX_NUMBER_BOUND || lettersPart >= MAX_LETTERS_BOUND) {
            throw new IllegalArgumentException("Check bounds");
        }
        this.numberPart = numberPart;
        this.lettersPart = lettersPart;
    }


    public static RegistrationNumber generateNumber() {
        //it's potential bottleneck for multithreading, synchronized calls
        int number = RandomNumberGeneratorHolder.randomNumberGenerator.nextInt(MAX_NUMBER_BOUND);
        int letter = RandomNumberGeneratorHolder.randomNumberGenerator.nextInt(MAX_LETTERS_BOUND);
        return new RegistrationNumber(number, letter);
    }

    public static RegistrationNumber getNext(RegistrationNumber r) {
        int nums;
        int letters;
        if (r.numberPart + 1 < MAX_NUMBER_BOUND) {
            nums = r.numberPart + 1;
            letters = r.lettersPart;
        } else {
            nums = 0;
            letters = (r.lettersPart + 1) % MAX_LETTERS_BOUND;
        }
        return new RegistrationNumber(nums, letters);
    }

    private enum Letter {
        А, Е, Т, О, Р, Н, У, К, Х, С, В, М;

        private static final List<Letter> letterMapping;

        static {
            Comparator<Letter> comp = Comparator.comparing(Letter::toString);
            List<Letter> letters = Arrays.asList(Letter.values());
            letters.sort(comp);
            letterMapping = letters;
        }

        public static Letter getLetterByIndex(int index) {
            return letterMapping.get(index);
        }
    }

    private static final class RandomNumberGeneratorHolder {
        static final Random randomNumberGenerator = new Random();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();

        int var1 = lettersPart;
        for (int i = 0; i < LETTERS_COUNT; i++) {
            stringBuilder.append(Letter.getLetterByIndex(var1 % Letter.values().length));
            var1 /= Letter.values().length;
        }
        stringBuilder.reverse();
        stringBuilder.insert(NUMBERS_POS, String.format("%03d", numberPart));
        stringBuilder.append(SUFFIX);

        return stringBuilder.toString();
    }

    public int getNumberPart() {
        return numberPart;
    }

    public int getLettersPart() {
        return lettersPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistrationNumber that = (RegistrationNumber) o;

        if (numberPart != that.numberPart) return false;
        return lettersPart == that.lettersPart;
    }

    @Override
    public int hashCode() {
        int result = numberPart;
        result = 31 * result + lettersPart;
        return result;
    }
}
