
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel implements KeyListener, MouseListener, MouseMotionListener {
    public static final int WIDTH = 800;  // Genişlik
    public static final int HEIGHT = 600; // Yükseklik

    private Player player;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Zombie> zombies = new ArrayList<>();

    private int score = 0;
    private int wave = 1;
    private int zombiesRemaining = 0;
    private int zombieSpawnTimer = 0;

    private boolean gamePaused = true; // Başlangıçta oyun duraklatılmış olmalı
    private boolean gameStarted = false; // Oyun başladığında menü gizlenecek

    private JPanel pauseMenu;
    private JButton resumeButton, saveButton, loadButton, exitButton;
    private JButton startButton; // Başlangıç için buton
    private Image menuBackground; // Menü arka planı 
    private Image gameBackground; // Oyun arka planı
    private JButton pauseButton;  // Bu satırın olduğunu kontrol edin

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        player = new Player(WIDTH / 2 - 20, HEIGHT / 2 - 20);  // Player başlangıç konumu
        loadMenuBackground();  // Menü arka planını yükle 
        loadGameBackground(); // Oyun arka planını yükle
        createPauseMenu();
        createPauseButton();
        startGameLoop();
    }

    private void loadMenuBackground() {
        menuBackground = new ImageIcon("images/menu_bg.png").getImage();  // Menü arka planını yükle
    } 
    // Arka plan görselini yüklemek için metod
    private void loadGameBackground() {
    gameBackground = new ImageIcon("images/image.png").getImage(); // Arka plan görseli yolu
    }


    private void startGameLoop() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!gamePaused) { // Sadece oyun duraklatılmadıysa güncellemeyi yap
                    update();
                }
                repaint();
            }
        }, 0, 16);
    } 
    private void startWave(int wave) {
        System.out.println("Yeni Dalga: " + wave);
        zombiesRemaining = 5 + wave * 2;  // Dalga başına çıkan zombi sayısı
    
        // Yeni dalga için oyuncuya yeni silahlar ver
        switch (wave) {
            case 2 -> player.setWeapon(new Rifle());
            case 4 -> player.setWeapon(new Shotgun());
            case 6 -> player.setWeapon(new Sniper());
            case 11 -> player.setWeapon(new RocketLauncher());
        }
    }
    
    private void update() {
        if (gamePaused) return; // Oyun duraklatıldıysa güncelleme yapılmaz
    
        // Oyuncunun hareketini güncelleme
        player.update(); // Player hareketini güncelleme
    
        // Mermilerin güncellenmesi
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.update(); // Mermiyi güncelle
            if (b.isOutOfBounds()) {
                bullets.remove(i);  // Mermi ekran dışına çıkarsa silinir
                i--;
                continue; // Silinen mermiyle çarpışma kontrolü yapma
            }
    
            // Merminin zombilere çarpıp çarpmadığını kontrol et
            for (int j = 0; j < zombies.size(); j++) {
                Zombie z = zombies.get(j);
                if (z.checkCollisionWithBullet(b)) {
                    z.takeDamage(b.getDamage()); // Zombi mermiden hasar alacak
                    if (!b.isPiercing()) { // Delici mermi (Sniper) yoluna devam eder
                        bullets.remove(i); // Mermi zombiye çarptığında yok edilsin
                        i--;
                    }
                    if (z.isDead()) {
                        zombies.remove(j);
                        score += 10;  // Zombi öldüğünde skor ekle
                    }
                    break; // Çarpışma bulunduktan sonra döngüyü bitir
                }
            }
        }
    
        // Zombilerin güncellenmesi
        for (int i = 0; i < zombies.size(); i++) {
            Zombie z = zombies.get(i);
            z.update(player);  // Zombi oyuncuya doğru hareket eder
    
            // Zombi öldüyse, görseli kaybolacak ve listeden çıkarılacak
            if (z.isDead()) {
                zombies.remove(i);
                i--;
                score += 10;
            }
    
            // Zombi ile çarpışma kontrolü ve oyuncuya hasar verme
            if (!z.isDead()) {
                z.checkCollisionWithPlayer(player); // Zombi ve oyuncu arasındaki çarpışmayı kontrol eder
            }
        }
    
        // Eğer tüm zombiler öldüyse ve yeni dalga başlatılacaksa
        if (zombies.isEmpty() && zombiesRemaining == 0) {
            wave++;
            startWave(wave);
        }
    
        // Zombi üretme işlemi
        if (zombiesRemaining > 0) {
            zombieSpawnTimer++;
            if (zombieSpawnTimer >= 60) {
                spawnZombie(wave);
                zombieSpawnTimer = 0;
                zombiesRemaining--;
            }
        }
    
        // Eğer oyuncunun canı 0 ise, oyunu bitir
        if (player.getHealth() <= 0) {
            gameOver();
        }
    }
    
    
    private void spawnZombie(int wave) {
        double x = Math.random() < 0.5 ? 0 : WIDTH;
        double y = Math.random() * HEIGHT;
        Zombie z;

        // Dalga seviyesine göre zombileri çeşitlendir
        if (wave >= 10 && Math.random() < 0.3) z = new SpitterZombie(x, y);
        else if (wave >= 7 && Math.random() < 0.25) z = new TankZombie(x, y);
        else if (wave >= 4 && Math.random() < 0.4) z = new CrawlerZombie(x, y);
        else z = new NormalZombie(x, y);

        zombies.add(z);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        // Arka plan görseli yükleme ve çizme
        if (gamePaused || !gameStarted) {
            // Menü ekranında arka planı gösteriyoruz
            if (menuBackground != null) {
                g.drawImage(menuBackground, 0, 0, WIDTH, HEIGHT, null);
            }
    
            g.setColor(Color.WHITE);
            g.drawString("Oyun Başlangıç Menü", WIDTH / 2 - 100, HEIGHT / 2 - 50);
        } else {
            // Oyun devam ediyorsa
            if (gameBackground != null) {
                g.drawImage(gameBackground, 0, 0, WIDTH, HEIGHT, null);  // Arka planı çiziyoruz
            }
    
            player.draw(g);  // Player'ı çiziyoruz
            for (Bullet b : bullets) b.draw(g);
            for (Zombie z : zombies) z.draw(g);
    
            // Oyun bilgilerini çizme
            g.setColor(Color.WHITE);
            // Sol üst: silah ve mermi
            g.drawString("Silah: " + player.getWeapon().getName(), 10, 20);
            g.drawString("Şarjör: " + player.getWeapon().getCurrentAmmo() + " | Toplam: " + player.getWeapon().getTotalAmmo(), 10, 40);
            // Sağ üst: can ve puan
            g.drawString("Can: " + player.getHealth(), WIDTH - 110, 20);
            g.drawString("Score: " + score, WIDTH - 110, 40);
        }
    }
    

    private void createPauseMenu() {
        pauseMenu = new JPanel();
        pauseMenu.setLayout(new GridLayout(5, 1, 10, 10)); // 5 satır olacak, birincisi başlangıç menüsü, diğerleri ise oyun menüsü
        pauseMenu.setBounds(WIDTH / 2 - 100, HEIGHT / 2 - 100, 200, 250);
        pauseMenu.setBackground(new Color(0, 0, 0, 180));

        startButton = new JButton("Başla");
        resumeButton = new JButton("Devam Et");
        saveButton = new JButton("Kaydet");
        loadButton = new JButton("Yükle");
        exitButton = new JButton("Çık");

        // Start butonunu başlangıç menüsüne ekliyoruz
        pauseMenu.add(startButton);

        // Oyun menüsündeki butonlar
        pauseMenu.add(resumeButton);
        pauseMenu.add(saveButton);
        pauseMenu.add(loadButton);
        pauseMenu.add(exitButton);

        add(pauseMenu);
        pauseMenu.setVisible(true);  // Menü açılırken görünür olacak

        // Başla butonuna tıklanırsa oyun başlasın
        startButton.addActionListener(e -> startGame());

        resumeButton.addActionListener(e -> resumeGame());
        saveButton.addActionListener(e -> saveGame());
        loadButton.addActionListener(e -> loadGame());
        exitButton.addActionListener(e -> System.exit(0));
    }
    private void resumeGame() {
        gamePaused = false;
        pauseMenu.setVisible(false); 
        pauseButton.setVisible(true);
        requestFocusInWindow(); // Klavye odağını panele geri ver
        repaint();
    }
    private void startGame() {
        // Oyunun başladığını ve duraklamadığını belirt
        gameStarted = true;
        gamePaused = false;
    
        // Başlangıç menüsünü gizle
        pauseMenu.setVisible(false); 

        // Oyun başladığında duraklat butonunu göster
        pauseButton.setVisible(true);  // Oyun başladıktan sonra duraklat butonunu göster

    
        // Oyuncuyu yeniden başlat
        player = new Player(WIDTH / 2 - 20, HEIGHT / 2 - 20);  // Oyuncuyu başa alıyoruz
    
        // Zombileri ve mermileri temizle
        zombies.clear();  // Zombileri temizliyoruz
        bullets.clear();  // Mermileri temizliyoruz
    
        // Skor ve dalgayı sıfırla
        score = 0;  // Skor sıfırlanacak
        wave = 1;  // Dalga numarasını başlatıyoruz
        zombiesRemaining = 0;  // Zombi sayısı sıfırlanacak
        zombieSpawnTimer = 0;  // Zombi spawn sayacı sıfırlanacak
    
        // İlk dalga başlasın
        startWave(wave);
        requestFocusInWindow(); // Klavye odağını panele geri ver
    
        // Oyun başlatıldıktan sonra tüm öğelerin güncellenmesi için update çağrılacak
        repaint();
    }
    private void createPauseButton() {
        pauseButton = new JButton("Duraklat");
        pauseButton.setBounds(WIDTH - 100, 10, 90, 30);
        pauseButton.addActionListener(e -> togglePause());
        pauseButton.setFocusable(false); // Butona tıklayınca klavye odağı panelde kalsın
        pauseButton.setVisible(false);  // Başlangıçta görünmesin
        add(pauseButton);  // Butonu ekliyoruz
    }
    

    private void togglePause() {
        gamePaused = !gamePaused;  // Oyun duraklatılıp duraklatılmadığını kontrol et
        pauseMenu.setVisible(gamePaused);  // Duraklatıldığında menüyü göster
        pauseButton.setVisible(!gamePaused);  // Duraklatıldığında duraklat butonunu gizle
    
        if (gamePaused) {
            saveButton.setVisible(true);   // Kaydet butonunu göster
            exitButton.setVisible(true);   // Çıkış butonunu göster
            resumeButton.setVisible(true); // Devam Et butonunu göster
        } else {
             // Oyun devam ediyorsa, "Duraklat" butonunun metnini geri al
        pauseButton.setText("Duraklat");
        requestFocusInWindow(); // Klavye odağını panele geri ver
        exitButton.setVisible(false);   // Çıkış butonunu gizle
        resumeButton.setVisible(false); // Devam Et butonunu gizle
        }
    }
    

    private void saveGame() {
        GameState state = new GameState(wave, score, player, zombies, bullets);
        FileIO.saveGame(state, "savegame.dat");
    }

    private void loadGame() {
        GameState state = FileIO.loadGame("savegame.dat");
        if (state != null) {
            this.wave = state.wave;
            this.score = state.score;
            this.player = state.player;
            this.zombies = state.zombies;
            this.bullets = state.bullets;
            this.gameStarted = true; // Menüden yüklenince oyun ekranı görünsün
        }
    }

    public void gameOver() {
        System.out.println("Oyun Bitti!");
        // Oyun bitince yapılacak işlemler (menüyü açmak, vs.)
        gamePaused = true;
        pauseMenu.setVisible(true);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> player.setDirection(true, false, false, false);
            case KeyEvent.VK_S -> player.setDirection(false, true, false, false);
            case KeyEvent.VK_A -> player.setDirection(false, false, true, false);
            case KeyEvent.VK_D -> player.setDirection(false, false, false, true);
            case KeyEvent.VK_R -> player.getWeapon().reload();
            case KeyEvent.VK_ESCAPE -> togglePause();
            case KeyEvent.VK_F5 -> saveGame();
            case KeyEvent.VK_F9 -> loadGame();
        }
    }

    public void keyReleased(KeyEvent e) {
        player.keyReleased(e); // Tuş bırakılınca hareket durur
    }
    public void keyTyped(KeyEvent e) {}

    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int px = (int) (player.getX() + player.getWidth() / 2);
        int py = (int) (player.getY() + player.getHeight() / 2);

        Weapon w = player.getWeapon();
        if (w.canFire()) {
            ArrayList<Bullet> newBullets = w.fire(px, py, mouseX, mouseY);
            bullets.addAll(newBullets);
            w.markFired();
        }
    }

    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
}
