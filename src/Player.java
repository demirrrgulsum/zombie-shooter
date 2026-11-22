import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;

public class Player implements KeyListener, Serializable {
    private double x, y;
    private double speed;
    private int size;
    private int health;
    private transient Image spriteSheet;  // Sprite sheet (kaydedilmez, yüklemeden sonra tekrar yüklenir)
    private int spriteX, spriteY;  // Sprite'ın X ve Y koordinatları
    private final int spriteWidth = 32;  // Her sprite'ın genişliği
    private final int spriteHeight = 32;  // Her sprite'ın yüksekliği 
    

    private boolean movingUp, movingDown, movingLeft, movingRight;

    private Weapon currentWeapon;
    private long lastHitTime = 0; // Son hasar alma zamanı

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.size = 64;
        //dikkat burayı değiştirdik
        this.speed = 2.0;
        this.health = 200;
        this.currentWeapon = new Pistol();  // Oyuncu tabancayla başlar
        this.spriteSheet = new ImageIcon("images/pixel_character_pale_blue_original.png").getImage();  // Sprite sheet'i buradan yüklüyoruz
       
    }
    public void update() {
        // Sadece tuşlar basıldığında hareket etsin
        if (movingUp) y -= speed;
        if (movingDown) y += speed;
        if (movingLeft) x -= speed;
        if (movingRight) x += speed;
    
        // Ekran sınırları kontrolü
        if (x < 0) x = 0;  // Ekranın sol sınırı
        if (x + size > GamePanel.WIDTH) x = GamePanel.WIDTH - size;  // Ekranın sağ sınırı
        if (y < 0) y = 0;  // Ekranın üst sınırı
        if (y + size > GamePanel.HEIGHT) y = GamePanel.HEIGHT - size;  // Ekranın alt sınırı
    
        if (health <= 0) {
            die();
        }
    }
    
    

    public void draw(Graphics g) {
        // Yönlere göre sprite'ı seçiyoruz
        if (movingUp) {
            spriteX = 0;
            spriteY = 0;  // Yukarı yön için sprite'ı ayarla
        }
        if (movingDown) {
            spriteX = 0;
            spriteY = spriteHeight*2;  // Aşağı yön için sprite'ı ayarla
        }
        if (movingLeft) {
            spriteX = 0;
            spriteY = spriteHeight *2;  // Sola yön için sprite'ı ayarla
        }
        if (movingRight) {
            spriteX = spriteWidth *2;
            spriteY =  spriteHeight *2; // Sağa yön için sprite'ı ayarla
        }

        // Kayıttan yüklendiyse sprite sheet null olur, tekrar yükle
        if (spriteSheet == null) {
            spriteSheet = new ImageIcon("images/pixel_character_pale_blue_original.png").getImage();
        }

        // Sprite'ı çizeriz
        if (spriteSheet != null) {
            g.drawImage(spriteSheet, (int) x, (int) y, (int) (x + spriteWidth * 1.2), (int) (y + spriteHeight * 1.2),
                        spriteX, spriteY, spriteX + spriteWidth, spriteY + spriteHeight, null);  // Görseli daha büyük boyutlandırıyoruz
        } else {
            g.setColor(Color.GREEN);
            g.fillOval((int) x, (int) y, size, size);  // Eğer görsel yoksa, yeşil daire çizeriz
        }

        // Sağlık çubuğu
        g.setColor(Color.RED);  // Sağlık çubuğu kırmızı
        g.fillRect((int) x, (int) y - 10, spriteWidth, 5);  // Sağlık çubuğu arka planı
        g.setColor(Color.GREEN);  // Sağlık barının yeşil kısmı
        g.fillRect((int) x, (int) y - 10, (int) (spriteWidth * ((double) health / 200)), 5);  // Sağlık barı
    }

    public void takeDamage(int damage) {
        long now = System.currentTimeMillis();
        if (now - lastHitTime < 500) return; // Her karede değil, en fazla 0.5 saniyede bir hasar al
        lastHitTime = now;
        health -= damage;
        if (health <= 0) die();
    }

    public void die() {
        System.out.println("Oyuncu öldü!"); 
        
        
    }

    public void setDirection(boolean up, boolean down, boolean left, boolean right) {
        this.movingUp = up;
        this.movingDown = down;
        this.movingLeft = left;
        this.movingRight = right;
    }

    public void setWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
    }

    public Weapon getWeapon() {
        if (currentWeapon == null) {
            currentWeapon = new Rifle(); // Null ise, varsayılan olarak bir silah ekliyoruz
        }
        return this.currentWeapon;
    }

    // Getter ve Setter
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return size; }
    public int getHeight() { return size; }
    public int getHealth() { return health; }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> setDirection(true, false, false, false);  // Yukarı
            case KeyEvent.VK_S -> setDirection(false, true, false, false);  // Aşağı
            case KeyEvent.VK_A -> setDirection(false, false, true, false);  // Sola
            case KeyEvent.VK_D -> setDirection(false, false, false, true);  // Sağa
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> setDirection(false, movingDown, movingLeft, movingRight);  // W bırakıldığında hareketi durdur
            case KeyEvent.VK_S -> setDirection(movingUp, false, movingLeft, movingRight);  // S bırakıldığında hareketi durdur
            case KeyEvent.VK_A -> setDirection(movingUp, movingDown, false, movingRight);  // A bırakıldığında hareketi durdur
            case KeyEvent.VK_D -> setDirection(movingUp, movingDown, movingLeft, false);  // D bırakıldığında hareketi durdur
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
