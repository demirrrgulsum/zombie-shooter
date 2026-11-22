import java.awt.*;
import javax.swing.*;

public class TankZombie extends Zombie {

    public TankZombie(double x, double y) {
        super(x, y);
        this.speed = 0.8;  // TankZombie yavaş hareket eder
        this.size = 64;
        this.health = 30;  // TankZombie daha dayanıklı
        this.damage = 15; // TankZombie'nin verdiği hasar
        this.color = new Color(100, 50, 20); // Tank zombi için kahverengi rengi
        this.image = new ImageIcon("images/zombie_tank.png").getImage(); // Tank zombi görseli
    }

    @Override
    public void update(Player player) {
        // Zombi oyuncuya doğru hareket eder
        double dx = player.getX() + player.getWidth() / 2 - x;
        double dy = player.getY() + player.getHeight() / 2 - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Zombi oyuncuya yaklaşırken yönünü ayarla
        x += (dx / distance) * speed;
        y += (dy / distance) * speed;

        // Zombi ile oyuncu arasındaki çarpışmayı kontrol et
        checkCollisionWithPlayer(player);
    }

    @Override
    public boolean isDead() {
        return this.health <= 0; // Tank zombi öldüğünde true dönecek
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
            player.takeDamage(this.damage); // Tank zombi oyuncuya 15 hasar veriyor
            return true;  // Çarpışma varsa true döndür
        }
        return false;  // Çarpışma yoksa false döndür
    }

    @Override
    public void draw(Graphics g) {
        if (image == null) image = new ImageIcon("images/zombie_tank.png").getImage(); // Kayıttan yüklendiyse görseli tekrar yükle
        g.drawImage(image, (int) x, (int) y, 48, 48, null);
    }
}
