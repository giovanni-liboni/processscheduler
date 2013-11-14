/*
 * This file is a part of processscheduler.
 * Copyright (C) 2013  Giovanni Liboni

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package it.privato.Giovanni;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

public class Table extends JPanel implements ListSelectionListener {

	public static MyTableModel model;
	JTable table;
	
	public Table(){
		 super(new GridLayout(1,0));
		 model = new MyTableModel(null);
		 table = new JTable(model);
	     table.setFillsViewportHeight(true);
	        JScrollPane scrollPane = new JScrollPane(table);
	        
	        //Add the scroll pane to this panel.
	        add(scrollPane);
		     table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		     ListSelectionModel rowSM = table.getSelectionModel();
		     rowSM.addListSelectionListener(this);
	}
    public static void updateModel(ArrayList<Processo> processi){
        model.updateModel(processi);
}
class MyTableModel extends AbstractTableModel {
    	
    	private ArrayList<String> campi;
    	private ArrayList<Processo> processi;
    
        public MyTableModel( ArrayList<Processo> _studenti )
        {
        	super();
        	campi = new ArrayList<String>();
        	campi.add("Processo");
        	campi.add("Tempo di burst");
        	campi.add("Tempo di arrivo");
        
        	processi = new ArrayList<Processo>();
        	processi = (ArrayList<Processo>) _studenti;
        }

        public int getColumnCount() {
            return campi.size();
        }
 
        public int getRowCount() {
        	if(processi != null)
        		return processi.size();
        	else
        		return 0;
        }
 
        public String getColumnName(int col) {
            return campi.get(col);
        }
 
        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }
        public void updateModel(ArrayList<Processo> _studenti){
         if(this.processi != null)
        	 this.processi.clear();
		else
			this.processi = new ArrayList<Processo>();
         if(_studenti != null)
             for (Processo studente: _studenti )
                 this.processi.add(studente);
         fireTableDataChanged();
        }
		@Override
		public Object getValueAt(int row, int col) {
			
			// bisogna aggiornare qui se si aggiunge qualcosa all'interno dei dati
			switch(col)
			{
			case 0: return processi.get(row).nome;
			case 1: return processi.get(row).burst;
			case 2: return processi.get(row).tempoArrivo;
			default: return "";
			}
		}
		public void addRow(Processo stud){
			processi.add(stud);
			fireTableRowsInserted(processi.size() - 1, processi.size() - 1);
		}
     }
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		Finestra.indexTable = table.getSelectedRow();
	}

}
