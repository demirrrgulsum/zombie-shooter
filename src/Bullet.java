import java.io.Serializable;
import java.awt.Color;
import java.awt.Graphics;

public class Bullet implements Serializable {
    protected double x, y;
    private double dx, dy;
    private int size = 6;
    private double speed = 10;

    private boolean piercing = false;
    private boolean explosive = false;

    private int damage;  // Merminin hasar değeri

    // Constructor'da damage parametresi de alacak
    public Bullet(int startX, int startY, int targetX, int targetY, int damage) {
        this.x = startX;
        this.y = startY;
        this.damage = damage;  // Hasar değeri atanıyor

        double angle = Math.atan2(targetY - y, targetX - x);
        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval((int) x, (int) y, size, size);
    }

    public boolean isOutOfBounds() {
        return x < 0 || x > GamePanel.WIDTH || y < 0 || y > GamePanel.HEIGHT;
    }

    public void setPiercing(boolean val) {
        piercing = val;
    }

    public boolean isPiercing() {
        return piercing;
    }

    public void setExplosive(boolean val) {
        explosive = val;
    }

    public boolean isExplosive() {
        return explosive;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    // size değerini döndüren getSize() metodunu ekleyelim
    public int getSize() {
        return size;
    }

    // Merminin hasarını döndüren getter metodunu ekleyelim
    public int getDamage() {
        return damage;
    }
}
