import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.lang.Object;
import java.beans.*;
import java.io.*;
import java.applet.*;
import java.net.*;

public class Directions extends JFrame implements ActionListener,ListSelectionListener{
  private String []columns={"From","To"};
  private MapViewer myViewer;
  private ArrayList<Location> Location_List;
  private MyFromTableModel fromModel;
  private MyToTableModel toModel;
  private JTable fromtable;
  private JTable totable;
  private JPanel panelAction;
  private JButton cancel;
  private JButton ok;
  private int fromId;
  private int toId;
  private ArrayList<Path> Path_List;
  private MyPanel myPanel;
  private ArrayList<S_Path> S_List = new ArrayList<S_Path>();
  private ArrayList<Location> U_List; 
  private int List_Index = -1; private double min_Distance = 99999.0; 
  private Location L_temp;
  private Path P_temp;
  
  public Directions(MyPanel _myPanel, ArrayList<Location> _Location_List){
    myPanel = _myPanel;
    setLocationRelativeTo(null);
    Location_List = _Location_List;
    U_List = new ArrayList<Location>(Location_List);
    myViewer = new MapViewer();
    fromModel=new MyFromTableModel(Location_List);
    toModel=new MyToTableModel(Location_List);
    fromtable=new JTable(fromModel);
    totable = new JTable(toModel);
    fromtable.getSelectionModel().addListSelectionListener(this);
    fromtable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    totable.getSelectionModel().addListSelectionListener(this);
    totable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    JScrollPane scrollfrom = new JScrollPane(fromtable); 
    scrollfrom.setPreferredSize(new Dimension(225,150));
    JScrollPane scrollto = new JScrollPane(totable); 
    scrollto.setPreferredSize(new Dimension(225, 150));
    setLayout(new GridBagLayout());
    panelAction = new JPanel();
    cancel = new JButton("Cancel");
    cancel.addActionListener(this);
    ok = new JButton("Ok");
    ok.addActionListener(this);
    panelAction.add(cancel);
    panelAction.add(ok);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.weightx = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 0;
    add(scrollfrom, gbc);
    gbc.gridx = 1;
    add(scrollto, gbc);
    gbc.gridx = 1;
    gbc.gridy++;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weighty = 0;
    add(panelAction, gbc);
    fromtable.getSelectionModel().addListSelectionListener(this);
    pack();
    setVisible(true);
  }
  
  public void valueChanged(ListSelectionEvent e){
  }
  
  public void actionPerformed(ActionEvent e){
    if(e.getSource().equals(ok))
    {
      fromId = fromtable.getSelectedRow();
      toId = totable.getSelectedRow();
      if(fromId!=toId && fromId>=0 && toId>=0)
        find_ShortestPath(U_List.get(fromId), U_List.get(toId));
      myViewer.set_fromID(fromId);
      myViewer.set_toID(toId);
      myPanel.repaint();
      dispose();
    }
    else if(e.getSource().equals(cancel))
    {
      dispose();
    }
  }
  
  public void find_ShortestPath(Location origin, Location destination){
    for(int i = 0;i<Location_List.size()&&Location_List !=null;i++){
      if(i == 0) {
        S_List.add(new S_Path(0,new ArrayList<Path>(),origin,null));
        U_List.remove(origin);    
      }
      else{
        calculate_Shortest();
        if(List_Index>=0){
          S_List.add(new S_Path(P_temp.get_Distance()+S_List.get(List_Index).get_Weight(),S_List.get(List_Index).get_Path_List(),L_temp,P_temp));
          U_List.remove(L_temp);
          if(L_temp.get_Id() == destination.get_Id()){
            for(int j = 0;j<S_List.get(S_List.size()-1).get_Path_List().size()&&S_List.get(S_List.size()-1).get_Path_List()!=null;j++){
              S_List.get(S_List.size()-1).get_Path_List().get(j).change_Red();
            }
            return;
          }
          List_Index = -1; min_Distance = 99999.0; L_temp = null;
        }
      }
    }
    JOptionPane.showMessageDialog(null,"No Such Path Exists!","Error",JOptionPane.WARNING_MESSAGE);
  }
  
  public void calculate_Shortest(){
    for(int i = 0; i<S_List.size()&&S_List!=null;i++){
      Location temp = S_List.get(i).get_Port();
      for(int j =0; j<temp.get_Connection().size()&&temp.get_Connection()!=null;j++){
        if(temp.get_Connection().get(j)!=S_List.get(i).get_Last_Path()){
          for(int z=0; z<U_List.size()&&U_List!=null;z++){
            if(temp.get_Connection().get(j).get_Destination()==U_List.get(z)||
               temp.get_Connection().get(j).get_Origin()==U_List.get(z)){
              if(min_Distance>temp.get_Connection().get(j).get_Distance()+S_List.get(i).get_Weight()){
                List_Index = i;
                L_temp = U_List.get(z);
                P_temp = temp.get_Connection().get(j);
                min_Distance = S_List.get(i).get_Weight()+P_temp.get_Distance();
              }
            }
          }
        }
      }
    }
  } 
  
  public void set_PathList(ArrayList<Path> _Path_List){
    Path_List = _Path_List;
  }
  
  public void set_LocationList(ArrayList<Location> _Location_List){
    Location_List = _Location_List;
  }
}

class MyFromTableModel extends AbstractTableModel{
  private String []columnNames={"From"};
  private ArrayList<Location> Location_List;
  
  public MyFromTableModel(ArrayList<Location> _Location_List){
    Location_List = _Location_List;
  }
  
  public String getColumnName(int column) {
    return columnNames[column];
  }
  
  public int getColumnCount() {
    return 1;
  }
  
  public int getRowCount() {
    return Location_List.size();
  }
  
  public Object getValueAt(int rowIndex, int columnIndex) {
    String temp = Integer.toString(Location_List.get(rowIndex).get_Id())+"  "+Location_List.get(rowIndex).get_Name();
    return temp;
  }
}

class MyToTableModel extends AbstractTableModel{
  private String []columnNames={"To"};
  private ArrayList<Location> Location_List;
  
  public MyToTableModel(ArrayList<Location> _Location_List){
    Location_List = _Location_List;
  }
  
  public String getColumnName(int column) {
    return columnNames[column];
  }
  
  public int getColumnCount() {
    return 1;
  }
  
  public int getRowCount() {
    return Location_List.size();
  }
  
  public Object getValueAt(int rowIndex, int columnIndex) {
    String temp = Integer.toString(Location_List.get(rowIndex).get_Id())+"  "+Location_List.get(rowIndex).get_Name();
    return temp;
  }
}
