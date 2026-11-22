import java.io.Serializable;
import java.util.ArrayList;

public abstract class Weapon implements Serializable {
    protected int clipSize;
    protected int totalAmmo;
    protected int currentAmmo;
    protected int fireRate;
    protected long lastShotTime = 0;
    protected int damage;  // Silahın verdiği hasar

    public Weapon(int clipSize, int totalAmmo, int fireRate, int damage) {
        this.clipSize = clipSize;
        this.totalAmmo = totalAmmo;
        this.currentAmmo = clipSize;
        this.fireRate = fireRate;
        this.damage = damage;
    }

    // Her silah için fire metodunu implement etme
    public abstract ArrayList<Bullet> fire(int startX, int startY, int targetX, int targetY);

    public void reload() {
        int needed = clipSize - currentAmmo;
        if (totalAmmo >= needed) {
            currentAmmo = clipSize;
            totalAmmo -= needed;
        } else {
            currentAmmo += totalAmmo;
            totalAmmo = 0;
        }
    }

    public boolean canFire() {
        long now = System.currentTimeMillis();
        long delay = 60000 / fireRate;
        return (now - lastShotTime >= delay && currentAmmo > 0);
    }

    public void markFired() {
        lastShotTime = System.currentTimeMillis();
        currentAmmo--;
    }

    public int getCurrentAmmo() { return currentAmmo; }
    public int getTotalAmmo() { return totalAmmo; }
    public String getName() { return this.getClass().getSimpleName(); }
    public int getDamage() { return this.damage; }  // Hasar değerini döndüren getter
}
