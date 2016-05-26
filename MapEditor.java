/* CS 251: Project 5
 * Group Members: Alfred Huo                   0025516173
 *                Gurumukh Uttamchandani       0025219228
 */
import java.lang.Object;
import javax.swing.*; 
import javax.swing.event.*; 
import javax.swing.text.*; 
import javax.swing.border.*; 
import javax.swing.colorchooser.*; 
import javax.swing.filechooser.*; 
import javax.accessibility.*; 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.awt.*; 
import java.awt.event.*; 
import java.beans.*; 
import java.util.*; 
import java.io.*; 
import java.applet.*; 
import java.net.*;

class MapEditor extends JFrame implements ActionListener, MouseListener, MouseMotionListener{
  // The preferred size of the demo
  private int PREFERRED_WIDTH = 680;
  private int PREFERRED_HEIGHT = 600;
  private JScrollPane scrollPane;
  private MyPanel myPanel;
  private CustomDialog dialog;
  private NewDialog newDialog;
  /*
   Run_Modes==0: Do Nothing
   Run_Modes==1: Insert Location Mode
   Run_Modes==2: Delete Location Mode
   Run_Modes==3: Insert Path Mode
   Run_Modes==4: Delete Path Mode
   Run_Modes==5: Change Properties
   */
  private int Run_Modes = 0;
  
  //Location when clicking the map
  private int map_X;
  private int map_Y;
  
  //Count the Id number
  private int id_Count = 0;
  //For temp2, just store the location just double clicked, so that we can get the Menu location properties work.
  private Location temp2 = null;
  
  //Initialize GUI
  private JMenuBar bar = new JMenuBar();
  private JMenu Files = new JMenu("Files");
  private JMenu Modes = new JMenu("Modes");
  private JMenuItem Do_Nothing = new JMenuItem("Do Nothing");
  private JMenuItem Insert_Location = new JMenuItem("Insert Location");
  private JMenuItem Delete_Location = new JMenuItem("Delete Location");
  private JMenuItem Insert_Path = new JMenuItem("Insert Path");
  private JMenuItem Delete_Path = new JMenuItem("Delete Path");
  private JMenuItem Change_Properties = new JMenuItem("Change Properties"); 
  private JMenuItem New = new JMenuItem("New");
  private JMenuItem Open = new JMenuItem("Open");
  private JMenuItem Save = new JMenuItem("Save");
  private JMenuItem Save_As = new JMenuItem("Save As");
  
  private ArrayList<Location> Location_List = new ArrayList<Location>();
  private ArrayList<Path> Path_List = new ArrayList<Path>();
  
  public static void main(String[] args) { 
    MapEditor mapEditor = new MapEditor();
    mapEditor.setVisible(true);
  } 
  
  MapEditor() {
    setTitle("Map Editor");
    setSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    setBackground(Color.gray);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    JPanel panel = new JPanel();
    panel.setLayout( new BorderLayout()); 
    getContentPane().add(panel);
    
    myPanel = new MyPanel(Location_List, Path_List);
    scrollPane = new JScrollPane();
    scrollPane.getViewport().add(myPanel);
    scrollPane.getVerticalScrollBar().setUnitIncrement(5);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(5);
    
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(myPanel.get_JSlider(),BorderLayout.SOUTH);
    myPanel.set_ScrollPane(scrollPane);
    
    setJMenuBar(bar);
    bar.add(Files);
    Files.add(New);
    Files.add(Open);
    Files.add(Save);
    Files.add(Save_As);
    
    bar.add(Modes);
    Modes.add(Do_Nothing);
    Modes.add(Insert_Location);
    Modes.add(Delete_Location);
    Modes.add(Insert_Path);
    Modes.add(Delete_Path);
    Modes.add(Change_Properties);
    
    scrollPane.addMouseListener(this);
    scrollPane.addMouseMotionListener(this);
    
    Do_Nothing.addActionListener(this);
    New.addActionListener(this);
    Open.addActionListener(this);
    Save.addActionListener(this);
    Save_As.addActionListener(this);
    Insert_Location.addActionListener(this);
    Delete_Location.addActionListener(this);
    Insert_Path.addActionListener(this);
    Delete_Path.addActionListener(this);
    Change_Properties.addActionListener(this);
  }
  
  //Used for store the temperary location which is in focus.
  private Location temp;
  private Path temp_Path;
  private boolean State_focus = false;
  private boolean dragged = false;
  
