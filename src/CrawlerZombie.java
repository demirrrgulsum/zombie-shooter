import java.awt.*;
import javax.swing.*;

public class CrawlerZombie extends Zombie {
    private int jumpCooldown = 0; // Zıplama soğuma süresi

    public CrawlerZombie(double x, double y) {
        super(x, y);
        this.speed = 2.5; // CrawlerZombie'nin hareket hızı
        this.size = 64;
        this.health = 2; // CrawlerZombie'nin sağlığı
        this.color = Color.ORANGE; // CrawlerZombie için turuncu renk
        this.image = new ImageIcon("images/zombie_crawler.png").getImage(); // Görsel
    }

    @Override
    public void update(Player player) {
        if (jumpCooldown > 0) jumpCooldown--; // Eğer zıplama soğuma süresi varsa bir azalt

        double dx = player.getX() + player.getWidth() / 2 - x; // Oyuncunun X konumu
        double dy = player.getY() + player.getHeight() / 2 - y; // Oyuncunun Y konumu
        double distance = Math.sqrt(dx * dx + dy * dy); // İki nokta arasındaki mesafe

        if (distance < 150 && jumpCooldown == 0) { // Eğer oyuncuya yakınsa ve zıplama süresi dolmuşsa
            // Zıplama hareketi
            x += dx / distance * 40; // Yüksek hızda oyuncuya yönel
            y += dy / distance * 40;
            jumpCooldown = 60; // Zıplama sonrası soğuma süresi
        } else {
            // Yavaş hareket et
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }

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
            player.takeDamage(5); // CrawlerZombie oyuncuya 5 hasar verir
            return true;  // Çarpışma varsa true döndür
        }
        return false;  // Çarpışma yoksa false döndür
    }

    @Override
    public boolean isDead() {
        return this.health <= 0; // Crawler zombi öldüğünde true dönecek
    }

    @Override
    public void draw(Graphics g) {
        if (image == null) image = new ImageIcon("images/zombie_crawler.png").getImage(); // Kayıttan yüklendiyse görseli tekrar yükle
        g.drawImage(image, (int) x, (int) y,  48, 48, null);
    }
}
