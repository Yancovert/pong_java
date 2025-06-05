package com.ponggame;

import javax.sound.sampled.*;
import java.io.InputStream;

public class MusicPlayer {

    private Clip clip;

    public MusicPlayer() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/resources/music.wav");
            if (audioSrc == null) {
                System.err.println("Music resource not found.");
                return;
            }
            InputStream bufferedIn = new java.io.BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playLoop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
