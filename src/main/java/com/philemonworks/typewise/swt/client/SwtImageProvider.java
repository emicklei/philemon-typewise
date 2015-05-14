package com.philemonworks.typewise.swt.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * SwtImageProvider is a caching SWT image fetcher
 * 
 * @author E.M.Micklei
 */
public class SwtImageProvider {
	private static Map cachedImages = new HashMap();
	private static String UNLOADABLE = "UNLOADABLE";

	/**
	 * Return the image resource specified by an url.
	 * If the image is not available the try to load it.
	 * If the image could not be loaded then return null
	 * @param urlspec String
	 * @param display swt.Display
	 * @return Image || null
	 */
	public static synchronized Image get(String urlspec, Display display) {
		Object img = cachedImages.get(urlspec);
		if (img == UNLOADABLE) return null;
		if (img != null) return (Image)img;
		// fetch the image
		Logger.getLogger(SwtImageProvider.class).debug("Fetching an image at:"+urlspec);
		org.eclipse.swt.graphics.Image image = null;
		InputStream is = null;
		try {
			URL url = new URL(urlspec);
			is = url.openStream();
			image = new org.eclipse.swt.graphics.Image(display, is);
		} catch (Exception ex) {
			Logger.getLogger(SwtImageProvider.class).error("Unable to fetch:" + urlspec + " because:" + ex.getMessage());
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException ex) {
					Logger.getLogger(SwtImageProvider.class).error("Unable to close:" + is);
				}
		}
		cachedImages.put(urlspec,image);
		return image;
	}
	/**
	 * Empty the cache and dispose all images.
	 */
	public static synchronized void flushImages(){
		Logger.getLogger(SwtImageProvider.class).debug("Flushing SWT Image cache:"+cachedImages.size());
		for (Iterator iter = cachedImages.values().iterator(); iter.hasNext();) {
			Image element = (Image) iter.next();
			element.dispose();
		}
		cachedImages = new HashMap();
	}
}
