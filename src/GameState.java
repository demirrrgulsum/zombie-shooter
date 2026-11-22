import java.io.Serializable;
import java.util.ArrayList;

public class GameState implements Serializable {
    public int wave;
    public int score;
    public Player player;
    public ArrayList<Zombie> zombies;
    public ArrayList<Bullet> bullets;

    public GameState(int wave, int score, Player player, ArrayList<Zombie> zombies, ArrayList<Bullet> bullets) {
        this.wave = wave;
        this.score = score;
        this.player = player;
        this.zombies = zombies;
        this.bullets = bullets;
    }
}