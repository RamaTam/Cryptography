
import javax.swing.table.AbstractTableModel;

public class CharTableModel extends AbstractTableModel {

    private char [][] data;
    private char[] headers;
    private int rows;
    private int cols;
    public CharTableModel(char[][] data, char[] headers){

       this.data=data;
       this.headers=headers;
        rows = data.length;
        if(rows > 0)
            cols = data[0].length;
        else
            cols=0;
    }

    @Override
    public String getColumnName(int column) {
        if(headers[column]==0){
            return " ";
        }
        else return "" +headers[column];
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return cols;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        char result;
         String input = ((String)aValue);
        if(aValue!=null && !input.isEmpty())
            result = input.charAt(input.length()-1);
        else
            result =  Character.SPACE_SEPARATOR;

        if(result==0)
            result = Character.SPACE_SEPARATOR;

        data[rowIndex][columnIndex] = result;    //To change body of overridden methods use File | Settings | File Templates.

        //fireTableDataChanged();
        fireTableStructureChanged();
        Week1ProgrammingAssignment.changeTable(data[rowIndex][columnIndex], rowIndex, columnIndex);   //noob way;p
    }


    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }


}
