package menjacnica.gui;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import menjacnica.Currency;
import menjacnica.ExchangeOffice;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.awt.event.ActionEvent;

public class Menjacnica extends JFrame {

	private JPanel contentPane;
	private JLabel lblIzValuteZemlje;
	private JLabel lblUValutuZemlje;
	private JComboBox comboBoxIz;
	private JLabel lblIznos;
	private JTextField textField;
	private JComboBox comboBoxU;
	private JLabel lblIznos_1;
	private JTextField textField_1;
	private JButton btnKonvertuj;
	private ArrayList<Currency> valute;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menjacnica frame = new Menjacnica();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws Exception 
	 */
	public Menjacnica() throws Exception {
		valute=ExchangeOffice.vratiValute();
		setTitle("Menjacnica");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLblIzValuteZemlje());
		contentPane.add(getLblUValutuZemlje());
		contentPane.add(getComboBoxIz());
		contentPane.add(getLblIznos());
		contentPane.add(getTextField());
		contentPane.add(getComboBoxU());
		contentPane.add(getLblIznos_1());
		contentPane.add(getTextField_1());
		contentPane.add(getBtnKonvertuj());
	}
	private JLabel getLblIzValuteZemlje() {
		if (lblIzValuteZemlje == null) {
			lblIzValuteZemlje = new JLabel("Iz valute zemlje:");
			lblIzValuteZemlje.setBounds(44, 38, 117, 20);
		}
		return lblIzValuteZemlje;
	}
	private JLabel getLblUValutuZemlje() {
		if (lblUValutuZemlje == null) {
			lblUValutuZemlje = new JLabel("U valutu zemlje:");
			lblUValutuZemlje.setBounds(242, 38, 127, 20);
		}
		return lblUValutuZemlje;
	}
	private JComboBox getComboBoxIz() {
		if (comboBoxIz == null) {
			comboBoxIz = new JComboBox(valute.toArray());
			comboBoxIz.setBounds(44, 69, 117, 26);
		}
		return comboBoxIz;
	}
	private JLabel getLblIznos() {
		if (lblIznos == null) {
			lblIznos = new JLabel("Iznos:");
			lblIznos.setBounds(44, 111, 117, 20);
		}
		return lblIznos;
	}
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setBounds(44, 146, 146, 26);
			textField.setColumns(10);
		}
		return textField;
	}
	private JComboBox getComboBoxU() {
		if (comboBoxU == null) {
			comboBoxU = new JComboBox(valute.toArray());
			comboBoxU.setBounds(241, 69, 128, 26);
		}
		return comboBoxU;
	}
	private JLabel getLblIznos_1() {
		if (lblIznos_1 == null) {
			lblIznos_1 = new JLabel("Iznos: ");
			lblIznos_1.setBounds(242, 111, 69, 20);
		}
		return lblIznos_1;
	}
	private JTextField getTextField_1() {
		if (textField_1 == null) {
			textField_1 = new JTextField();
			textField_1.setBounds(242, 146, 146, 26);
			textField_1.setColumns(10);
		}
		return textField_1;
	}
	private JButton getBtnKonvertuj(){
		if (btnKonvertuj == null) {
			btnKonvertuj = new JButton("Konvertuj");
			btnKonvertuj.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int iz = comboBoxIz.getSelectedIndex();
					int u = comboBoxU.getSelectedIndex();
					String val1 = valute.get(iz).getCurrencyID();
					String val2 = valute.get(u).getCurrencyID();
					
					
					double kurs=0.0;
					try {
						Double iznosIz = Double.parseDouble(textField.getText());
						kurs=ExchangeOffice.vratiKurs(val1, val2);
						textField_1.setText(""+kurs* iznosIz);
					} 
					 catch (NumberFormatException e1) {
							JOptionPane.showMessageDialog(null, "Niste uneli iznos koji zelite da konvertujete.",
							"", JOptionPane.INFORMATION_MESSAGE);
					}catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Ne postoje podaci o konverziji izmedju datih valuta.",
								"Greska", JOptionPane.ERROR_MESSAGE);
					}
					finally {
							ExchangeOffice.serijalizacija(val1, val2, kurs);
						
					}
					
					
					
				}
			});
			btnKonvertuj.setBounds(156, 199, 115, 29);
		}
		return btnKonvertuj;
	}
}
