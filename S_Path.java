import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;
import java.lang.Object;
import java.awt.geom.Line2D;
import java.awt.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;
public class S_Path{
  private double Weight = 0;
  private ArrayList<Path> shortest;
  private Location Port;
  
  public S_Path(double _Weight, ArrayList<Path> _Paths ,Location _new_Location, Path new_P){
    Weight = _Weight;
    shortest = new ArrayList<Path>(_Paths);
    if(new_P!=null)
      shortest.add(new_P);
    Port = _new_Location;
  }
  
  public Location get_Port(){
    return Port;
  }
  
  public Path get_Last_Path(){
    if(shortest.size()!=0)
      return shortest.get(shortest.size()-1);
    else
      return null;
  }
  
  public double get_Weight(){
    return Weight;
  }
  
  public void set_Weight(double _Weight){
    Weight += _Weight;
  }
  
  public ArrayList<Path> get_Path_List(){
    return shortest;
  }
}
