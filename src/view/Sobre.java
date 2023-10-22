/*
 * The MIT License
 *
 * Copyright 2023 Professor José de Assis.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package view;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.SystemColor;
import java.awt.Font;

public class Sobre extends JDialog {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sobre dialog = new Sobre();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Sobre() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Sobre.class.getResource("/img/instagram.png")));
		setTitle("Sobre o carômetro");
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 376, 205);
		getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("Projeto Carômetro");
		lblNewLabel.setForeground(SystemColor.textHighlight);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel.setBounds(18, 21, 198, 22);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Professor José de Assis");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblNewLabel_1.setBounds(18, 50, 151, 23);
		getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon(Sobre.class.getResource("/img/mit.png")));
		lblNewLabel_3.setBounds(248, 0, 96, 96);
		getContentPane().add(lblNewLabel_3);

		JButton btnGithub = new JButton("");
		btnGithub.setToolTipText("Documentação");
		btnGithub.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				link("https://github.com/professorjosedeassis/carometro");
			}
		});
		btnGithub.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnGithub.setContentAreaFilled(false);
		btnGithub.setBorderPainted(false);
		btnGithub.setIcon(new ImageIcon(Sobre.class.getResource("/img/github.png")));
		btnGithub.setBounds(64, 84, 48, 48);
		getContentPane().add(btnGithub);

		JButton btnOk = new JButton("OK");
		btnOk.setForeground(SystemColor.textHighlight);
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnOk.setBounds(259, 119, 74, 23);
		getContentPane().add(btnOk);

		JButton btnTutorial = new JButton("");
		btnTutorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				link("https://www.youtube.com/playlist?list=PLbEOwbQR9lqz6tSoH51QAEzpo1VsXgvIv");
			}
		});
		btnTutorial.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTutorial.setIcon(new ImageIcon(Sobre.class.getResource("/img/youtube.png")));
		btnTutorial.setToolTipText("Tutorial");
		btnTutorial.setContentAreaFilled(false);
		btnTutorial.setBorderPainted(false);
		btnTutorial.setBounds(15, 84, 48, 48);
		getContentPane().add(btnTutorial);

		this.setLocationRelativeTo(null);
	}

	private void link(String url) {
		Desktop desktop = Desktop.getDesktop();
		try {
			URI uri = new URI(url);
			desktop.browse(uri);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
