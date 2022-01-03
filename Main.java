package encryptdecrypt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

abstract class ChoiceAlgorithm {
    String mode;
    int key;
    String data;
    String in;
    String out;

    ChoiceAlgorithm(String mode, int key, String data, String in, String out) {
        this.mode = mode;
        this.key = key;
        this.data = data;
        this.in = in;
        this.out = out;
    }

    public void encOrDec(String mode) throws IOException {
        switch (mode) {

            case "dec":
                this.decrypt(this.key, this.data, this.in, this.out);
                break;
            case "enc":
            default:
                this.encrypt(this.key, this.data, this.in, this.out);
                break;
        }
    }

    public void encrypt(int key, String data, String in, String out) throws IOException {
        String s = "";
        char[] arr;
        if (!(data.isEmpty())) {
            arr = data.toCharArray();
        } else if (new File(in).exists()) {
            arr = ChoiceAlgorithm.readFileAsString(in).toCharArray();

        } else {
            arr = s.toCharArray();
        }
        System.out.println(String.valueOf(arr));
        arr = encryptHelper(arr, key);
        String encrypted = String.valueOf(arr);
        System.out.println(encrypted);
        if (!("".equals(out))) {
            File writeFile = new File(out);
            try (FileWriter writer = new FileWriter(writeFile)) {
                writer.write(encrypted);
            } catch (IOException e) {
                System.out.printf("Error occurred, %s", e.getMessage());
            }
        } else {
            System.out.println(encrypted);
        }
    }

    public void decrypt(int key, String data, String in, String out) throws IOException {
        String s = "";
        char[] arr;
        if (!(data.isEmpty())) {
            arr = data.toCharArray();
        } else if (new File(in).exists()) {
            arr = ChoiceAlgorithm.readFileAsString(in).toCharArray();
        } else {
            arr = s.toCharArray();
        }
        arr = decryptHelper(arr, key);
        String decrypted = String.valueOf(arr);
        if (!("".equals(out))) {
            File writeFile = new File(out);
            try (FileWriter writer = new FileWriter(writeFile)) {
                writer.write(decrypted);
            } catch (IOException e) {
                System.out.printf("Error occurred, %s", e.getMessage());
            }
        } else {
            System.out.println(decrypted);
        }
    }

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public abstract char[] encryptHelper(char[] arr, int key);
    public abstract char[] decryptHelper(char[] arr, int key);

}

class Shift extends ChoiceAlgorithm {

    Shift(String mode, int key, String data, String in, String out) {
        super(mode, key, data, in, out);
    }

    @Override
    public char[] encryptHelper(char[] arr, int shift) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 32) {
                continue;
            }
            if ((arr[i] >= 97 && arr[i] <= 122 - shift) ||
                (arr[i] >= 65 && arr[i] <= 90 - shift)) {
                arr[i] += shift;
            } else {
                arr[i] += shift - 26;
            }
        }
        return arr;
    }

    @Override
    public char[] decryptHelper(char[] arr, int shift) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 32) {
                continue;
            }
            if ((arr[i] >= 97 + shift && arr[i] <= 122) ||
                (arr[i] >= 65 + shift && arr[i] <= 90)) {
                arr[i] -= shift;
            } else {
                arr[i] -= shift - 26;
            }
        }
        return arr;
    }
}

class Unicode extends ChoiceAlgorithm {

    Unicode(String mode, int key, String data, String in, String out) {
        super(mode, key, data, in, out);
    }

    @Override
    public char[] encryptHelper(char[] arr, int key) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] += key;
        }
        return arr;
    }

    @Override
    public char[] decryptHelper(char[] arr, int key) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= key;
        }
        return arr;
    }
}
public class Main {
    public static void main(String[] args) throws IOException {
        List<String> argList = Arrays.asList(args);
        String mode = "";
        String keyStr = "";
        String data = "";
        String in = "";
        String out = "";
        String alg = "";

        for (int i = 0; i < argList.size(); i++) {
            if (argList.get(i).contains("-mode")) {
                mode = argList.get(i + 1);
            }
            if (argList.get(i).contains("-key")) {
                keyStr = argList.get(i + 1);
            }
            if (argList.get(i).contains("-data")) {
                data = argList.get(i + 1);
            }
            if (argList.get(i).contains("-in")) {
                in = argList.get(i + 1);
            }
            if (argList.get(i).contains("-out")) {
                out = argList.get(i + 1);
            }
            if (argList.get(i).contains("-alg")) {
                alg = argList.get(i + 1);
            }
        }
        int key = Integer.parseInt(keyStr);
        switch (alg) {
            case "unicode":
                System.out.println(alg);
                ChoiceAlgorithm uniAlgo = new Unicode(mode, key, data, in, out);
                uniAlgo.encOrDec(uniAlgo.mode);
                break;
            case "shift":
            default:
                ChoiceAlgorithm shiftAlg = new Shift(mode, key, data, in, out);
                shiftAlg.encOrDec(shiftAlg.mode);
                break;
        }
    }
}
