//########################################################################
//#
//# � University of Southampton IT Innovation Centre, 2011 
//# Copyright in this library belongs to the University of Southampton 
//# University Road, Highfield, Southampton, UK, SO17 1BJ 
//# This software may not be used, sold, licensed, transferred, copied 
//# or reproduced in whole or in part in any manner or form or in or 
//# on any media by any person other than in accordance with the terms 
//# of the Licence Agreement supplied with the software, or otherwise 
//# without the prior written consent of the copyright owners.
//#
//# This software is distributed WITHOUT ANY WARRANTY, without even the 
//# implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, 
//# except where stated in the Licence Agreement supplied with the software.
//#
//#	Created By :			Mariusz Jacyno
//#	Created Date :			2011-08-05
//#	Created for Project :	ROBUST
//#
//########################################################################

package cs.gui.app;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class JTSeparator extends JPanel {

	private final class SeparatorPane extends JPanel {

		private SeparatorPane() {
			super(new GridBagLayout());
			setOpaque(false);
			setDoubleBuffered(false);
			JSeparator separator = new JSeparator() {
				//@Override

				protected void paintComponent(Graphics g) {
					Graphics2D g2d = (Graphics2D) g;
					Composite composite = g2d.getComposite();
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
					super.paintComponent(g);
					g2d.setComposite(composite);
				}
			};
			add(separator, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		}

		@SuppressWarnings("deprecation")
		@Override
		public void reshape(int x, int y, int w, int h) {
			super.reshape(x, y, w, h);
			doLayout();
		}
	}
	private SeparatorPane westSeparator;
	private JLabel label = new JLabel();

	/**
	 * Construct a separator with a title.
	 * @param title the title to set.
	 */
	public JTSeparator(String title) {
		super(new BorderLayout());
		JPanel westPanel = new JPanel(new BorderLayout()) {

			@SuppressWarnings("deprecation")
			@Override
			public void reshape(int x, int y, int w, int h) {
				super.reshape(x, y, w, h);
				doLayout();
			}
		};
		westPanel.setOpaque(false);
		westPanel.setDoubleBuffered(false);
		westSeparator = new SeparatorPane();
		boolean isLeftToRight = getComponentOrientation().isLeftToRight();
		if (isLeftToRight) {
			westSeparator.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
			westPanel.add(westSeparator, BorderLayout.WEST);
		} else {
			westSeparator.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
			westPanel.add(westSeparator, BorderLayout.EAST);
		}
		setOpaque(false);
		westPanel.add(label, BorderLayout.CENTER);
		if (isLeftToRight) {
			add(westPanel, BorderLayout.WEST);
		} else {
			add(westPanel, BorderLayout.EAST);
		}
		SeparatorPane separatorPane = new SeparatorPane();
		if (isLeftToRight) {
			separatorPane.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
		} else {
			separatorPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
		}
		add(separatorPane, BorderLayout.CENTER);
		setTitle(title);
		adjustLook();
	}

	/**
	 * Get the title of this separator.
	 * @return the title.
	 */
	public String getTitle() {
		return label.getText();
	}

	/**
	 * Set the title of the separator.
	 * @param title the new title.
	 */
	public void setTitle(String title) {
		if (title == null) {
			title = "";
		}
		boolean isVisible = title.length() != 0;
		westSeparator.setVisible(isVisible);
		label.setVisible(isVisible);
		label.setText(title);
	}

	@Override
	public void updateUI() {
		super.updateUI();
		adjustLook();
	}

	private void adjustLook() {
		if (westSeparator != null) {
			westSeparator.setPreferredSize(new Dimension(new JLabel("M").getPreferredSize().width, westSeparator.getPreferredSize().height));
		}
		if (label != null) {
			//Color titleColor = UIManager.getColor("TitledBorder.titleColor");
			Color titleColor = Color.WHITE;
			Font font = UIManager.getFont("TitledBorder.font");
			if (titleColor == null || font == null) {
				TitledBorder titledBorder = new TitledBorder("");
				titleColor = titledBorder.getTitleColor();
				font = titledBorder.getTitleFont();
			}
			label.setForeground(titleColor);
			label.setFont(font);
		}
	}
}
