import java.util.ArrayList;

public class RocketLauncher extends Weapon {

    public RocketLauncher() {
        super(1, 5, 10, 50); // 1 şarjör, 5 toplam mermi, 10 rpm, 50 hasar
    }

    @Override
    public ArrayList<Bullet> fire(int startX, int startY, int targetX, int targetY) {
        ArrayList<Bullet> bullets = new ArrayList<>();
        Bullet b = new Bullet(startX, startY, targetX, targetY, getDamage()); // RocketLauncher mermisinin hasarını geçir
        b.setExplosive(true); // Patlayıcı olarak işaretle
        bullets.add(b);
        return bullets;
    }
}
