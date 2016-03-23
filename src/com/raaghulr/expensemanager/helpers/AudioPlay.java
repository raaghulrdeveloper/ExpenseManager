   


package com.raaghulr.expensemanager.helpers;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

public class AudioPlay {


	private File mPath;
	private MediaPlayer mPlayer;
	private Context mContext;
	private FileHelper fileHelper;

	
	public AudioPlay(String id, Context _context,Boolean isFav) {
		mContext = _context;
		mPlayer = new MediaPlayer();
		fileHelper = new FileHelper();
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			mPath = getFile(isFav,id);
			mPlayer.setScreenOnWhilePlaying(true);
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			try {
				mPlayer.setDataSource(mPath.toString());
				mPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(mContext, "sdcard not available", Toast.LENGTH_LONG).show();
		}
	}

	private File getFile(Boolean isFav,String id) {
		if(isFav) {
			return fileHelper.getAudioFileFavorite(id);
		} else {
			return fileHelper.getAudioFileEntry(id);
		}
	}

	
	public void startPlayBack() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			try {
				mPlayer.start();
			} catch (IllegalStateException e) {
			}
		} else {
			Toast.makeText(mContext, "sdcard not available", Toast.LENGTH_LONG).show();
		}
	}

	
	public void stopPlayBack() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			if (mPlayer.isPlaying())
				mPlayer.stop();
		} else {
			Toast.makeText(mContext, "sdcard not available", Toast.LENGTH_LONG).show();
		}
	}

	
	public int getPlayBackTime() {
		return mPlayer.getDuration();
	}

	
	public boolean isAudioPlaying() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return mPlayer.isPlaying();
		} else {
			return false;
		}
	}
}
