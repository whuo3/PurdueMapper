import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
public class NewDialog extends JDialog implements ActionListener {
  private JPanel panel = null;
  private JTextField nameField = null;
  private JTextField scaleField = null;
  private JButton ok = null;
  private JButton cancel = null;
  private JLabel nameLabel = null;
  private JLabel scaleLabel = null;
  private ArrayList<Location> Location_List = new ArrayList<Location>();
  private ArrayList<Path> Path_List = new ArrayList<Path>();
  private MyPanel myPanel;
  private String imageName;
  private String scale;
  
  public NewDialog(MyPanel _panel){
    setSize(100,100);
    setLocationRelativeTo(null);
    myPanel=_panel;
    panel = new JPanel(new GridLayout(3,2,5,5));
    getContentPane().add(panel);
    nameLabel = new JLabel("\t\tName : ");
    nameField = new JTextField(15);
    nameField.setEditable(true);
    nameField.addActionListener(this);
    scaleLabel = new JLabel("\t\tScale : ");
    scaleField = new JTextField(15);
    cancel = new JButton("Cancel");
    cancel.addActionListener(this);
    ok = new JButton("Ok");
    ok.addActionListener(this);
    panel.add(nameLabel);
    panel.add(nameField);
    panel.add(scaleLabel);
    panel.add(scaleField);
    panel.add(cancel);
    panel.add(ok);
    setVisible(true);
    pack();
  }
  
  public void actionPerformed(ActionEvent e){
    if(e.getSource().equals(ok)){
      imageName = nameField.getText();
      scale = scaleField.getText();
      if(imageName.isEmpty() || scale.isEmpty()){
        JOptionPane.showMessageDialog(null,"The image name or scale wasn't entered!","Warning!",JOptionPane.WARNING_MESSAGE);
      }
      myPanel.setbackGround_Image(imageName);
      myPanel.set_Scale(Double.parseDouble(scale));
      dispose();
    }
    else if(e.getSource().equals(cancel)){
      dispose();
    }
    myPanel.repaint();
  }
}










