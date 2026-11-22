import java.io.*;

public class FileIO {

    public static void saveGame(GameState state, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(state);
            System.out.println("Oyun kaydedildi.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState loadGame(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            System.out.println("Oyun yüklendi.");
            return (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

