package com.mason.meizu.sample.prvclass;

class ClassD4Listener {
	
	public int testInterface(Listener listener, int i) {
		return listener.onChange(i);
	}
	
	private static interface Listener {
		int onChange(int i);
	}
}
