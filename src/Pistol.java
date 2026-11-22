import java.util.ArrayList;

public class Pistol extends Weapon {

    public Pistol() {
        super(12, Integer.MAX_VALUE, 120, 3); // 12 şarjör kapasitesi, sınırsız toplam mermi, 120 rpm, 3 hasar
    }

    @Override
    public ArrayList<Bullet> fire(int startX, int startY, int targetX, int targetY) {
        ArrayList<Bullet> bullets = new ArrayList<>();
        Bullet b = new Bullet(startX, startY, targetX, targetY, getDamage());  // Mermi oluşturulurken hasarı geç
        bullets.add(b);
        return bullets;
    }
}
