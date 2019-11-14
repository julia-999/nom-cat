import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;

public class Sound {

	private Clip clipMenu, clipGame, clip;
	private final String menuMusic = "menu.wav", gameMusic = "game.wav", button = "ping1.wav", chew = "chewing.wav", gameOver = "GameOver.wav";
	private AudioInputStream ais;

	public void playMenuMusic() {
		File sound = new File(menuMusic);

		try {
			ais = AudioSystem.getAudioInputStream(sound);
			clipMenu = AudioSystem.getClip();
			clipMenu.open(ais); //Clip opens AudioInputStream
			clipMenu.loop(clipMenu.LOOP_CONTINUOUSLY); //Start playing audio
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}


	public void playGameMusic() {
		File sound = new File(gameMusic);

		try {
			ais = AudioSystem.getAudioInputStream(sound);
			clipGame = AudioSystem.getClip();
			clipGame.open(ais); //Clip opens AudioInputStream
			clipGame.loop(clipGame.LOOP_CONTINUOUSLY); //Start playing audio
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void playButton() {
		File sound = new File(button);

		try {
			ais = AudioSystem.getAudioInputStream(sound);
			clip = AudioSystem.getClip();
			clip.open(ais); //Clip opens AudioInputStream
			clip.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void playChew() {
		File sound = new File(chew);

		try {
			ais = AudioSystem.getAudioInputStream(sound);
			clip = AudioSystem.getClip();
			clip.open(ais); //Clip opens AudioInputStream
			clip.start();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void playGameOver() {
		File sound = new File(gameOver);

		try {
			ais = AudioSystem.getAudioInputStream(sound);
			clip = AudioSystem.getClip();
			clip.open(ais); //Clip opens AudioInputStream
			clip.start();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void stopAll() {
		if (clipMenu != null && clipMenu.isRunning()) {
			clipMenu.stop();
			clipMenu.close();
		}
		if (clipGame != null && clipGame.isRunning()) {
			clipGame.stop();
			clipGame.close();
		}
	}
}
