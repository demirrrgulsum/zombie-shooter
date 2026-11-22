import java.awt.*;
import javax.swing.*;

public class NormalZombie extends Zombie {

    public NormalZombie(double x, double y) {
        super(x, y);
        this.speed = 1.0;
        this.size = 64;
        this.health = 3;
        this.color = Color.GREEN;
        this.image = new ImageIcon("images/zombie_normal.png").getImage();  // Normal zombi görseli 
    }

    @Override
    public void update(Player player) {
        double dx = player.getX() + player.getWidth() / 2 - x;
        double dy = player.getY() + player.getHeight() / 2 - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        x += (dx / distance) * speed;
        y += (dy / distance) * speed;

        // Zombi ile oyuncu arasındaki çarpışmayı kontrol et
        checkCollisionWithPlayer(player);
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
            player.takeDamage(1); // Normal zombi oyuncuya 1 hasar verir
            return true;  // Çarpışma varsa true döndür
        }
        return false;  // Çarpışma yoksa false döndür
    }

    @Override
    public boolean isDead() {
        return this.health <= 0; // Normal zombi öldüğünde true dönecek
    }

    @Override
    public void draw(Graphics g) {
        if (image == null) image = new ImageIcon("images/zombie_normal.png").getImage(); // Kayıttan yüklendiyse görseli tekrar yükle
        g.drawImage(image, (int) x, (int) y,  48, 48, null);
    }
}
