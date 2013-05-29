/*******************************************************************************
 * Copyright (c) 2013 Olaf Lessenich.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Olaf Lessenich - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package de.fosd.jdime.common;

/**
 * @author Olaf Lessenich
 *
 */
public class UnsupportedMergeTypeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4562992040545178701L;
	
	/**
	 * 
	 */
	public UnsupportedMergeTypeException() {
	}
	
	/**
	 * @param message 
	 */
	public UnsupportedMergeTypeException(final String message) {
		super(message);
	}

}
