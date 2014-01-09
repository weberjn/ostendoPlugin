/*
 * Copyright 2012 VR Kreditwerk AG
 */

package de.jwi.ostendoplugin.idl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.jacorb.orb.giop.Messages;
import org.jacorb.orb.giop.RequestInputStream;
import org.omg.CORBA.ORB;
import org.omg.GIOP.MsgType_1_1;

/**
 * TODO 01.03.2012, K263335: Dokumentation ergänzen
 * 
 * @author K263335
 * 
 */
public class MessageTypeChecker
{
	public static final int UNDEF = 0;

	public static final int REQUEST = 1;

	public static final int REPLY = 2;

	public static int getMessageType(File f) throws IOException
	{
		if (f.isDirectory())
		{
			return UNDEF;
		}
		
		FileInputStream fis = new FileInputStream(f);

		byte[] b = new byte[12];

		fis.read(b);

		fis.close();

		if (!Messages.matchGIOPMagic(b))
		{
			return UNDEF;
		}

		int type = Messages.getMsgType(b);

		if (type == MsgType_1_1._Request)
		{
			return REQUEST;
		}

		if (type == MsgType_1_1._Reply)
		{
			return REPLY;
		}

		return UNDEF;
	}

	public static String getOperation(ORB orb, File f) throws IOException
	{
		FileInputStream fis = new FileInputStream(f);

		byte[] b = new byte[(int)f.length()];

		fis.read(b);

		fis.close();

		RequestInputStream ris = new RequestInputStream(orb, null, b);

		String operationName = ris.req_hdr.operation;

		return operationName;
	}
}
