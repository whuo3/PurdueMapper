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

public class Path {
  private Location origin;
  private Location destination;
  private double distance;
  private ArrayList<Location> Location_List;
  //color: 1->shortest path, 0-> normal
  private int color = 0;
  
  public void change_Red(){
    color = 1;
  }
  
  public void recover_Color(){
    color = 0;
  }
  
  public int get_Color(){
    return color;
  }
  
  public Path(ArrayList<Location> _List){
    Location_List = _List;
    origin = null;
    destination = null;
  } 
  
  public Path(ArrayList<Location> _List, int idfrom, int idto){
    Location_List = _List;
    for(int i = 0;i<Location_List.size();i++){
      if(Location_List.get(i).get_Id()==idfrom){
        origin = Location_List.get(i);
        origin.add_Connection(this);
      }
      if(Location_List.get(i).get_Id()==idto){
        destination = Location_List.get(i);
        destination.add_Connection(this);
      }
    }
  }
  
  public double get_Distance(){
    return distance;
  }
  
  public void set_Distance(double _distance){
    distance = _distance;
  }
  
  public void calculate_Distance(){
    distance = origin.get_Point().distance(destination.get_Point());
  }
  
  public void set_Origin(Location _Origin){
    origin = _Origin;
  }
  
  public void set_Destination(Location _Destination){
    destination = _Destination;
  }
  
  public Location get_Origin(){
    return origin;
  }
  
  public Location get_Destination(){
    return destination;
  }
  
  public boolean check_Path(Point _point,double scale){
    if(origin!=null&&destination!=null){
      if((new Line2D.Float(origin.get_Point(), destination.get_Point())).ptLineDist(_point) <= (6.0/scale))
      {
       double x1=origin.getX() - _point.getX();
       double y1=origin.getY() - _point.getY();
       double x2=destination.getX() - _point.getX();
       double y2=destination.getY() - _point.getY();
       if((x1*x2)+(y1*y2)<0)
        return true;
      else    return false;
    }
    }
    return false;
  }
}
