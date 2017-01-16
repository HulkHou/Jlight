package com.lew.jlight.core.util;

import java.io.Closeable;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOResourceUtil {

	private static final Logger logger = LoggerFactory.getLogger( IOResourceUtil.class );

	public static void closeResource( Closeable stream ) {
		if ( stream == null ) {
			return;
		}
		try {
			stream.close( );
		} catch ( Exception e ) {
			if ( logger.isErrorEnabled( ) ) {
				logger.error( "close stream error!" , e );
			}
		}
	}
	
	/**
	 * 获取Classpath路径
	 * 
	 * @return
	 */
	public static String getClasspath( ) {
		String classPath = Thread.currentThread( ).getContextClassLoader( ).getResource( "" ).getPath( );
		String rootPath = "";
		// windows
		if ( "\\".equals( File.separator ) ) {
			rootPath = classPath.substring( 1 );
			rootPath = rootPath.replace( "/", "\\" );
		}
		// linux
		if ( "/".equals( File.separator ) ) {
			rootPath = classPath.substring( 1 );
			rootPath = rootPath.replace( "\\", "/" );
		}
		return rootPath;
	}

}
