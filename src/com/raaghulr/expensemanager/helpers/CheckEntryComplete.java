 


package com.raaghulr.expensemanager.helpers;

import java.io.File;

import android.content.Context;

import com.raaghulr.expensemanager.models.Entry;
import com.raaghulr.expensemanager.R;

public class CheckEntryComplete {
	private FileHelper fileHelper;
	
	public boolean isEntryComplete(Entry entryList, Context mContext) {
		fileHelper = new FileHelper();
		if(isAmountValid(entryList.amount)) {
			if (entryList.type.equals(mContext.getString(R.string.camera))) {
				return isCameraFileReadable(entryList.id);
			} else if (entryList.type.equals(mContext.getString(R.string.voice))) {
				return isAudioFileReadable(entryList.id);
			} else if (entryList.type.equals(mContext.getString(R.string.text))) {
				return isTagValid(entryList.description,mContext);
			}
		}
		return false;
	}
	
	private boolean isAmountValid(String amount) {
		if( amount!= null) {
			if (amount.contains("?")) {
				return false;
			} else {
				return true;
			} 
				
		} else {
			return false;
		}
	}
	
	private boolean isTagValid(String tag,Context context) {
		if(tag != null) {
			if (tag.equals("") || tag.equals(context.getString(R.string.unfinished_textentry)) || tag.equals(context.getString(R.string.finished_textentry))) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	private boolean isAudioFileReadable(String id) {
		File mFile = fileHelper.getAudioFileEntry(id);
		if (mFile.canRead()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isCameraFileReadable(String id) {
		File mFileSmall = fileHelper.getCameraFileSmallEntry(id);
		File mFile = fileHelper.getCameraFileLargeEntry(id);
		File mFileThumbnail = fileHelper.getCameraFileThumbnailEntry(id);
		if (mFile.canRead() && mFileSmall.canRead() && mFileThumbnail.canRead()) {
			return true;
		}
		return false;
	}
}
