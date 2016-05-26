import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomDialog extends JDialog implements ActionListener {
  private JPanel myPanel = null;
  private JTextField name_Field = null;
  private JLabel id_label = null;
  private JLabel namel = null;
  private JLabel idl = null;
  private JLabel locl = null;
  private JLabel loc_label = null;
  private JButton ok = null;
  private JButton cancel = null;
  private String name;
  private Location location;
  private int map_X;
  private int map_Y;
  public CustomDialog(Location _location)
  {
    location = _location;
    setSize(100,100);
    setLocationRelativeTo(null);
    myPanel = new JPanel(new GridLayout(6,2,3,7));
    getContentPane().add(myPanel);
    namel=new JLabel("          Name : ");
    name_Field=new JTextField(_location.get_Name(),20);
    name_Field.setEditable(true);
    name_Field.addActionListener(this);
    idl=new JLabel("          ID : ");
    id_label=new JLabel(Integer.toString(_location.get_Id()));
    locl=new JLabel("          X,Y :");;
    loc_label=new JLabel(Integer.toString(_location.getX())+" , "+Integer.toString(_location.getY()));
    cancel=new JButton("Cancel");
    cancel.addActionListener(this);
    ok=new JButton("Ok");
    ok.addActionListener(this);
    myPanel.add(new JLabel());
    myPanel.add(new JLabel());
    myPanel.add(namel);
    myPanel.add(name_Field);
    myPanel.add(idl);
    myPanel.add(id_label);
    myPanel.add(locl);
    myPanel.add(loc_label);
    myPanel.add(cancel);
    myPanel.add(ok);
    setVisible(true);
    pack();
  }
  
  public void actionPerformed(ActionEvent e){
    if(e.getSource().equals(ok))
    {
      name=name_Field.getText();
      if(name.isEmpty()){
        JOptionPane.showMessageDialog(null,"No Name Entered.","Warning!",JOptionPane.WARNING_MESSAGE);
      }
      location.set_Name(name_Field.getText());
      dispose();
    }
    else if(e.getSource().equals(cancel))
    {
      dispose();
    }
  }
}    




