import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.lang.Object;
import java.beans.*;
import java.io.*;
import java.applet.*;
import java.net.*;

public class MST{
  private ArrayList<Path> SPath_List;
  private ArrayList<Location> UList;
  private ArrayList<Location> SList;
  private ArrayList<Path> UPath_List;
  private ArrayList<Location> Location_List;
  private int List_Index = -1; private double min_Distance = 99999.0; 
  private Location L_temp;
  private Path P_temp;
  private int id;
  public MST(ArrayList<Path> PathList,ArrayList<Location> LocationList){
    UList = new ArrayList<Location>(LocationList);
    UPath_List = new ArrayList<Path>(PathList);
    SList = new ArrayList<Location>(); 
    SPath_List = new ArrayList<Path>();
    SList.add(UList.get(UList.size()-1));
    UList.remove(UList.get(UList.size()-1));
    for(int i=0;i<LocationList.size()-1 && UList!=null;i++){
      for(int j=0;j<SList.size() && SList!=null ;j++){
        for(int k=0;k< SList.get(j).get_Connection().size() && SList.get(j).get_Connection()!=null;k++){
          if(SList.contains(SList.get(j).get_Connection().get(k).get_Destination())&&SList.contains(SList.get(j).get_Connection().get(k).get_Origin())) continue;
          if(SList.get(j).get_Connection().get(k).get_Distance() < min_Distance){
            P_temp=SList.get(j).get_Connection().get(k);
            if(SList.get(j).get_Connection().get(k).get_Destination() == SList.get(j)){
              L_temp =SList.get(j).get_Connection().get(k).get_Origin();
            }
            else{
              L_temp = SList.get(j).get_Connection().get(k).get_Destination();
            }
            min_Distance = P_temp.get_Distance();
          } 
        }
      } 
      if(L_temp!=null&&P_temp!=null){
        SList.add(L_temp);
        UList.remove(L_temp);
        SPath_List.add(P_temp);
        P_temp = null;
        L_temp = null;
        min_Distance = 99999.0;
      }
      else break;
    }
    for(int i=0;i<SPath_List.size() && SPath_List!=null;i++){
      SPath_List.get(i).change_Red();
    }  
  }
  
  public ArrayList<Location> get_UList(){
    return UList;
  }
}

