
package de.jwi.ostendoplugin.views;

import java.io.File;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.jwi.ostendoplugin.Activator;
import de.jwi.ostendoplugin.idl.LoggedMessage;
import de.jwi.ostendoplugin.idl.MessageTypeChecker;

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

public class LogViewerLabelProvider extends LabelProvider
{
	static String pluginID = Activator.PLUGIN_ID;
	
	private static final Image folderImage = AbstractUIPlugin
			.imageDescriptorFromPlugin(pluginID,
					"icons/folder.gif").createImage();

	private static final Image driveImage = AbstractUIPlugin
			.imageDescriptorFromPlugin(pluginID,
					"icons/filenav_nav.gif").createImage();

	private static final Image fileImage = AbstractUIPlugin
			.imageDescriptorFromPlugin(pluginID,
					"icons/file_obj.gif").createImage();

	private static final Image requestImage = AbstractUIPlugin
					.imageDescriptorFromPlugin(pluginID,
							"icons/nav_forward.gif").createImage();

	private static final Image replyImage = AbstractUIPlugin
					.imageDescriptorFromPlugin(pluginID,
							"icons/nav_backward.gif").createImage();

	
	OstendoView view;
	
	public LogViewerLabelProvider(OstendoView view)
	{
		super();
		this.view = view;
	}

	@Override
	public Image getImage(Object element)
	{
		LoggedMessage message = (LoggedMessage) element;
		File file = message.file;
		if (file.isDirectory())
		{
			return file.getParent() != null ? folderImage : driveImage;
		}
		
		int messageType = message.getMessageType();
		
		if (messageType == MessageTypeChecker.REQUEST)
		{
			return requestImage;
		}

		if (messageType == MessageTypeChecker.REPLY)
		{
			return replyImage;
		}
		
		return fileImage;
	}

	@Override
	public String getText(Object element)
	{
		// Toplevel mit vollem Path anzeigen
		if (Activator.getDefault().logdirs.contains(element))
		{
			return element.toString();
		}
		
		String fileName = ((LoggedMessage) element).file.getName();
		if (fileName.length() > 0)
		{
			return fileName;
		}
		return ((LoggedMessage) element).file.getPath();
	}
}
