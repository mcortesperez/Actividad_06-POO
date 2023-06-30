import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Marco_contacto extends JFrame implements ActionListener{
    private Container contenedor;
    private JTextField campo_nombre, campo_numero;
    private JButton create, read, update, delete;
    private JLabel nombre, numero;

    public Marco_contacto() {
        lamina();
        setVisible(true);
        setTitle("Contactos");
        setSize(350, 350);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void lamina() {
        contenedor = getContentPane();
        contenedor.setLayout(null);        //Lamina sin layout

        nombre = new JLabel();
        nombre.setText("Name:");
        nombre.setBounds(20, 20, 135, 23);
        campo_nombre = new JTextField();
        campo_nombre.setBounds(100, 20, 200, 23);

        numero = new JLabel();
        numero.setText("Number:");
        numero.setBounds(20, 50, 135, 23);
        campo_numero = new JTextField();
        campo_numero.setBounds(100, 50, 200, 23);

        create = new JButton();
        create.setText("Create");
        create.setBounds(100, 120, 135, 23);
        create.addActionListener(this);

        read = new JButton();
        read.setText("Read");
        read.setBounds(100, 160, 135, 23);
        read.addActionListener(this);

        update = new JButton();
        update.setText("Update");
        update.setBounds(100, 200, 135, 23);
        update.addActionListener(this);

        delete = new JButton();
        delete.setText("Delete");
        delete.setBounds(100, 240, 135, 23);
        delete.addActionListener(this);

        contenedor.add(nombre);
        contenedor.add(campo_nombre);
        contenedor.add(numero);
        contenedor.add(campo_numero);
        contenedor.add(create);
        contenedor.add(read);
        contenedor.add(update);
        contenedor.add(delete);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == create) {

            String nombre_contacto = campo_nombre.getText();
            long numero_contacto;

            try {
                numero_contacto = Long.parseLong(campo_numero.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "El número debe ser un valor numérico válido.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                File file = new File("Contactos.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }

                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                boolean found = false;

                while (raf.getFilePointer() < raf.length()) {
                    String cadena = raf.readLine();
                    String[] lineSplit = cadena.split("!");
                    String nom = lineSplit[0];
                    long num = Long.parseLong(lineSplit[1]);

                    if (nom.equals(nombre_contacto) || num == numero_contacto) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    String cadena = nombre_contacto + "!" + numero_contacto;
                    raf.writeBytes(cadena);
                    raf.writeBytes(System.lineSeparator());
                    JOptionPane.showMessageDialog(this,
                            "Contacto añadido.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "El nombre o número ya existe.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                raf.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al acceder al archivo.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }if (e.getSource() == read) {
            try {
                File file = new File("Contactos.txt");

                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this,
                            "El archivo de contactos no existe.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                RandomAccessFile raf = new RandomAccessFile(file, "rw");

                String cadena;
                String nom;
                long num;

                while (raf.getFilePointer() < raf.length()) {

                    cadena = raf.readLine();
                    String[] lineSplit = cadena.split("!");

                    nom = lineSplit[0];
                    num = Long.parseLong(lineSplit[1]);

                    JOptionPane.showMessageDialog(this,
                            "Contact Name: " + nom + "\n"
                                    + "Contact Number: " + num + "\n",
                            "Contact Information", JOptionPane.INFORMATION_MESSAGE);
                }

                raf.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al acceder al archivo.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }if (e.getSource() == update) {
            try {
                String nombre_contacto = campo_nombre.getText();
                long numero_contacto;

                try {
                    numero_contacto = Long.parseLong(campo_numero.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "El número debe ser un valor numérico válido.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File file = new File("Contactos.txt");

                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this,
                            "El archivo de contactos no existe.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                boolean found = false;

                while (raf.getFilePointer() < raf.length()) {
                    String cadena = raf.readLine();
                    String[] lineSplit = cadena.split("!");
                    String nom = lineSplit[0];
                    long num = Long.parseLong(lineSplit[1]);

                    if (nom.equals(nombre_contacto) || num == numero_contacto) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    File tmpFile = new File("temp.txt");
                    RandomAccessFile tmpraf = new RandomAccessFile(tmpFile, "rw");

                    raf.seek(0);

                    while (raf.getFilePointer() < raf.length()) {
                        String cadena = raf.readLine();
                        int index = cadena.indexOf('!');
                        String name = cadena.substring(0, index);

                        if (name.equals(nombre_contacto)) {
                            cadena = name + "!" + String.valueOf(numero_contacto);
                        }

                        tmpraf.writeBytes(cadena);
                        tmpraf.writeBytes(System.lineSeparator());
                    }

                    raf.seek(0);
                    tmpraf.seek(0);

                    while (tmpraf.getFilePointer() < tmpraf.length()) {
                        raf.writeBytes(tmpraf.readLine());
                        raf.writeBytes(System.lineSeparator());
                    }

                    raf.setLength(tmpraf.length());

                    tmpraf.close();
                    raf.close();

                    tmpFile.delete();

                    JOptionPane.showMessageDialog(this,
                            "Contacto actualizado.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "El nombre o número no existe.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al acceder al archivo.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }if (e.getSource() == delete) {
            try {
                String nombre_contacto = campo_nombre.getText();

                File file = new File("Contactos.txt");

                if (!file.exists()) {
                    JOptionPane.showMessageDialog(this,
                            "El archivo de contactos no existe.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                boolean found = false;

                while (raf.getFilePointer() < raf.length()) {
                    String cadena = raf.readLine();
                    String[] lineSplit = cadena.split("!");
                    String nom = lineSplit[0];

                    if (nom.equals(nombre_contacto)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    File tmpFile = new File("temp.txt");
                    RandomAccessFile tmpraf = new RandomAccessFile(tmpFile, "rw");

                    raf.seek(0);

                    while (raf.getFilePointer() < raf.length()) {
                        String cadena = raf.readLine();
                        int index = cadena.indexOf('!');
                        String nom = cadena.substring(0, index);

                        if (nom.equals(nombre_contacto)) {
                            continue;
                        }

                        tmpraf.writeBytes(cadena);
                        tmpraf.writeBytes(System.lineSeparator());
                    }

                    raf.seek(0);
                    tmpraf.seek(0);

                    while (tmpraf.getFilePointer() < tmpraf.length()) {
                        raf.writeBytes(tmpraf.readLine());
                        raf.writeBytes(System.lineSeparator());
                    }

                    raf.setLength(tmpraf.length());

                    tmpraf.close();
                    raf.close();

                    tmpFile.delete();

                    JOptionPane.showMessageDialog(this,
                            "Contacto eliminado.", "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "El nombre no existe.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al acceder al archivo.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}