
package de.jwi.ostendoplugin.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.jwi.ostendoplugin.Activator;
import de.jwi.ostendoplugin.idl.IDLInterface;
import de.jwi.ostendoplugin.idl.IDLOperation;

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

/**
 * @author Juergen Weber
 * created 22.03.2011
 *
 */
public class IDLLabelProvider extends LabelProvider
{
	static String pluginID = Activator.PLUGIN_ID;
	
	private static final Image interfaceImage = AbstractUIPlugin
			.imageDescriptorFromPlugin(pluginID,
					"icons/int_obj.gif").createImage();

	private static final Image operationImage = AbstractUIPlugin
			.imageDescriptorFromPlugin(pluginID,
					"icons/op_obj.gif").createImage();

	private static final Image fileImage = AbstractUIPlugin
	.imageDescriptorFromPlugin(pluginID,
			"icons/file_obj.gif").createImage();
	
	OstendoView view;
	
	public IDLLabelProvider(OstendoView view)
	{
		super();
		this.view = view;
	}

	@Override
	public Image getImage(Object element)
	{
		if (element instanceof IDLOperation)
		{
			return operationImage;
		}

		if (element instanceof IDLInterface)
		{
			return interfaceImage;
		}
		
		return fileImage;
	}

	@Override
	public String getText(Object element)
	{
		return element.toString();
	}
}