  public void mouseEntered(MouseEvent e){}   
  public void mouseExited(MouseEvent e){}
  public void mouseClicked(MouseEvent e){
    if(Run_Modes==0){}
    else if(Run_Modes==1){
      if(e.getClickCount()==2){
        showDialog(e);
      }
    }
    else if(Run_Modes==5)
    {
      showDialog(e);
    }
  }
  public void mousePressed(MouseEvent e){
    boolean location_Owned = false;
    Point p = scrollPane.getViewport().getViewPosition();
    map_X = (int)(e.getX()/myPanel.get_Scale()+p.getX()/myPanel.get_Scale());
    map_Y = (int)(e.getY()/myPanel.get_Scale()+p.getY()/myPanel.get_Scale());
    p.move( map_X , map_Y );
    if(Run_Modes==0){}
    else if(Run_Modes==1){
      for(int i=0; Location_List !=null&&i<Location_List.size();i++){
        if((Location_List.get(i)).check_Location(map_X,map_Y,myPanel.get_Scale())){ 
          location_Owned = true;
          temp =  Location_List.get(i);  
        }
      }
      if(location_Owned == false){
        Location new_Location = new Location(p);
        new_Location.set_Id(id_Count);
        id_Count++;
        Location_List.add(new_Location);
      }
      else{
        State_focus = true;
        temp.change_Size();
      }
      myPanel.repaint();
    }
    else if(Run_Modes==3){
      for(int i=0; Location_List !=null&&i<Location_List.size();i++){
        if((Location_List.get(i)).check_Location(map_X,map_Y,myPanel.get_Scale())){ 
          location_Owned = true;
          temp =  Location_List.get(i);  
        }
      }
      if(location_Owned == true){
        Path new_Path = new Path(Location_List);
        temp_Path = new_Path;
        new_Path.set_Origin(temp);
        Point p2 = new Point((int)p.getX(),(int)p.getY());
        Location temp2 = new Location(p2);
        new_Path.set_Destination(temp2);
        Path_List.add(temp_Path);
        State_focus = true;
      }  
    }
    else if(Run_Modes==4){
      
    }
    else{
    }
  }
  
  public void mouseReleased(MouseEvent e){
    boolean location_Owned = false;
    Point p = scrollPane.getViewport().getViewPosition();
    map_X = (int)(e.getX()/myPanel.get_Scale()+p.getX()/myPanel.get_Scale());
    map_Y = (int)(e.getY()/myPanel.get_Scale()+p.getY()/myPanel.get_Scale());
    p.move( map_X , map_Y );
    if(Run_Modes==0){}
    else if(Run_Modes==1){
      if(State_focus==true){
        State_focus = false;
        if(dragged){
          temp.move_NewPoint(p);
          dragged = false; 
        }
        temp.recover_Size();
        myPanel.repaint();
      }
    }
    else if(Run_Modes==2){
      for(int i=0; Location_List !=null&&i<Location_List.size();i++){
        if((Location_List.get(i)).check_Location(map_X,map_Y,myPanel.get_Scale())){ 
          location_Owned = true;
          temp =  Location_List.get(i);  
        }
      }
      if(location_Owned == true){
        for(int i=0; temp.get_Connection() !=null&&i<temp.get_Connection().size();i++){
          if(temp.get_Connection().get(i).get_Origin() == temp)
            temp.get_Connection().get(i).get_Destination().get_Connection().remove(temp.get_Connection().get(i));
          else
            temp.get_Connection().get(i).get_Origin().get_Connection().remove(temp.get_Connection().get(i));
          Path_List.remove(temp.get_Connection().get(i));
        }
        Location_List.remove(temp);
        myPanel.repaint();
      }
    }
    else if(Run_Modes==3){
      boolean repeated = false; Location temp_Origin = temp;
      for(int i=0; Location_List !=null&&i<Location_List.size();i++){
        if((Location_List.get(i)).check_Location(map_X,map_Y,myPanel.get_Scale())&&Location_List.get(i)!=temp){ 
          location_Owned = true;
          temp =  Location_List.get(i);  
        }
      }
      if(location_Owned == true&& State_focus==true){ 
        for(int i=0; Path_List !=null&&i<Path_List.size();i++){
          if((Path_List.get(i).get_Origin()==temp_Origin&&Path_List.get(i).get_Destination()==temp)||
             Path_List.get(i).get_Origin()==temp&&Path_List.get(i).get_Destination()==temp_Origin){
            repeated = true;
          }
        }
        if(repeated){
          Path_List.remove(temp_Path);
        }
        else{
          temp_Path.set_Destination(temp);
          temp_Origin.add_Connection(temp_Path);
          temp.add_Connection(temp_Path);
          temp_Path.calculate_Distance();
        }
        State_focus = false;
        temp_Path = null;
        myPanel.repaint();
      }
      else{
        Path_List.remove(temp_Path);
        State_focus = false;
        temp_Path = null;
        myPanel.repaint();
      }
    }
    else if(Run_Modes==4){
      for(int i=0; Path_List !=null&&i<Path_List.size();i++){
        if(Path_List.get(i).check_Path(p,myPanel.get_Scale())){
          Path_List.get(i).get_Origin().get_Connection().remove(Path_List.get(i));
          Path_List.get(i).get_Destination().get_Connection().remove(Path_List.get(i));
          Path_List.remove(i);
          break; //make sure only remove one.
        }
      }
      myPanel.repaint();
    }
  }
  public void mouseMoved(MouseEvent e){}
  
