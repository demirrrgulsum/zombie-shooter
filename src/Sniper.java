import java.util.ArrayList;

public class Sniper extends Weapon {

    // Sniper silahının yapıcı metodu, hasar değeri burada belirleniyor
    public Sniper() {
        // Silahın özellikleri: clipSize, totalAmmo, fireRate, damage
        super(5, 15, 30, 50); // Sniper 50 hasar verir
    }

    @Override
    public ArrayList<Bullet> fire(int startX, int startY, int targetX, int targetY) {
        ArrayList<Bullet> bullets = new ArrayList<>();
        
        // Sniper mermisi, yüksek hasar ile yaratılıyor
        Bullet b = new Bullet(startX, startY, targetX, targetY, getDamage());  // Damage parametresini ekliyoruz
        b.setPiercing(true); // Sniper mermisi zombileri delip geçsin
        bullets.add(b);
        
        return bullets;
    }
}
