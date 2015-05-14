/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) copyright PhilemonWorks B.V. 2005 - All rights reserved.
 * Use, duplication or disclosure restricted. 
 * See http://www.philemonworks.com
 * 
 * 2004-02-02: created by emicklei
 * 
 * */
package com.philemonworks.typewise.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.internal.Widget;

/**
 * This class can be used to preview Terminal Widgets
 */
public class Previewer extends java.awt.Frame implements java.awt.event.MouseListener,
		java.awt.event.MouseMotionListener {
	private static final long serialVersionUID = 3257009843339538741L;
	Widget widget = null;
	WidgetRenderer renderer = new WidgetRenderer();
	boolean showGrid = false;
	Font currentFont;
	int zoom = 1;
	static int screenMarginX = 10;
	static int screenMarginY = 30;
	private Widget lastUnderMouse= null;

	public static Color toAWT(com.philemonworks.util.Color cwtColor){
		return new Color(cwtColor.redValue,cwtColor.blueValue,cwtColor.greenValue);
	}
	/**
	 * Previewer constructor comment.
	 */
	public Previewer() {
		super();
		initialize();
	}
	/**
	 * Previewer constructor comment.
	 * 
	 * @param title
	 *        java.lang.String
	 */
	public Previewer(String title) {
		super(title);
	}
	public void displayGridOn(java.awt.Graphics g) {
		g.setColor(java.awt.Color.gray);
		this.displayGridOn(g, this.getWidth(), this.getHeight());
		g.setColor(java.awt.Color.white);
		this.displayGridOn(g, renderer.screenoffsetx + widget.getColumns() * renderer.charwidth, renderer.screenoffsety
				+ widget.getRows() * renderer.charheight);
	}
	public void displayGridOn(java.awt.Graphics g, int width, int height) {
		for (int x = renderer.screenoffsetx; x <= width; x = x + renderer.charwidth) {
			g.drawLine(x, renderer.screenoffsety, x, height);
		}
		for (int y = renderer.screenoffsety; y <= height; y = y + renderer.charheight) {
			g.drawLine(renderer.screenoffsetx, y, width, y);
		}
	}
	public String getAppName() {
		return "Previewer";
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception
	 *        java.lang.Throwable
	 */
	private void handleException(Throwable exception) {
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		exception.printStackTrace(System.out);
	}
	private void initialize() {
		try {
			setName("Previewer");
			setSize(220, renderer.screenoffsety + 220);
			setBackground(java.awt.Color.black);
			setTitle(this.getAppName());
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}
	public static Previewer show(Screen aScreen) {
		Previewer viewer = null;
		try {
			viewer = new Previewer();
			viewer.addMouseMotionListener(viewer);
			viewer.addMouseListener(viewer);
			viewer.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			viewer.widget = aScreen;
			viewer.zoomChanged();
			if (aScreen.getModel() != null)
				aScreen.getModel().view.show(aScreen);
			viewer.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of Previewer");
			exception.printStackTrace(System.out);
		}
		return viewer;
	}
	public Dimension computeDimension() {
		int width = renderer.charwidth * widget.getColumns();
		int height = renderer.charheight * widget.getRows();
		// add space for titlebar and borders
		height = height + screenMarginY;
		width = width + screenMarginX;
		return new Dimension(width, height);
	}
	public void zoomChanged() {
		if (zoom == 1) {
			currentFont = Font.decode("courier-bold-12");
			renderer.initialize(7, 12, 24, 4, 1, -2);
		} else if (zoom == 2) {
			currentFont = Font.decode("courier-bold-14");
			renderer.initialize(8, 14, 24, 4, 1, -2);
		} else if (zoom == 3) {
			currentFont = Font.decode("courier-bold-16");
			renderer.initialize(10, 16, 24, 4, 0, -2);
		} else if (zoom == 4) {
			currentFont = Font.decode("courier-bold-18");
			renderer.initialize(11, 18, 24, 4, 0, -3);
		}
		this.setSize(this.computeDimension());
	}
	public void mouseClicked(java.awt.event.MouseEvent e) {
		// System.out.println(e.getModifiers());
		if (e.getModifiers() == 16) {
			showGrid = !showGrid;
			this.update(getGraphics());
		} else if (e.getModifiers() == 4) {
			if (++zoom > 4)
				zoom = 1;
			this.zoomChanged();
		}
	}
	/**
	 * Invoked when a mouse button is pressed on a component and then dragged. Mouse drag events will continue to be
	 * delivered to the component where the first originated until the mouse button is released (regardless of whether
	 * the mouse position is within the bounds of the component).
	 */
	public void mouseDragged(java.awt.event.MouseEvent e) {
	}
	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(java.awt.event.MouseEvent e) {
	}
	/**
	 * Invoked when the mouse exits a component.
	 */
	public void mouseExited(java.awt.event.MouseEvent e) {
	}
	/**
	 * Invoked when the mouse button has been moved on a component (with no buttons no down).
	 */
	public void mouseMoved(java.awt.event.MouseEvent e) {
		int row = (e.getPoint().y - renderer.screenoffsety) / renderer.charheight + 1;
		int column = (e.getPoint().x - renderer.screenoffsetx) / renderer.charwidth + 1;
		StringBuffer buffer = new StringBuffer();
		buffer.append(getAppName());
		if (widget instanceof Screen) {
			buffer.append(" | ");
			buffer.append(((Screen) widget).getTitle());
		}
		buffer.append(" | row.");
		buffer.append(row);
		buffer.append(" column.");
		buffer.append(column);
		// Find widget
		Widget which = widget.widgetUnderPoint(row, column);
		if (lastUnderMouse == which) return;
		lastUnderMouse = which;
		buffer.append(" | ");
		if (which == null) {
			buffer.append("<outside screen>");
		} else {
			buffer.append(which);
			Graphics g = this.getGraphics();
			g.setFont(currentFont);
			renderer.setGraphics(g);
			which.kindDo(renderer);
			if (showGrid)
				this.displayGridOn(g);
			if (which != widget) {
				g.setColor(Color.RED);
				renderer.drawBorder(which);
			}
		}
		this.setTitle(buffer.toString());
	}
	/**
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(java.awt.event.MouseEvent e) {
	}
	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(java.awt.event.MouseEvent e) {
	}
	public void paint(java.awt.Graphics g) {
		g.setFont(currentFont);
		renderer.setGraphics(g);
		widget.kindDo(renderer);
		if (showGrid)
			this.displayGridOn(g);
	}
}
