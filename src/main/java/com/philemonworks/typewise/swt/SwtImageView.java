package com.philemonworks.typewise.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.internal.ColorLookup;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtImageProvider;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtImage is
 * 
 * @author E.M.Micklei
 */
public class SwtImageView extends Image implements SwtWidget, MouseListener, PaintListener {
	private static final long serialVersionUID = 3256723991791088691L;
	Canvas swt;
	org.eclipse.swt.graphics.Image imageToPaint = null;
	SwtApplicationView view;
	int reservedWidth = 0;
	int reservedHeight = 0;

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtImageView(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void mouseDoubleClick(MouseEvent arg0) {
		this.mouseUp(arg0);
	}
	public void mouseUp(MouseEvent arg0) {
		view.getMessageDispatcher().dispatch(this.messageForEvent(EVENT_CLICKED));
	}
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = new Canvas(parent, SWT.NORMAL);
		swt.addPaintListener(this);
		SwtUtils.setColors(ColorLookup.backgroundFor(this), ColorLookup.foregroundFor(this), swt, swtView);
		this.updateBounds();
		swt.addMouseListener(this);
	}
	private void updateBounds(){
		Rectangle bounds = view.computeRectangleFor(this,0,0,0,0);
		reservedWidth = bounds.width;
		reservedHeight = bounds.height;
		swt.setLocation(bounds.x,bounds.y);
	}
	public void mouseDown(MouseEvent arg0) {
	}
	public void setUrl(String newURL) {
		super.setUrl(newURL);
		this.updateImageControlContents();
	}
	/**
	 * 
	 */
	private void updateImageControlContents(){
		org.eclipse.swt.graphics.Image image = SwtImageProvider.get(this.getUrl(),view.getDisplay());
		if (image != null) {
			int scaledWidth = 0, scaledHeight = 0;
			ImageData imageData = image.getImageData();
			if (this.isScaleToFit()) {				
				while (imageData.width == 0);
				float xScale = reservedWidth * 1.0f / imageData.width;
				float yScale = reservedHeight * 1.0f / imageData.height;
				// maintain aspect ratio
				if (xScale < yScale){
					scaledWidth = reservedWidth;
					scaledHeight = Math.round(reservedHeight * xScale);
				} else {
					scaledWidth = Math.round(reservedWidth * yScale);
					scaledHeight = reservedHeight;
				}
				 image = new org.eclipse.swt.graphics.Image(view.getShell().getDisplay(),imageData.scaledTo(scaledWidth,scaledHeight));
			} else {
				scaledWidth = imageData.width;
				scaledHeight = imageData.height;
			}
			swt.setBounds(swt.getLocation().x,swt.getLocation().y,scaledWidth,scaledHeight);
			imageToPaint = image;
			swt.redraw();
		}
	}
	/* (non-Javadoc)
	 * @see com.philemonworks.typewise.swt.client.SwtWidget#updateFont(org.eclipse.swt.graphics.Font)
	 */
	public void updateFont(Font newFont) {		
		this.updateBounds();
		if (swt != null) {
			this.updateImageControlContents();
		}
	}
	public void removeFromView(SwtApplicationView swtView) {
		swt.dispose();
		view = null;		
	}
	public void paintControl(PaintEvent arg0) {
		Rectangle clientArea = swt.getClientArea();
		arg0.gc.drawImage(imageToPaint,0,0);
	}	
}
