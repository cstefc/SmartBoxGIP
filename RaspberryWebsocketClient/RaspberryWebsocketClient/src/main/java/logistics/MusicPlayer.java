package logistics;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class MusicPlayer {
	private ArrayList<File> musicFiles = new ArrayList<>();
	private Player player;

	//private final static String url= "//home//pi//Muziek";
	
	private final static String url = "//Users//stefosse//Music";
	private final static int NOTSTARTED = 0;
	private final static int PLAYING = 1;
	private final static int PAUSED = 2;
	private final static int FINISHED = 3;

	// locking object used to communicate with player thread
	private final Object playerLock = new Object();

	// status variable what player thread is doing/supposed to do
	private int playerStatus = NOTSTARTED;

	public int loop = 0;

	public MusicPlayer() {
		getSongs();
		setUpNext();
	}

	// Starts playback (resumes if paused)
	public void play() {
		synchronized (playerLock) {
			switch (playerStatus) {
			case NOTSTARTED:

				final Runnable r = new Runnable() {
					public void run() {
						playInternal();
					}
				};

				final Thread t = new Thread(r);
				t.setDaemon(true);
				t.setPriority(Thread.MAX_PRIORITY);
				playerStatus = PLAYING;
				t.start();
				break;
			case PAUSED:
				resume();
				break;
			default:
				break;
			}
		}
	}

	// Pauses playback. Returns true if new state is PAUSED.
	public boolean pause() {
		synchronized (playerLock) {
			if (playerStatus == PLAYING) {
				playerStatus = PAUSED;
			}
			return playerStatus == PAUSED;
		}
	}

	// Resumes playback. Returns true if the new state is PLAYING.
	public boolean resume() {
		synchronized (playerLock) {
			if (playerStatus == PAUSED) {
				playerStatus = PLAYING;
				playerLock.notifyAll();
			}
			return playerStatus == PLAYING;
		}
	}

	// Stops playback. If not playing, does nothing
	public void stop() {
		synchronized (playerLock) {
			playerStatus = FINISHED;
			playerLock.notifyAll();
		}
	}

	private void playInternal() {
		while (playerStatus != FINISHED) {

			try {
				if (!player.play(1)) {
					setUpNext();
					playerStatus = PLAYING;
					playInternal();
				}
			} catch (JavaLayerException e1) {
				break;
			}

			// check if paused or terminated
			synchronized (playerLock) {
				while (playerStatus == PAUSED) {
					try {
						playerLock.wait();
					} catch (InterruptedException e) {
						// terminate player
						break;
					}
				}
			}

		}
		close();
	}

	// Closes the player, regardless of current state.
	public void close() {
		synchronized (playerLock) {
			playerStatus = FINISHED;
		}
		try {
			player.close();
		} catch (final Exception e) {
			// ignore, we are terminating anyway
		}
	}

	public int getState() {
		return playerStatus;
	}

	public void setUpNext() {
		try {
			FileInputStream fis = new FileInputStream(musicFiles.get(loop));
			player = new Player(fis);

			loop++;
			if (loop == musicFiles.size()-1) {
				Collections.shuffle(musicFiles);
				loop = 0;
				playerStatus = NOTSTARTED;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getSongs() {
		File folder = new File(url);
		File[] listFiles = folder.listFiles();

		// Gets all music files in the folder
		for (File file : listFiles) {
			if (getExtensionOfFile(file).equals("mp3")) {
				musicFiles.add(file);
			}
		}
		Collections.shuffle(musicFiles);
	}

	public static String getExtensionOfFile(File file) {
		String fileExtension = "";
		String fileName = file.getName();

		// If fileName do not contain "." or starts with "." then it is not a valid file
		if (fileName.contains(".") && fileName.lastIndexOf(".") != 0) {
			fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
		}

		return fileExtension;
	}

}
