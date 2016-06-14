package de.jwi.ostendoplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.omg.CORBA.ORB;
import org.osgi.framework.BundleContext;

import de.jwi.ostendoplugin.idl.IDL;
import de.jwi.ostendoplugin.idl.IDLOperation;
import de.jwi.ostendoplugin.idl.LoggedMessage;

/*
 * OstendoPlugin - An Eclipse Pluing for the Ostendo CORBA IIOP Message Analyzer
 * 
 * Copyright (C) 2011 Juergen Weber
 * 
 * This file is part of OstendoPlugin.
 * 
 * OstendoPlugin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OstendoPlugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with OstendoPlugin.  If not, see <http://www.gnu.org/licenses/>.
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.jwi.OstendoPlugin"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	public boolean stateIsRestored = false;

	public List<IDL> idls = new ArrayList<IDL>();

	public List<LoggedMessage> logdirs = new ArrayList<LoggedMessage>();

	public String lastLogDir = null;

	public String lastIDLDir = null;

	public Map<String, IDLOperation> opnames = new HashMap<String, IDLOperation>();

	public Map<String, Integer> opnameInstanceCount = new HashMap<String, Integer>();
	
	public ORB orb;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
    	Properties p = new Properties();

		p.setProperty("org.omg.CORBA.ORBClass", "org.jacorb.orb.ORB");
		p.setProperty("org.omg.CORBA.ORBSingletonClass",
				"org.jacorb.orb.ORBSingleton");
		p.setProperty("jacorb.log.default.verbosity", "0");
        
		String[] args = {};
		
        orb = ORB.init(args, p);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		orb.destroy();
		orb = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
