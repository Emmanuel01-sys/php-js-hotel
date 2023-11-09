import java.io.*;
import java.util.Date; 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Notepad {
  int width, height, fontSize;
  File fileReference;
  String fileName;
  JFileChooser fileChooser;
  boolean firstTimeSave;
  JFrame frame;
  JTextArea workArea;
  JScrollPane scroll;
  JMenuItem newFile,open,save,saveAs,print,closeTab,closeWindow,exit,cut,copy,paste,
    replace,find,gotoln,time,zoomOut,zoomIn,statusBar,allSelect;
  JLabel status, utf;
  JMenuBar menu;
  JMenu file, edit, view, zoom;
  boolean showStatusBar;

  Notepad(){
    fileReference = null;
    width = height = 500;
    fileName = "Untitled";
    fileChooser = new JFileChooser();
    workArea = new JTextArea();
    scroll = new JScrollPane(workArea);
    firstTimeSave = true;
    fontSize = 16;
    showStatusBar = true;
    frame = new JFrame(fileName);
    status = new JLabel("    Ln 1, Col 1");
    utf = new JLabel("|| UTF 8", JLabel.RIGHT);

    Image logo = Toolkit.getDefaultToolkit().getImage("logo.jpg");
    frame.setIconImage(logo);

    //MENU
    menu = new JMenuBar();
    file = new JMenu("File");
    edit = new JMenu("Edit");
    view = new JMenu("View");
    zoom = new JMenu("Zoom");

    newFile = new JMenuItem("New file");
    open = new JMenuItem("Open");
    save = new JMenuItem("Save");
    saveAs = new JMenuItem("Save as");
    print = new JMenuItem("Print");
    closeWindow = new JMenuItem("Close Window");
    exit = new JMenuItem("Exit");
    cut = new JMenuItem("Cut");
    copy = new JMenuItem("Copy");
    paste = new JMenuItem("Paste");
    allSelect = new JMenuItem("Select all");
    replace = new JMenuItem("Replace All");
    find = new JMenuItem("Find");
    gotoln = new JMenuItem("Go to");
    time = new JMenuItem("Time/Date");
    zoomOut = new JMenuItem("Zoom out");
    zoomIn = new JMenuItem("Zoom in");
    statusBar = new JMenuItem("-  \tStatus bar");  
  }

  void newFile(){
    fileName = "Untitled";
    frame.setTitle(fileName);
    workArea.setText("");
    firstTimeSave = true;
  }

    void open (){
    JFileChooser f = new JFileChooser();
    f.addChoosableFileFilter(new FileNameExtensionFilter("Plain text files", "txt"));
    f.addChoosableFileFilter(new FileNameExtensionFilter("Word Documents", "docx"));
    f.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
    f.addChoosableFileFilter(new FileNameExtensionFilter("Java source files", "java"));
    f.addChoosableFileFilter(new FileNameExtensionFilter("HTML", "html"));

    int val = f.showOpenDialog(this.frame);
    if(val == JFileChooser.APPROVE_OPTION){
      firstTimeSave = false;
      try {
        File file = fileReference = f.getSelectedFile();
        frame.setTitle(file.getName());
        FileReader stream = new FileReader(file);
        BufferedReader r = new BufferedReader(stream);
        String s = "";
        while (s != null){
          s = r.readLine();
          if(s == null) break;
          workArea.append(s + '\n');
        }
        stream.close();
        r.close();
      } catch(Exception e) {
        //
      }
    } else return;
  }

  String save(){
    String s = "";
      if(firstTimeSave) saveAs();
      else s = workArea.getText();
    return s;
  }

  void saveAs(){
    try{
      firstTimeSave = false;
      int val = fileChooser.showDialog(frame, "Save file");
      if(val == JFileChooser.APPROVE_OPTION){
        if(fileChooser.getSelectedFile().exists()) {
          int x = JOptionPane.showConfirmDialog(frame, "File already exists. Do you want to replace it?");
          if(x == JOptionPane.YES_OPTION){
            File f = new File(fileChooser.getSelectedFile().getPath());
            FileWriter writer = new FileWriter(f);
            writer.write(workArea.getText());
            writer.close();
            fileReference = f;
            JOptionPane.showMessageDialog(frame, "Successfully saved.");
          }
        }
        else {
          File f = new File(fileChooser.getSelectedFile().getPath());
          f.createNewFile();
          FileWriter writer = new FileWriter(f);
          writer.write(workArea.getText());
          writer.flush();
          writer.close();
          fileReference = f;
          JOptionPane.showMessageDialog(frame, "Successfully saved.");
        }
      }
    } catch (Exception e) {
      //
    }
  }

  void print(){
    try{
      workArea.print();
    } catch (Exception e){
      //
    }
    
  }

  void confirmChanges(){
    File f = fileReference;
    try {
      FileWriter writer = new FileWriter(f);
      writer.write(save());
      writer.flush();
      writer.close();
    } catch(Exception e){
      //
    }
  }

  void exit(){
    int x = JOptionPane.showConfirmDialog(frame, "Confirm changes ?");
    if(x == JOptionPane.YES_OPTION){
      confirmChanges();
      frame.dispose();
    }
    else if(x == JOptionPane.NO_OPTION) frame.dispose();
  }

  void copy(){
    workArea.copy();
  }

  void cut(){
    workArea.cut();
  }

  void paste(){
    workArea.paste();
  }

  void allSelect(){
    workArea.selectAll();
  }

  void gotoln(){
      int ln;
      try {
        ln = workArea.getLineOfOffset(workArea.getCaretPosition()) + 1;
        String str = JOptionPane.showInputDialog(frame,"Enter Line Number:","" + ln);
        if(str == null) return;
        ln = Integer.parseInt(str);
        workArea.setCaretPosition(workArea.getLineStartOffset(ln - 1)); 
      } catch (Exception e){
        //
      }
    }

  void time(){
    workArea.insert(new Date().toString(), workArea.getSelectionStart());
  }

  void zoomIn(){
    //set lower limit to 40
    if(fontSize < 40) {
      fontSize += 2;
      workArea.setFont(new Font("Serif", Font.PLAIN, fontSize));
    }
  }

  void zoomOut(){
    //set lower limit to 6
    if(fontSize > 6) {
      fontSize -= 2;
      workArea.setFont(new Font("Serif", Font.PLAIN, fontSize));
    }
  }

  void statusBar(){
    if(showStatusBar){
      status.setVisible(false);
      statusBar.setText("Status bar");
      showStatusBar = false;
    } else {
      status.setVisible(true);
      statusBar.setText("-  \tStatus bar");
      showStatusBar = true;
    }
  }

  void find(){
    String s = JOptionPane.showInputDialog(frame, "Enter name:");
    if(s.length() > 0 && !s.startsWith(" "))
      if(workArea.getText().indexOf(s) != -1) {
        int index = workArea.getText().indexOf(s);
        workArea.select(index, index + s.length());
      }
  }

  void replace() {
    String oldStr = JOptionPane.showInputDialog(frame, "Enter the name you want to replace:");
    if(oldStr == null)return;
    if(oldStr.length() > 0 && !oldStr.startsWith(" ")){
      String newStr = JOptionPane.showInputDialog(frame, "Enter the new name:");
      workArea.setText(workArea.getText().replaceAll(oldStr,newStr));
    }
  }

  void addEvents(){
    frame.addComponentListener(new ComponentListener(){
      public void componentResized(ComponentEvent e){
        width = frame.getWidth();
        height = frame.getHeight();

        workArea.setBounds(0, 0, width, height-100);
        status.setBounds(0,height-100, workArea.getWidth()/2, 30);
        utf.setBounds(workArea.getWidth()/2 - 20,height-100, workArea.getWidth()/2, 30);
      }
      public void componentMoved(ComponentEvent e){}
      public void componentShown(ComponentEvent e){}
      public void componentHidden(ComponentEvent e){}
    });

    frame.addWindowListener(new WindowListener(){
      public void windowClosing(WindowEvent e){
        exit();
      }
      public void windowActivated(WindowEvent e) {}
      public void windowClosed(WindowEvent e) {}
      public void windowDeactivated(WindowEvent e) {}
      public void windowDeiconified(WindowEvent e) {}
      public void windowIconified(WindowEvent e) {}
      public void windowOpened(WindowEvent e) {}
    });

    newFile.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        newFile();
      }
    });

    print.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        print();
      }
    });

    open.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        open();
      }
    });
    save.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        save();
      }
    });
    saveAs.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        saveAs();
      }
    });

    exit.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        exit();
      }
    });

    closeWindow.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        exit();
      }
    });

    copy.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        copy();
      }
    });

    cut.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        cut();
      }
    });

    paste.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        paste();
      }
    });

    allSelect.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        allSelect();
      }
    });

    gotoln.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        gotoln();
      }
    });

    time.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        time();
      }
    });

    zoomIn.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        zoomIn();
      }
    });

    zoomOut.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        zoomOut();
      }
    });

    find.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        find();
      }
    });

    statusBar.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        statusBar();
      }
    });

    replace.addMouseListener(new Events(){
      public void mousePressed(MouseEvent e){
        replace();
      }
    });

    workArea.addCaretListener(new CaretListener(){
      public void caretUpdate(CaretEvent e){
        int line = 0, column = 0, position = 0;
        try {
          if(workArea.getText().length() == 0) {
            line = 0;
            column = 0;
            }
          position = workArea.getCaretPosition();
          line = workArea.getLineOfOffset(position);
          column = position - workArea.getLineStartOffset(line);
        }catch(Exception ex){
          //
        }
        status.setText("    Ln " + (line+1) + ",\t Col " + (column + 1));
      }
    });
  }

  void setSizesAndFonts(){
    //SIZES
    frame.setSize(width, height);
    workArea.setBounds(0, 0, width, height-100);
    workArea.setLineWrap(true);
    status.setBounds(0,height-100, workArea.getWidth()/2, 30);
    utf.setBounds(workArea.getWidth()/2 - 20,height-100, workArea.getWidth()/2, 30);

    //FONTS
      workArea.setFont(new Font("Serif", Font.PLAIN, fontSize));
      status.setFont(new Font("Serif", Font.PLAIN, fontSize));
      utf.setFont(new Font("Serif", Font.PLAIN, fontSize));
      
      JMenuItem[] menuItems = { newFile,open,save,saveAs,print,closeWindow,exit,cut,copy,paste,
      replace,find,gotoln,time,zoomOut,zoomIn,statusBar,allSelect };
      for(JMenuItem item : menuItems) item.setFont(new Font("Serif", Font.PLAIN, fontSize));
      JMenu[] menus = { file, edit, view, zoom };
      for(JMenuItem item : menus) item.setFont(new Font("Serif", Font.PLAIN, 16));
      menus = null;
      menuItems = null;
    }

    void componentsAggregations(){
      //FRAME
      frame.setJMenuBar(menu); 
      frame.add(workArea);frame.add(scroll);
      frame.add(status);frame.add(utf);frame.add(new JLabel(""));
      frame.setVisible(true);
      //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      menu.add(file);menu.add(edit);menu.add(view);
      
      //menus
      file.add(newFile);file.add(open);file.add(save);file.add(saveAs);
      file.addSeparator();
      file.add(print);
      file.addSeparator();
      file.add(closeWindow);file.add(exit);

      edit.add(cut);edit.add(copy);edit.add(paste);edit.add(allSelect);
      edit.addSeparator();
      edit.add(replace);edit.add(find);edit.add(gotoln);
      edit.addSeparator();edit.add(time);
      view.add(zoom);view.add(statusBar);

      //submenu
      zoom.add(zoomIn);zoom.add(zoomOut);
    }


  public static void main(String[] args){
    Notepad notepad = new Notepad();
    notepad.addEvents();
    notepad.setSizesAndFonts();
    notepad.componentsAggregations();
  }
}

//class Events to implement mouse events
class Events implements MouseListener {
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
}