import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.filechooser.*;
import javax.accessibility.*;
import java.awt.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.io.*;
import java.applet.*;
import java.net.*;

class Location{
  //used to store the path point to others or pointed by
  private ArrayList<Path> connected_With = new ArrayList<Path>();
  //Size is used to printing on the graph 
  private int Size = 15;
  
  private Point _point;
  private int id = 0;
  private String Name;
  
  public Point get_Point(){
    return _point;
  }
  
  public Location(Point point, int _id, String _name ){
    _point = point;
    id = _id;
    Name = _name;
  }
  
  public Location(Point point){
    _point = point;
  }
  
  public void change_Size(){
    Size = (int)(Size/2);
  }
  
  public void recover_Size(){
    Size = Size*2;
  }
  
  public int get_Size(){
    return Size;
  }
  
  public void move_NewPoint(Point point){
    _point = point;
  }
  
  public void set_Name(String _Name){
    Name = _Name;
  }
  
  public void set_Id(int _id){
    id = _id;
  }
  
  public String get_Name(){
    return Name;
  }
  
  public int get_Id(){
    return id;
  }
  
  public int getX(){
    return (int)_point.getX();
  }
  
  public int getY(){
    return (int)_point.getY();
  }
  
  public void add_Connection(Path _Path){
    connected_With.add(_Path) ;
  }
  
  public ArrayList<Path> get_Connection(){
    return connected_With;
  }
  
  public boolean check_Location(int px, int py, double scale){
    int x = (int) _point.getX();
    int y = (int) _point.getY();
    int radius = (int)(7.5/scale);
    return px > x - radius && px < x + radius && 
      py > y - radius && py < y + radius;
  }
}

