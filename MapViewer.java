/* CS 251: Project 5
 * Group Members: Alfred Huo                0025516173
 *                Gurumukh Uttamchandani    0025219228
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

class MapViewer extends JFrame implements ActionListener{
  private int PREFERRED_WIDTH = 680;
  private int PREFERRED_HEIGHT = 600;
  private JScrollPane scrollPane;
  private MyPanel myPanel;
  private MapEditor mapEditor;
  private JMenuBar bar = new JMenuBar();
  private JMenu Files = new JMenu("File");
  private JMenu functions = new JMenu("Functions");
  private JMenuItem Open = new JMenuItem("Open");
  private JMenuItem directions = new JMenuItem("Directions");
  private JMenuItem mST = new JMenuItem("MST");
  private JMenuItem clear = new JMenuItem("Clear");
  private ArrayList<Location> Location_List = new ArrayList<Location>();
  private ArrayList<Path> Path_List = new ArrayList<Path>();
  private int from_ID;
  private int to_ID;
  private MST myMst;
  
  public MapViewer(){
    setTitle("Map Viewer");
    setSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    setBackground(Color.gray);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = new JPanel();
    panel.setLayout( new BorderLayout()); 
    getContentPane().add(panel);
    myPanel = new MyPanel(Location_List, Path_List);
    scrollPane = new JScrollPane();
    mapEditor = new MapEditor();
    scrollPane.getViewport().add(myPanel);
    scrollPane.getVerticalScrollBar().setUnitIncrement(5);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(5);
    
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(myPanel.get_JSlider(),BorderLayout.SOUTH);
    myPanel.set_ScrollPane(scrollPane);
    
    setJMenuBar(bar);
    bar.add(Files);
    Files.add(Open);
    
    bar.add(functions);
    functions.add(directions);
    functions.add(mST);
    functions.add(clear);
    
    Open.addActionListener(this);
    directions.addActionListener(this);
    mST.addActionListener(this);
    clear.addActionListener(this);
  }
  
  public static void main(String[] args) { 
    MapViewer mapViewer = new MapViewer();
    mapViewer.setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e){
    if(e.getSource().equals(Open)){
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
    else if(e.getSource().equals(directions)){
      Directions direction_List = new Directions(myPanel, Location_List);
    }
    else if(e.getSource().equals(mST)){
      myMst=new MST(Path_List,Location_List);
      while(myMst.get_UList().size()!=0){
        myMst = new MST(Path_List,myMst.get_UList());
      }
      myPanel.repaint();
    }
    else if(e.getSource().equals(clear)){
      for(int i=0;i<Path_List.size()&&Path_List!=null;i++)
        Path_List.get(i).recover_Color();
      myPanel.repaint();
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
  
  public void set_fromID(int id){
    from_ID = id;
  }
  
  public void set_toID(int id){
    to_ID = id;
  }
}
