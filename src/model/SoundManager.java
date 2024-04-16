package model;

import javax.sound.sampled.*;
import java.io.File;
import java.net.URL;

public class SoundManager {
    private Clip backgroundMusic;
    private Clip clickSound;
    private Clip winSound;
    private Clip rotateSound;
    private Clip timerSound;
    private Clip levelMusic;

    public SoundManager() {
        loadSounds();
    }

    private void loadSounds() {
        try {
            // Load sounds
            backgroundMusic = loadSound("res/audio/main_theme.wav");
            clickSound = loadSound("res/audio/button_click.wav");
            winSound = loadSound("res/audio/win.wav");
            rotateSound = loadSound("res/audio/pipe_rotate.wav");
            levelMusic = loadSound("res/audio/levelPlaying.wav");
            //timerSound = loadSound("res/audio/timer.wav");

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
        if (backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void playLevelMusic(){
        if (levelMusic != null) {
            levelMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopLevelMusic(){
        if (levelMusic != null) {
            levelMusic.stop();
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

    private void playSound(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