  public void mouseDragged(MouseEvent e){
    Point p = scrollPane.getViewport().getViewPosition();
    map_X = (int)(e.getX()/myPanel.get_Scale()+p.getX()/myPanel.get_Scale());
    map_Y = (int)(e.getY()/myPanel.get_Scale()+p.getY()/myPanel.get_Scale());
    p.move( map_X , map_Y );
    if(Run_Modes==0){}
    else if(Run_Modes==1){
      if(State_focus==true){
        temp.move_NewPoint(p);
        myPanel.repaint();
        dragged = true;
      }
    }
    else if(Run_Modes==3){
      if(State_focus==true){
        temp_Path.get_Destination().move_NewPoint(p);
        myPanel.repaint();
      }
    }
  }
  
  public void actionPerformed(ActionEvent e){
    if(e.getSource().equals(Do_Nothing)) Run_Modes=0;
    else if(e.getSource().equals(Insert_Location)) Run_Modes=1;
    else if(e.getSource().equals(Delete_Location)) Run_Modes=2;
    else if(e.getSource().equals(Insert_Path)) Run_Modes=3;
    else if(e.getSource().equals(Delete_Path)) Run_Modes=4;
    else if(e.getSource().equals(Change_Properties))Run_Modes=5;
    else if(e.getSource().equals(New)){
      Path_List.clear();
      Location_List.clear();
      myPanel.set_backGroundImageName("");
      myPanel.get_JSlider().setValue(16);
      myPanel.set_Scale(1.0);
      scrollPane.getViewport().setViewPosition(new Point(0,0));
      myPanel.repaint();
      newDialog=new NewDialog(myPanel);
      myPanel.repaint();
    }
    else if(e.getSource().equals(Open))
    {
      JFileChooser chooser = new JFileChooser();
      try{
        File f1 = new File(new File(".").getCanonicalPath());
        chooser.setCurrentDirectory(f1);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML (*.xml)","xml");
        chooser.setFileFilter(filter);
        int val = chooser.showOpenDialog(null);
        if(val == JFileChooser.APPROVE_OPTION) {
          File fileToOpen=chooser.getSelectedFile();  
          openFile(fileToOpen);
        }
      }
      catch(Exception err){}
    }
    else if(e.getSource().equals(Save)){
      JFileChooser chooser = new JFileChooser();
      File f;
      try{
        f=new File(new File("MapEditor.xml").getCanonicalPath());
        chooser.setCurrentDirectory(f);
        chooser.setSelectedFile(f);
        int val = chooser.showSaveDialog(null);
        if(val == JFileChooser.APPROVE_OPTION){
          saveFile(f);
        }
      }
      catch (Exception err){}
    }
    else if(e.getSource().equals(Save_As)){
      JFileChooser chooser = new JFileChooser();
      try{
        File f1 = new File(new File(".").getCanonicalPath());
        chooser.setCurrentDirectory(f1);
        int val = chooser.showSaveDialog(null);
        if(val == JFileChooser.APPROVE_OPTION){
          File f = chooser.getSelectedFile();
          saveFile(f);
        }
      }
      catch (Exception err){}
    }
  }
  
