import java.util.ArrayList;

public class Rifle extends Weapon {

    public Rifle() {
        super(30, 90, 600, 5); // 30 mermi, 90 yedek, 600 rpm, 5 hasar
    }

    @Override
    public ArrayList<Bullet> fire(int startX, int startY, int targetX, int targetY) {
        ArrayList<Bullet> bullets = new ArrayList<>();

        double angle = Math.atan2(targetY - startY, targetX - startX); // Hedefe yönelme açısını hesapla
        double spread = Math.toRadians(30); // ±15 derece yay açısı

        double randomAngle = angle + (Math.random() - 0.5) * spread; // Yay açısına göre rastgele bir açı oluştur

        int tx = (int) (startX + Math.cos(randomAngle) * 100);  // Merminin hedef X konumu
        int ty = (int) (startY + Math.sin(randomAngle) * 100);  // Merminin hedef Y konumu

        Bullet b = new Bullet(startX, startY, tx, ty, getDamage()); // Her mermiye rifle'ın hasarını geçir
        bullets.add(b);

        return bullets;
    }
}
