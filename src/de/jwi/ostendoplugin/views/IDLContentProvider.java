
package de.jwi.ostendoplugin.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.jwi.ostendoplugin.idl.IDL;
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

public class IDLContentProvider implements ITreeContentProvider
{

	@Override
	public Object[] getChildren(Object parent)
	{
		if (parent instanceof IDL)
		{
			Object[] array = ((IDL)parent).idlInterfaces.toArray();
			return array;
		}

		if (parent instanceof IDLInterface)
		{
			Object[] array = ((IDLInterface)parent).operations.toArray();
			return array;
		}

		return null;
	}

	public Object[] getElements(Object inputElement)
	{
		return (Object[]) inputElement;
	}

	@Override
	public Object getParent(Object element)
	{
		if (element instanceof IDLOperation)
		{
			return ((IDLOperation)element).idlInterface;
		}

		if (element instanceof IDLInterface)
		{
			return ((IDLInterface)element).idl;
		}

		return null;
	}

	@Override
	public boolean hasChildren(Object parent)
	{
		return !(parent instanceof IDLOperation);
	}

	@Override
	public void dispose()
	{

	}

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2)
	{
	}

}