  public void showDialog(MouseEvent e)
  {
    boolean location_Owned = false;
    Point p = scrollPane.getViewport().getViewPosition();
    map_X = (int)(e.getX()/myPanel.get_Scale()+p.getX()/myPanel.get_Scale());
    map_Y = (int)(e.getY()/myPanel.get_Scale()+p.getY()/myPanel.get_Scale());
    p.move( map_X , map_Y );
    for(int i=0; Location_List !=null&&i<Location_List.size();i++){
      if((Location_List.get(i)).check_Location(map_X,map_Y,myPanel.get_Scale())){
        location_Owned = true; 
        temp =  Location_List.get(i);
      }
    }
    if(location_Owned == true){
      location_Owned = false;
      dialog=new CustomDialog(temp);
    }
  }
  
  public void openFile(File f){
    try
    {
      Location_List.clear();
      Path_List.clear();
      DocumentBuilderFactory documentFactory =DocumentBuilderFactory.newInstance();
      DocumentBuilder builder =documentFactory.newDocumentBuilder();
      Document doc = builder.parse(f);
      doc.getDocumentElement().normalize();
      NodeList bittemp = doc.getElementsByTagName("mapfile");
      for(int i=0;i<bittemp.getLength();i++)
      {
        Element bitmap = (Element)bittemp.item(i);
        String backGroundImage=bitmap.getAttribute("bitmap");
        myPanel.set_backGroundImageName(backGroundImage);
        double scale=Double.parseDouble(bitmap.getAttribute("scale-feet-per-pixel"));
        myPanel.set_Scale(scale);
      }
      NodeList loctemp = doc.getElementsByTagName("location");
      for(int i=0;i<loctemp.getLength();i++)        //Read Locations
      {
        Element locate = (Element)loctemp.item(i);
        int x = Integer.parseInt(locate.getAttribute("x"));
        int y = Integer.parseInt(locate.getAttribute("y"));
        String name = locate.getAttribute("name");
        int id = Integer.parseInt(locate.getAttribute("id"));
        Location_List.add(new Location(new Point(x,y),id,name));
      }
      NodeList pathtemp=doc.getElementsByTagName("path");
      for(int i=0;i<pathtemp.getLength();i++)          //Read Paths
      {
        Element path = (Element)pathtemp.item(i);
        int idfrom = Integer.parseInt(path.getAttribute("idfrom"));
        int idto = Integer.parseInt(path.getAttribute("idto"));
        String type = path.getAttribute("type");
        Path temp;
        Path_List.add((temp = new Path(Location_List,idfrom,idto)));
        temp.calculate_Distance();
      }
      myPanel.repaint();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void saveFile(File f) throws TransformerException, IOException{
    Document doc=buildFile();
    Transformer t = TransformerFactory.newInstance().newTransformer();
    t.setOutputProperty(OutputKeys.INDENT, "yes");
    t.setOutputProperty(OutputKeys.METHOD, "xml");
    t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(f)));
  }
  
  public Document buildFile(){
    try
    {
      DocumentBuilderFactory documentFactory =DocumentBuilderFactory.newInstance();
      DocumentBuilder builder =documentFactory.newDocumentBuilder();
      Document doc = builder.newDocument();
      Location temp;
      Path temp_Path;
      Element mapfile = doc.createElement("mapfile");
      doc.appendChild(mapfile);
      mapfile.setAttribute("scale-feet-per-pixel",""+ myPanel.get_Scale());
      mapfile.setAttribute("bitmap",myPanel.get_backGroundImageName());
      int idC=0;
      for(int i = 0;Location_List !=null&&i<Location_List.size();i++)
      {
        temp =  Location_List.get(i); 
        Element loc = doc.createElement("location");
        loc.setAttribute("x",""+ temp.getX());
        loc.setAttribute("y",""+ temp.getY());
        loc.setAttribute("name",temp.get_Name());
        temp.set_Id(idC);
        loc.setAttribute("id",""+ temp.get_Id());
        mapfile.appendChild(loc);
        idC++;
      }
      for(int i=0; Path_List !=null&&i<Path_List.size();i++)
      {
        temp_Path =  Path_List.get(i); 
        Location destination=temp_Path.get_Destination();
        Location origin=temp_Path.get_Origin();
        Element paths=doc.createElement("path");
        paths.setAttribute("type","undirected");
        paths.setAttribute("idto",""+ destination.get_Id());
        paths.setAttribute("idfrom",""+ origin.get_Id());
        mapfile.appendChild(paths);
      }
      return doc;
    }
    catch (ParserConfigurationException e)
    {
      e.printStackTrace();
    }
    return null;
  }
};

