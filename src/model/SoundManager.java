package model;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

public class SoundManager {

    private static SoundManager instance = null;

    private boolean soundMusic = true;
    private boolean soundSfx = true;

    private Clip backgroundMusic;
    private Clip clickSound;
    private Clip winSound;
    private Clip rotateSound;
    private Clip timerSound;
    private Clip levelMusic;
    private Clip pauseMenu;
    private Clip lostBoom;


    public SoundManager() {
        loadSounds();
    }
    
    
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }

        return instance;
    }


    public boolean getSoundMusic() { return this.soundMusic; }
    public boolean getSoundSfx() { return this.soundSfx; }


    private void loadSounds() {
        try {
            backgroundMusic = loadSound("res/audio/main_theme.wav");
            clickSound = loadSound("res/audio/button_click.wav");
            winSound = loadSound("res/audio/win.wav");
            rotateSound = loadSound("res/audio/pipe_rotate.wav");
            levelMusic = loadSound("res/audio/levelPlaying.wav");
            pauseMenu = loadSound("res/audio/pauseMenu.wav");
            timerSound = loadSound("res/audio/timerSound.wav"); 
            lostBoom = loadSound("res/audio/lostBoom.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Clip loadSound(String filePath) {
        try {
            File f = new File("./" + filePath);
            URL url = f.toURI().toURL();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void playBackgroundMusic() {
        if (soundMusic && backgroundMusic != null && backgroundMusic.isActive() == false) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }


    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isActive() == true) {
            backgroundMusic.stop();
        }
    }


    public void playLevelMusic(){
        if (soundMusic && levelMusic != null && levelMusic.isActive() == false) {
            levelMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }


    public void stopLevelMusic(){
        if (levelMusic != null && levelMusic.isActive()) {
            levelMusic.stop();

        }
    }


    public void playTimerMusic() {
        if (soundMusic && timerSound != null && timerSound.isActive() == false) {
            playSound(timerSound);
        }
    }


    public void stopTimerMusic(){
        if (timerSound != null && timerSound.isActive()) {
            timerSound.stop();
        }
    }


    public void playClickSound() {
        playSound(clickSound);
    }

    public void playWinSound() {
        playSound(winSound);
    }

    public void playRotateSound() {
        playSound(rotateSound);
    }

    public void playPauseSound(){
        playSound(pauseMenu);
    }

    public void playLostBoom(){
        playSound(lostBoom);
    }

    public void changeMusic() {
        this.soundMusic = !soundMusic;
    }

    public void changeSfx() {
        this.soundSfx = !soundSfx;
    }


    private void playSound(Clip clip) {
        if (clip != null && soundSfx) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
