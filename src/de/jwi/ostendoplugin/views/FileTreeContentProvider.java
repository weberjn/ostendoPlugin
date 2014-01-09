/**
 * 
 */
package de.jwi.ostendoplugin.views;

import java.io.File;

import java.util.Arrays;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
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
 * created 15.03.2011
 *
 */
public class FileTreeContentProvider implements ITreeContentProvider
{

	@Override
	public void dispose()
	{
	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2)
	{
	}

	@Override
	public Object[] getChildren(Object parent)
	{
		File file = (File) parent;
		File[] files = file.listFiles();
		Arrays.sort(files);
		return files;
	}

	@Override
	public Object[] getElements(Object inputElement)
	{
		return (Object[]) inputElement;
	}

	@Override
	public Object getParent(Object element)
	{
		File file = (File) element;
		File parent = file.getParentFile();
		return parent;
	}

	@Override
	public boolean hasChildren(Object parent)
	{
		File file = (File) parent;
		boolean b = file.isDirectory();
		return b;
	}

}
