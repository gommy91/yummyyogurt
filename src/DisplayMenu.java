import java.sql.*;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.DefaultTableCellRenderer;

public final class DisplayMenu extends JFrame implements ActionListener, ItemListener, ChangeListener, MouseListener {
	// table
	private JScrollPane scrollPane = new JScrollPane();
	private JTable table = new JTable();
	private DefaultTableModel dtm = new DefaultTableModel(new Object[]{"SellID", "BuyerName", "YogurtType", "Size", "Topping", "Qty", "TotalPrice"}, 0);
	
	// button
	private JButton btnInsert = new JButton("Insert");
	private JButton btnUpdate = new JButton("Update");
	private JButton btnDelete = new JButton("Delete");
	private JButton btnReset = new JButton("Reset");

	// label
	private JLabel lblSellID = new JLabel("Sell ID",JLabel.CENTER);
	private JLabel lblBuyerName = new JLabel("Buyer Name",JLabel.CENTER);
	private JLabel lblYogurtType = new JLabel("Yogurt Type",JLabel.CENTER);
	private JLabel lblSize = new JLabel("Size",JLabel.CENTER);
	private JLabel lblTopping = new JLabel("Topping",JLabel.CENTER);
	private JLabel lblQty = new JLabel("Qty",JLabel.CENTER);
	private JLabel lblTotalPrice = new JLabel("Total Price",JLabel.CENTER);

	//inputfield
	private JTextField txtSellID = new JTextField();
	private JTextField txtBuyerName = new JTextField();
	private JComboBox cbYogurtType = new JComboBox(new String[]{"-", "Standard", "Spesial"});
	private JComboBox cbSize = new JComboBox(new String[]{"-", "S", "M", "L"});
	private JCheckBox chkJelly, chkChocolate, chkFruits;
	private JSpinner spinQty = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
	private JTextField txtTotalPrice = new JTextField();

	//panel
	private JPanel panel = new JPanel(new BorderLayout());
	private JPanel chkBox = new JPanel(new FlowLayout());
	private JPanel buttons = new JPanel(new FlowLayout());
	private JPanel form = new JPanel(new GridLayout(7,2,5,5));

	// connection
	private Connect conn = new Connect();
	private ResultSet rs = null;
	private ResultSet rschk = null;

	public DisplayMenu() {
		createForm();
		
		chkBox.add(chkJelly = new JCheckBox("Jelly"));
		chkBox.add(chkChocolate = new JCheckBox("Chocolate"));
		chkBox.add(chkFruits = new JCheckBox("Fruits"));
		
		form.add(lblSellID);form.add(txtSellID);txtSellID.setEditable(false);
		form.add(lblBuyerName);form.add(txtBuyerName);
		form.add(lblYogurtType);form.add(cbYogurtType);
		form.add(lblSize);form.add(cbSize);
		form.add(lblTopping);form.add(chkBox);
		form.add(lblQty);form.add(spinQty);
		form.add(lblTotalPrice);form.add(txtTotalPrice);txtTotalPrice.setEditable(false);
		
		buttons.add(btnInsert);
		buttons.add(btnUpdate);
		buttons.add(btnDelete);
		buttons.add(btnReset);
		
		panel.add(form, BorderLayout.CENTER);
		panel.add(buttons, BorderLayout.SOUTH);
		
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
		
		btnInsert.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDelete.addActionListener(this);
		btnReset.addActionListener(this);
		cbYogurtType.addActionListener(this);
		cbSize.addActionListener(this);
		chkJelly.addItemListener(this);
		chkChocolate.addItemListener(this);
		chkFruits.addItemListener(this);
		spinQty.addChangeListener(this);
		table.addMouseListener(this);
		
		scrollPane.setViewportView(table);
		table.setModel(dtm);
		initializeTableDesign(table);
		
		refreshData();

		this.setVisible(true);
	}
	
