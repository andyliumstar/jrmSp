/*
 * 
 * Copyright (C) 2010 The Android Open Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the ""License"");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an ""AS IS"" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jrm.skype.api;

import java.lang.ref.WeakReference;

import android.view.Surface;

public class NativeCodeCaller {

	private int mNativeContext; // Accessed by native methods

	static {
		//System.loadLibrary("SkypeKitVideoDemo");
		System.loadLibrary("SkypeKit371");
	}

	@Override
	protected void finalize() {
		native_release();
	}

	public NativeCodeCaller() {
		native_setup(new WeakReference<NativeCodeCaller>(this));
	}

	private native final void native_setup(Object caller_this);

	private native final void native_release();

	public native final boolean startAVHosts();

	public native final boolean stopAVHosts();

	public native final void setVideoDisplay(Surface surface);

	public native final void setPreviewDisplay(Surface surface);

	public native final boolean initAVHosts(int mode);
	
	public native final boolean initAVHostsEx(int mode);
	
	public native final boolean initSurface();
	
	public native final int startAudioIn();

	public native final int stopAudioIn();
	
	public native final boolean stopRemoteVideo();
	
	public native final boolean stopLocalVideo();
}
