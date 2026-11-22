import java.awt.*;
import javax.swing.*;

public class SpitterZombie extends Zombie {

    public SpitterZombie(double x, double y) {
        super(x, y);
        this.speed = 1.0; // Yavaş hareket ediyor
        this.size = 64;
        this.health = 4; // Spitter zombisinin canı düşük
        this.damage = 10;  // Spitter'ın oyuncuya verdiği hasar
        this.color = Color.MAGENTA; // Renk seçimi
        this.image = new ImageIcon("images/zombie_spitter.png").getImage(); // Spitter görseli
    }

    @Override
    public void update(Player player) {
        // Zombi, oyuncuya doğru hareket eder
        double dx = player.getX() + player.getWidth() / 2 - x;
        double dy = player.getY() + player.getHeight() / 2 - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Zombi oyuncuya yaklaşırken yönünü ayarla
        x += (dx / distance) * speed;
        y += (dy / distance) * speed;

        // Zombi oyuncuya yaklaştığında çarpışma kontrolü yap
        checkCollisionWithPlayer(player);
    }

    @Override
    public boolean isDead() {
        return this.health <= 0; // Zombi öldüğünde true dönecek
    }

    // Zombi ile oyuncu arasındaki çarpışmayı kontrol et
    @Override
    public boolean checkCollisionWithPlayer(Player player) {
        double playerX = player.getX();
        double playerY = player.getY();
        double playerWidth = player.getWidth();
        double playerHeight = player.getHeight();

        // Zombi ve oyuncu arasındaki çarpışma kontrolü
        if (this.x < playerX + playerWidth && this.x + this.size > playerX &&
            this.y < playerY + playerHeight && this.y + this.size > playerY) {
            player.takeDamage(this.damage); // Spitter zombi oyuncuya 10 hasar veriyor
            return true;  // Çarpışma varsa true döndür
        }
        return false;  // Çarpışma yoksa false döndür
    }

    @Override
    public void draw(Graphics g) {
        if (image == null) image = new ImageIcon("images/zombie_spitter.png").getImage(); // Kayıttan yüklendiyse görseli tekrar yükle
        g.drawImage(image, (int) x, (int) y,  48, 48, null);
    }
}
