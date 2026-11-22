import java.awt.*;
import java.io.Serializable;
import javax.swing.*;

public abstract class Zombie implements Serializable {
    protected double x, y;       // X ve Y koordinatları
    protected double speed;      // Zombi hızını belirle
    protected int size;          // Zombi boyutu
    protected int health;        // Zombi sağlığı
    protected int damage;        // Zombi'nin verdiği hasar
    protected Color color;       // Zombi'nin rengi
    protected transient Image image; // Zombi görseli

    // Zombi yaratılırken, konum belirlenir
    public Zombie(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Oyuncuya doğru hareket etmesi için update fonksiyonu
    public abstract void update(Player player);

    // Zombi'nin görselini çizme
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, (int) x, (int) y, size, size, null);
        } else {
            g.setColor(color);
            g.fillRect((int) x, (int) y, size, size); // Eğer görsel yoksa renkli bir kare çizer
        }
    }

    // Zombi'nin öldüğünü kontrol eder
    public boolean isDead() {
        return health <= 0;
    }

    // Zombi'nin aldığı hasar
    public void takeDamage(int dmg) {
        health -= dmg;  // Zombi hasar aldığında sağlığı azalır
    }

    // Mermi ile çarpışmayı kontrol et
    public boolean checkCollisionWithBullet(Bullet bullet) {
        // Merminin X, Y koordinatlarını alalım
        double bulletX = bullet.getX();
        double bulletY = bullet.getY();
        double bulletSize = bullet.getSize();

        // Merminin ve zombinin çarpışıp çarpmadığını kontrol edelim
        return (bulletX < this.x + this.size && bulletX + bulletSize > this.x &&
                bulletY < this.y + this.size && bulletY + bulletSize > this.y);
    }

    // Zombi'nin hızını belirler
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // Zombi'nin hasar gücünü belirler
    public void setDamage(int damage) {
        this.damage = damage;
    }

    // Zombi'nin sağlık değerini belirler
    public void setHealth(int health) {
        this.health = health;
    }

    // Zombi'nin X koordinatını döner
    public int getX() {
        return (int) x;
    }

    // Zombi'nin Y koordinatını döner
    public int getY() {
        return (int) y;
    }

    // Zombi'nin boyutunu döner
    public int getSize() {
        return size;
    }

    // Zombi'nin verdiği hasarı döner
    public int getDamage() {
        return damage;
    }

    // Zombi'nin hızını döner
    public double getSpeed() {
        return speed;
    }

    // Zombi'nin sağlığını döner
    public int getHealth() {
        return health;
    } 
    public boolean checkCollisionWithPlayer(Player player) {
        double playerX = player.getX();
        double playerY = player.getY();
        double playerWidth = player.getWidth();
        double playerHeight = player.getHeight();

        // Zombi ve oyuncu arasındaki çarpışma kontrolü
        if (this.x < playerX + playerWidth && this.x + this.size > playerX &&
            this.y < playerY + playerHeight && this.y + this.size > playerY) {
            player.takeDamage(1); // Zombi oyuncuya 1 hasar verir
            return true;
        }
        return false;
    }

}
