/*
 * Copyright (c) 2017. by Sebastian Witasik
 * All rights reserved. No part of this application may be reproduced or be part of other software, without the prior written permission of the publisher. For permission requests, write to the author(WitasikSebastian@gmail.com).
 */

package jw.kingdom.hall.kingdomtimer.device.monitor;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MonitorObservableList extends ObservableListWrapper<Monitor> {
	private boolean ignoreChange;

	MonitorObservableList(){
		this(new ArrayList<>());
	}

	private MonitorObservableList(List<Monitor> list) {
		super(list);
		initialize();
	}

	private void initialize() {
		MonitorManager.addListener(new MonitorEventHandler() {
			@Override
			public void onPlugIn(GraphicsDevice device) {
				Monitor monitor = new Monitor(device);
                Platform.runLater(()->add(monitor));
			}

			@Override
			public void onPlugOut(GraphicsDevice device) {
				Monitor monitor = new Monitor(device);
                Platform.runLater(()->remove(monitor.ID));
			}
		});

		addListener((ListChangeListener<Monitor>) change -> {
			if(ignoreChange){
				return;
			}
			sortList();
		});
	}

	public boolean remove(String ID) {
		boolean isRemoved = false;
		Monitor toRemove = null;
		for(Monitor monitor:this){
			if(monitor.ID.equals(ID)){
				toRemove = monitor;
			}
		}
		if(toRemove!=null) {
			remove(toRemove);
			isRemoved=true;
		}
		return isRemoved;
	}

	public Monitor get(String ID) throws MonitorNotFound {
		for (Monitor monitor : this) {
			if (monitor.ID.equals(ID)) {
				return monitor;
			}
		}
		throw new MonitorNotFound();
	}

	public void set(Monitor newMonitor){
		for(int i=0;i<size();i++){
			Monitor monitor = get(i);
			if(monitor.ID.equals(newMonitor.ID)){
				set(i, newMonitor);
			}
		}
	}

	private void sortList() {
		ignoreChange = true;
		/*
		 * @return the value {@code 0} if {@code x == y};
		 *         a value less than {@code 0} if {@code x < y}; and
		 *         a value greater than {@code 0} if {@code x > y}
		 */
		sorted((o1, o2) -> {
			if(o1==null && o2!=null) {
				return -1;
			} else if(o1!=null && o2==null) {
				return 1;
			} else if(o1 == null) {
				return 0;
			}
			return Double.compare(o1.getBounds().getX(), o2.getBounds().getX());
		});
		for(int i=0;i<size();i++){
			Monitor monitor = get(i);
			monitor.setPlace(i);
		}
		ignoreChange = false;
	}
}
