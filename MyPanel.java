import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;
import java.awt.image.*;
class MyPanel extends JPanel implements ChangeListener{
  private ArrayList<Path> Path_List;
  private ArrayList<Location> Location_List;
  private Image backGround_Image;
  private Image train_Image;
  private JScrollPane scrollPane;
  private String backGround_ImageName="purdue-map.jpg";
  private double _scale = 1.0;
  private JSlider _zoom = new JSlider(1,36,16);
  
  public MyPanel(ArrayList<Location> _List, ArrayList<Path> _Path) {
    train_Image = new ImageIcon("Train.jpg").getImage();
    setbackGround_Image(backGround_ImageName);
    Location_List = _List;
    Path_List = _Path;
    _zoom.setMajorTickSpacing(5);
    _zoom.setMinorTickSpacing(1);
    _zoom.setPaintTicks(true);
    _zoom.setSnapToTicks(true);
    _zoom.setLabelTable(getLabelTable(1,36, 5));
    _zoom.setPaintLabels(true);
    _zoom.addChangeListener(this);
  }
  
  public JSlider get_JSlider(){
    return _zoom;
  }
  
  private Hashtable getLabelTable(int min, int max, int inc) {
    Hashtable<Integer,JLabel> table = new Hashtable<Integer,JLabel>();
    for (int j = min; j <= max; j += inc) {
      String s = String.format("%.2f", (j+4)/20.0);
      table.put(Integer.valueOf(j), new JLabel(s));
    }
    return table;
  }
  
  public void paint( Graphics g){
    super.paint( g );
    Graphics2D g2 = (Graphics2D) g;
    Font font =new Font("Arial",Font.ITALIC,7);
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2.setStroke(new BasicStroke(1));
    g2.setColor(Color.GREEN);
    g2.scale(_scale, _scale);
    g2.setFont(font);
    g2.drawImage(backGround_Image, 0, 0, null);
    for(int i=0; Location_List !=null&&i<Location_List.size();i++){
      g2.drawImage(train_Image,Location_List.get(i).getX()-(int)(Location_List.get(i).get_Size()/2),Location_List.get(i).getY()-(int)(Location_List.get(i).get_Size()/2),Location_List.get(i).get_Size(),Location_List.get(i).get_Size(),null);
    }
    for(int i=0; Path_List !=null&&i<Path_List.size();i++){
      if(Path_List.get(i).get_Color()==1){ 
        g2.setColor(Color.BLUE);
        String temp = Integer.toString((int)Path_List.get(i).get_Distance())+"ft.";
        g2.drawString(temp,(Path_List.get(i).get_Origin().getX()+Path_List.get(i).get_Destination().getX())/2,(Path_List.get(i).get_Origin().getY()+Path_List.get(i).get_Destination().getY())/2);
      }
      g2.drawLine(Path_List.get(i).get_Origin().getX(),Path_List.get(i).get_Origin().getY(),Path_List.get(i).get_Destination().getX(),Path_List.get(i).get_Destination().getY());
      if(Path_List.get(i).get_Color()==1){ 
        g2.setColor(Color.GREEN);
      }
    }
  }
  
  public Dimension getPreferredSize() {
    int w = 0, h = 0;
    if (this != null) {
      w = (int)(_scale*backGround_Image.getWidth(null));
      h = (int)(_scale*backGround_Image.getHeight(null));
    }
    return new Dimension(w, h);
  }
  
  public void stateChanged(ChangeEvent e){
    double scale = (_zoom.getValue() + 4) / 20.0;
    zoom(scale);
    repaint();
  }
  
  public void set_ScrollPane(JScrollPane _scrollPane){
    scrollPane = _scrollPane;
  }
  
  public void zoom(double scale) {
    double oldScale = _scale;
    Point point = scrollPane.getViewport().getViewPosition();
    _scale = scale;
    revalidate(); 
    repaint();
    double w = scrollPane.getViewport().getWidth();
    double h = scrollPane.getViewport().getHeight();
    int halfW = (int) (w/2.0);
    int halfH = (int) (h/2.0);
    point.translate(halfW, halfH);
    double relScale = scale / oldScale;
    point.move((int) (point.getX() * relScale), (int) (point.getY() * relScale));
    point.translate(-halfW, -halfH);
    scrollPane.getViewport().setViewPosition(point);
  }
  
  public double get_Scale(){
    return _scale;
  }
  
  public void set_Scale(double s){
    _scale=s;
  }
  
  public String get_backGroundImageName(){
    return backGround_ImageName;
  }
  public void set_backGroundImageName(String _imageName){
    backGround_ImageName = _imageName;
  }
  
  public void setbackGround_Image(String name){
  backGround_Image = new ImageIcon(name).getImage();
 }
  
}
