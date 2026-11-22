import java.util.ArrayList;

public class Shotgun extends Weapon {

    public Shotgun() {
        // Shotgun özellikleri: clipSize, totalAmmo, fireRate, damage
        super(5, 20, 60, 10); // Shotgun 10 hasar verir
    }

    @Override
    public ArrayList<Bullet> fire(int startX, int startY, int targetX, int targetY) {
        ArrayList<Bullet> bullets = new ArrayList<>();

        double angle = Math.atan2(targetY - startY, targetX - startX);  // Hedefe yönelme açısını hesapla
        double spread = Math.toRadians(45); // Toplam yay açısı, mermiler arasında 45 derecelik bir yay olacak

        int count = 9; // Shotgun her atışta 9 mermi atacak
        double step = spread / (count - 1); // Yayın her mermiye olan dağılımı

        for (int i = 0; i < count; i++) {
            double a = angle - spread / 2 + step * i; // Her mermi için açıyı hesapla
            int tx = (int) (startX + Math.cos(a) * 100);  // Merminin hedef X konumu
            int ty = (int) (startY + Math.sin(a) * 100);  // Merminin hedef Y konumu
            Bullet b = new Bullet(startX, startY, tx, ty, getDamage());  // Her mermiyi, shotgun'ın hasar değeri ile oluştur
            bullets.add(b);
        }

        return bullets;
    }
}
