/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uoc.myway.utils;

import java.io.File;
import java.io.FileFilter;


/**
 * Filtra tots els arxius amb una/es extensio/ns determinada/es
 * @author ajuhe
 */
public class ExtensionFilter implements FileFilter {
	
	private String[] extensions;
	private boolean accept;
	
	public ExtensionFilter(String[] ext, boolean acc){
		extensions = ext;
		accept = acc;
	}

    public boolean accept(File pathname) {
    	for(String ext : extensions){
    		if(pathname.getName().endsWith(ext)){
    			if(accept){
    				return true;
    			}else{
    				return false;
    			}
    		}
    	}
    	
    	if(accept){
    		return false;
    	}else{
    		return true;
    	}
    }
}
