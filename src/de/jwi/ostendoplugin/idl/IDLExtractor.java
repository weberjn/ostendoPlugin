/**
 * 
 */
package de.jwi.ostendoplugin.idl;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.jacorb.idl.AliasTypeSpec;
import org.jacorb.idl.ConstrTypeSpec;
import org.jacorb.idl.Declaration;
import org.jacorb.idl.Definition;
import org.jacorb.idl.Definitions;
import org.jacorb.idl.EnumType;
import org.jacorb.idl.IDLTreeVisitor;
import org.jacorb.idl.IdlSymbol;
import org.jacorb.idl.Interface;
import org.jacorb.idl.InterfaceBody;
import org.jacorb.idl.Method;
import org.jacorb.idl.Module;
import org.jacorb.idl.NativeType;
import org.jacorb.idl.OpDecl;
import org.jacorb.idl.Operation;
import org.jacorb.idl.ParamDecl;
import org.jacorb.idl.SimpleTypeSpec;
import org.jacorb.idl.Spec;
import org.jacorb.idl.StructType;
import org.jacorb.idl.TypeDeclaration;
import org.jacorb.idl.TypeDef;
import org.jacorb.idl.UnionType;
import org.jacorb.idl.Value;
import org.jacorb.idl.VectorType;

import de.jwi.ostendo.jacorbidl.ParserCaller;


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
 * created 03.03.2011
 *
 */
public class IDLExtractor implements IDLTreeVisitor
{
	static IDLExtractor instance = new IDLExtractor();
	
	List<IDLInterface> interfaces = new ArrayList<IDLInterface>(); 
	
	private IDLExtractor() {}
	
	public static IDLExtractor getInstance()
	{
		return instance;
	}
	
	public static void main(String[] args)
	{
		new IDLExtractor().extractInterfaces(new File("src/de/jwi/ostendo/testidl/server.idl"));

	}

	public List<IDLInterface> extractInterfaces(File idl)
	{
		String idlName = idl.toString();
		Spec theParsedSpec = ParserCaller.getInstance().loadIDL(idlName);

		if (theParsedSpec == null)
		{
			throw new RuntimeException("could not parse IDL " + idlName);
		}
		
		interfaces.clear();
		
		theParsedSpec.accept(this);
		
		List<IDLInterface> l = new ArrayList<IDLInterface>(interfaces);
		
		return l;
	}

	@Override
	public void visitAlias(AliasTypeSpec arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitConstrTypeSpec(ConstrTypeSpec arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDeclaration(Declaration arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitDefinitions(Definitions defs)
	{
		Enumeration e = defs.getElements();
		while (e.hasMoreElements())
		{
			IdlSymbol s = (IdlSymbol) e.nextElement();
			s.accept(this);
		}
	}



	@Override
	public void visitDefinition(Definition def)
	{
		def.get_declaration().accept(this);
	}

	@Override
	public void visitEnum(EnumType arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitInterface(Interface interfce)
	{
		if (interfce.body == null)
			return;

		// list super interfaces
		String[] superInts = interfce.get_ids();

		for (int i = 1; i < superInts.length; i++)
		{
			// skip index 0, which contains the current interface id

		}
		
		String iname = interfce.name();
		String id = interfce.id();
		String p = interfce.pack_name+"."+iname;
		p = p.replace(".", "::");

		IDLInterface ifc = new IDLInterface(id);

		Operation[] ops = interfce.body.getMethods();


		for (int i = 0; i < ops.length; i++)
		{
			String name = ops[i].name();
			
			IDLOperation idlOperation = new IDLOperation(ifc, name);
			
			ifc.addOperation(idlOperation);
		}
		
		interfaces.add(ifc);
	}


	@Override
	public void visitInterfaceBody(InterfaceBody body)
	{
	}

	@Override
	public void visitMethod(Method arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitModule(Module module)
	{
		module.getDefinitions().accept(this);
	}

	@Override
	public void visitNative(NativeType arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitOpDecl(OpDecl arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitParamDecl(ParamDecl arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSimpleTypeSpec(SimpleTypeSpec arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitSpec(Spec spec)
	{

		Enumeration e = spec.definitions.elements();
		while (e.hasMoreElements())
		{
			IdlSymbol s = (IdlSymbol) e.nextElement();
			s.accept(this);
		}
	}

	@Override
	public void visitStruct(StructType arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitTypeDeclaration(TypeDeclaration arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitTypeDef(TypeDef arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitUnion(UnionType arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitValue(Value arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visitVectorType(VectorType arg0)
	{
		// TODO Auto-generated method stub
		
	}
	
}