	public void refreshData() {
		int x = dtm.getRowCount();
		for(int i=0;i<x;i++){
			dtm.removeRow(0);
		}
		rs = conn.executeQuery("SELECT * FROM Yogurt");
		Vector<String> vec = new Vector<String>();
		try {
			while (rs.next()) {
				vec = new Vector<String>();
				vec.add(rs.getString(1));
				vec.add(rs.getString(2));
				vec.add(rs.getString(3));
				vec.add(rs.getString(4));
				vec.add(rs.getString(5));
				vec.add(rs.getString(6));
				vec.add(rs.getString(7));
				dtm.addRow(vec);
			}
		} catch (Exception ex) {}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnInsert){
			if(table.getSelectedRow() == -1){
				int confirm = JOptionPane.showConfirmDialog (null, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(confirm == JOptionPane.YES_OPTION){
					boolean validation = doValidation();
					if(validation == true) {
						String SellID = txtSellID.getText();
						String BuyerName = txtBuyerName.getText();
						String YogurtType = cbYogurtType.getSelectedItem().toString();
						String Size = cbSize.getSelectedItem().toString();
						String Topping = generateToppingTxt(chkJelly.isSelected(), chkChocolate.isSelected(), chkFruits.isSelected());
						String Qty = spinQty.getValue().toString();
						String TotalPrice = txtTotalPrice.getText();
						conn.executeUpdate("INSERT INTO Yogurt VALUES('"+SellID+"','"+BuyerName+"','"+YogurtType+"','"+Size+"','"+Topping+"','"+Qty+"','"+TotalPrice+"')");
						refreshData();
						JOptionPane.showMessageDialog(null, "Data successfully inserted!");
						doReset();
					}
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Can't insert existing data!");
				doReset();
			}
		}
		
		if(e.getSource() == btnUpdate){
			if(table.getSelectedRow() != -1){
				int confirm = JOptionPane.showConfirmDialog (null, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(confirm == JOptionPane.YES_OPTION){
					boolean validation = doValidation();
					if(validation == true) {
						String SellID = txtSellID.getText();
						String BuyerName = txtBuyerName.getText();
						String YogurtType = cbYogurtType.getSelectedItem().toString();
						String Size = cbSize.getSelectedItem().toString();
						String Topping = generateToppingTxt(chkJelly.isSelected(), chkChocolate.isSelected(), chkFruits.isSelected());
						String Qty = spinQty.getValue().toString();
						String TotalPrice = txtTotalPrice.getText();
						conn.executeUpdate("UPDATE Yogurt SET "
								+ "SellID='"+SellID+"', "
								+ "BuyerName='"+BuyerName+"', "
								+ "YogurtType='"+YogurtType+"', "
								+ "Size='"+Size+"', "
								+ "Topping='"+Topping+"', "
								+ "Qty='"+Qty+"', "
								+ "TotalPrice='"+TotalPrice+"' "
								+ "WHERE SellID='"+table.getValueAt(table.getSelectedRow(), 0)+"'");
						refreshData();
						JOptionPane.showMessageDialog(null, "Data successfully updated!");
						doReset();
					}
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Please choose data!");
			}
		}
		
		if(e.getSource() == btnDelete){
			if(table.getSelectedRow() != -1){
				int confirm = JOptionPane.showConfirmDialog (null, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(confirm == JOptionPane.YES_OPTION){
					String SellID = txtSellID.getText();
					conn.executeUpdate("DELETE FROM Yogurt WHERE SellID='"+SellID+"'");
					refreshData();
					JOptionPane.showMessageDialog(null, "Data successfully deleted!");
					doReset();
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "Please choose data!");
			}
		}
		
		if(e.getSource() == btnReset){
			int confirm = JOptionPane.showConfirmDialog (null, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    	if(confirm == JOptionPane.YES_OPTION) {
	    		doReset();
	    	}
		}
		
		if(e.getSource() == cbYogurtType){
			if(table.getSelectedRow() == -1){
				txtSellID.setText(generateSellID());
			}
			txtTotalPrice.setText(calculateTotalPrice());
		}
		
		if(e.getSource() == cbSize) {
			txtTotalPrice.setText(calculateTotalPrice());
		}
	}
	
    public void itemStateChanged(ItemEvent e) {
    	txtTotalPrice.setText(calculateTotalPrice());
    }
	
    public void stateChanged(ChangeEvent e) {
    	txtTotalPrice.setText(calculateTotalPrice());
    }
    
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==table){
			try{
				txtSellID.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
				txtBuyerName.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
				cbYogurtType.setSelectedItem(table.getValueAt(table.getSelectedRow(), 2).toString());
				cbSize.setSelectedItem(table.getValueAt(table.getSelectedRow(), 3).toString());
				
				List<String> toppingChecker = createListFromString(table.getValueAt(table.getSelectedRow(), 4).toString());
				if(toppingChecker.contains("Jelly")) {
					chkJelly.setSelected(true);
				}
				else {
					chkJelly.setSelected(false);
				}
				
				if(toppingChecker.contains("Chocolate")) {
					chkChocolate.setSelected(true);
				}
				else {
					chkChocolate.setSelected(false);
				}
				
				if(toppingChecker.contains("Fruits")) {
					chkFruits.setSelected(true);
				}
				else {
					chkFruits.setSelected(false);
				}
				
				spinQty.setValue(Integer.valueOf(table.getValueAt(table.getSelectedRow(), 5).toString()));
				txtTotalPrice.setText(table.getValueAt(table.getSelectedRow(), 6).toString());
			} catch (Exception f){}
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	public void initializeTableDesign(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		columnModel.getColumn(0).setPreferredWidth(60);
		columnModel.getColumn(1).setPreferredWidth(130);
		columnModel.getColumn(2).setPreferredWidth(70);
		columnModel.getColumn(3).setPreferredWidth(30);
		columnModel.getColumn(4).setPreferredWidth(175);
		columnModel.getColumn(5).setPreferredWidth(30);
		columnModel.getColumn(6).setPreferredWidth(85);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		columnModel.getColumn(0).setCellRenderer(centerRenderer);
		columnModel.getColumn(2).setCellRenderer(centerRenderer);
		columnModel.getColumn(3).setCellRenderer(centerRenderer);
		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		columnModel.getColumn(5).setCellRenderer(rightRenderer);
		columnModel.getColumn(6).setCellRenderer(rightRenderer);
	}
	
	private void createForm() {
		this.setTitle("Yummy Yogurt");
		this.setSize(615, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
	
	private String generateToppingTxt(boolean jelly, boolean chocolate, boolean fruits) {
		List<String> topping = new ArrayList<String>();
		
		if (jelly) {
			topping.add("Jelly");
		}
		if (chocolate) {
			topping.add("Chocolate");
		}
		if (fruits) {
			topping.add("Fruits");
		}
		
		String result = String.join(", ", topping);
		
		return result;
	}
	
	private List<String> createListFromString(String toppingTxt) {
		List<String> topping = new ArrayList<String>();
		
		if (toppingTxt.contains("Jelly")) {
			topping.add("Jelly");
		}
		if (toppingTxt.contains("Chocolate")) {
			topping.add("Chocolate");
		}
		if (toppingTxt.contains("Fruits")) {
			topping.add("Fruits");
		}
		
		return topping;
	}
	
	public void doReset() {
        txtSellID.setText(null);
        txtBuyerName.setText(null);
        cbYogurtType.setSelectedIndex(0);
        cbSize.setSelectedIndex(0);
    	chkJelly.setSelected(false);
    	chkChocolate.setSelected(false);
    	chkFruits.setSelected(false);
    	spinQty.setValue(Integer.valueOf(1));
    	txtTotalPrice.setText(null);
    	table.clearSelection();
	}
	
	public boolean doValidation() {
		if (txtBuyerName.getText() == "" || txtBuyerName.getText().trim().length() == 0) {
			JOptionPane.showMessageDialog(null, "All fields must be filled in!", "Information", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else if (cbYogurtType.getSelectedItem() == "-") {
			JOptionPane.showMessageDialog(null, "All fields must be filled in!", "Information", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else if (cbSize.getSelectedItem() == "-") {
			JOptionPane.showMessageDialog(null, "All fields must be filled in!", "Information", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	public String generateSellID() {
		String id = new String();
		List<String>usedID = new ArrayList<String>();
		String yogurtType = cbYogurtType.getSelectedItem().toString();
		rschk = conn.executeQuery("SELECT SellID FROM Yogurt");
		
		try {
			while (rschk.next()) {
				String currId = rschk.getString("SellID");
				usedID.add(currId);
			}
		} catch (Exception ex) {}
		
		Collections.sort(usedID);
		Integer i = 1;
		
		if(yogurtType == "Standard") {
			id = "YS001";
			while(usedID.contains(id)) {
				if(i < 10) {
					id = "YS00"+i.toString();
				}
				else if(i < 100 && i >= 10) {
					id = "YS0"+i.toString();
				}
				else {
					id = "YS"+i.toString();
				}
				i++;
			}
		}
		else if(yogurtType == "Spesial") {
			id = "SS001";
			while(usedID.contains(id)) {
				if(i < 10) {
					id = "SS00"+i.toString();
				}
				else if(i < 100 && i >= 10) {
					id = "SS0"+i.toString();
				}
				else {
					id = "SS"+i.toString();
				}
				i++;
			}
		}
		
		return id;
	}
	
	public String calculateTotalPrice() {
		Integer totalPrice = 0;
		
		if(cbYogurtType.getSelectedItem() == "Standard") {
			if(cbSize.getSelectedItem() == "S") {
				totalPrice = 20000;
			}
			else if(cbSize.getSelectedItem() == "M") {
				totalPrice = 30000;
			}
			else if(cbSize.getSelectedItem() == "L") {
				totalPrice = 40000;
			}
			else {
				totalPrice = 0;
			}
		}
		else if(cbYogurtType.getSelectedItem() == "Spesial") {
			if(cbSize.getSelectedItem() == "S") {
				totalPrice = 30000;
			}
			else if(cbSize.getSelectedItem() == "M") {
				totalPrice = 40000;
			}
			else if(cbSize.getSelectedItem() == "L") {
				totalPrice = 50000;
			}
			else {
				totalPrice = 0;
			}
		}
		
		if(chkJelly.isSelected()) {
			totalPrice += 4000;
		}
		
		if(chkChocolate.isSelected()) {
			totalPrice += 4000;
		}
		
		if(chkFruits.isSelected()) {
			totalPrice += 5000;
		}
		
		totalPrice = totalPrice * Integer.valueOf(spinQty.getValue().toString());
		
		return totalPrice.toString();
	}
	
	public static void main(String[] args) {
        new DisplayMenu();
    }
}