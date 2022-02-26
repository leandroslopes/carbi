
package br.com.carbi.tablemodel;

import br.com.carbi.model.Student;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

public class TableModelStudent extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private ArrayList<Student> students;
    private String[] columnsNames = {"MATRÍCULA", "NAME", "CURSO"};

    public TableModelStudent(ArrayList<Student> studentsInitials) {
        students = studentsInitials;
    }

    public int getRowCount() {
        return students.size();
    }

    public int getColumnCount() {
        return columnsNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnsNames[column];
    }

    public Object getValueAt(int indexLine, int indexColumn) {
        String value = "";

        switch (indexColumn) {
            case 0:
                value = String.valueOf(students.get(indexLine).getRegistration());
                break;
            case 1:
                value = students.get(indexLine).getName();
                break;
            case 2:
                value = students.get(indexLine).getCourse();
                break;
        }

        return value;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public Student getStudent(int indexLine) {
        return students.get(indexLine);
    }

    public static void resizeColumns(JTable tableStudents) {
        TableColumnModel tableColumnModel = tableStudents.getColumnModel();
        tableColumnModel.getColumn(0).setPreferredWidth(45);
        tableColumnModel.getColumn(1).setPreferredWidth(230);
        tableColumnModel.getColumn(2).setPreferredWidth(100);
        
        tableColumnModel.getColumn(0).setCellRenderer(new DefaultTableCellRenderer());
        ((DefaultTableCellRenderer) tableColumnModel.getColumn(0).getCellRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        tableColumnModel.getColumn(1).setCellRenderer(new DefaultTableCellRenderer());
        ((DefaultTableCellRenderer) tableColumnModel.getColumn(1).getCellRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        tableColumnModel.getColumn(2).setCellRenderer(new DefaultTableCellRenderer());
        ((DefaultTableCellRenderer) tableColumnModel.getColumn(2).getCellRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
    }
}
