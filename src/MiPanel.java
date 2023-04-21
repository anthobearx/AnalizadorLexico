/*Por alguna razón que desconozco, debo hacer los componentes con JPanel para que los componentes tomen
 * el tamaño indicado*/

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MiPanel extends JPanel {
    JTextArea textA;
    JButton btnAbrirArchivo;
    JButton btnAnalizar;
    JButton btnLimpiar;
    JFileChooser fileChooser = new JFileChooser();
    JTable tablita;

    public MiPanel() {
        /*Establecer un layout manager para tener control sobre los componentes*/
        setLayout(new GridBagLayout());
        textA = new JTextArea(10, 15);

        //creacion de oyente Abrir
        ManejadorAbrir manejadorAbrir = new ManejadorAbrir();
        btnAbrirArchivo = new JButton("Abrir");
        btnAbrirArchivo.setMnemonic('A');
        btnAbrirArchivo.addActionListener(manejadorAbrir);


        ManejadorLimpiar manejadorLimpiar = new ManejadorLimpiar();
        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setMnemonic('L');
        btnLimpiar.addActionListener(manejadorLimpiar);

        ManejadorAnalizar manejadorAnalizar = new ManejadorAnalizar();
        btnAnalizar = new JButton("Analizar");
        btnAnalizar.addActionListener(manejadorAnalizar);

        /*Crear una instancia de GridBagConstraints*/
        GridBagConstraints c = new GridBagConstraints();

        /*Establecer los atributos de GridBagConstraints*/
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.insets = new Insets(220, 10, 0, 0); // Margen izquierdo de 10px
        c.gridx = 0; // Posición x del botón Abrir
        c.gridy = 0; // Posición y del botón Abrir--j
        add(btnAbrirArchivo, c);//aqui

        c.insets = new Insets(10, 10, 0, 0); // Margen btn analizar
        c.gridx = 0; // Posición x del botón Abrir
        c.gridy = 1; // Posición y del botón Abrir
        add(btnLimpiar, c);

        c.insets = new Insets(10, 10, 230, 0); // Margen izquierdo de 10px
        c.gridx = 0; // Posición x del botón Abrir
        c.gridy = 2; // Posición y del botón Abrir
        add(btnAnalizar, c);

        c.insets = new Insets(10, 10, 10, 10); // Margen
        c.gridx = 1; // Posición x del JTextArea
        c.gridy = 0; // Posición y del JTextArea
        c.gridheight = 0; // El JTextArea ocupa 2 filas
        c.weightx = 2.0; // El JTextArea ocupa más espacio horizontalmente que los botones
        add(new JScrollPane(textA), c);

        Object[][] datos = {
                {"", ""},
                {"", ""},
                {"", ""}
        };
        // Nombres de las columnas
        String[] columnas = {"Token", "Lexema"};

        // Crea el modelo con los datos y las columnas
        DefaultTableModel modelo = new DefaultTableModel(datos, columnas);
        // Crea la tabla con el modelo
        tablita = new JTable(modelo);

        // Agrega la tabla a un JScrollPane para poder hacer scroll
        JScrollPane scrollPane = new JScrollPane(tablita);
        c.insets = new Insets(10, 10, 10, 10); // Margen
        c.gridx = 2; // Posición x del JTextArea
        c.gridy = 0; // Posición y del JTextArea
        c.gridheight = 0; // El JTextArea ocupa 2 filas
        c.weightx = 0.0; // El JTextArea ocupa más espacio horizontalmente que los botones

        add(scrollPane,c);
    }
    private class ManejadorAbrir implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            // Mostrar el cuadro de diálogo Abrir archivo
            int resultado = fileChooser.showOpenDialog(null);

            // se elige un archivo, se lee contenido y lo muestra en el área de texto
            if (resultado == JFileChooser.APPROVE_OPTION) {
                //se guarda el archivo en tipo file
                File archivo = fileChooser.getSelectedFile();
                //se lee mediante un bufferreader y se escribe en el textarea
                try (BufferedReader leer = new BufferedReader(new FileReader(archivo))) {
                    StringBuilder sb = new StringBuilder();
                    String linea;
                    while ((linea = leer.readLine()) != null) {
                        sb.append(linea);
                        sb.append("\n");
                    }
                    textA.setText(sb.toString());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    private class ManejadorLimpiar implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            textA.setText("");
            DefaultTableModel model = (DefaultTableModel) tablita.getModel();
            model.setRowCount(0);//limpiar
            model.setRowCount(3);//poner 3 de nuevo
        }
    }
    private class ManejadorAnalizar implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultTableModel model = (DefaultTableModel) tablita.getModel();
            model.setRowCount(0);//limpiar
            model.setRowCount(3);//poner 3 de nuevo
            ArrayList<String> cadenas = new ArrayList<>();
            String contenido = textA.getText();
            //separar palabras
            /*
            * La expresión regular "\s+" busca uno o más espacios en blanco, incluyendo espacios,
            *  tabuladores, saltos de línea, entre otros. El doble backslash en "\s+" es necesario
            *  para escapar el carácter "" y que la expresión regular sea interpretada correctamente
            *  por el método split.
            *
            * */
            String[] palabrasSeparadas = contenido.split("\\s+");//separar palabras y guardarlas en palabrasSeparadas
            /*Es lo mismo que si se hace un for each para guardar todas las palabrasSeparadas en el arreglo
            *for (String palabra: palabrasSeparadas) {//añadirlo al ArrayList
                cadenas.add(palabra);
            }
            * */
            Collections.addAll(cadenas, palabrasSeparadas);

            //añadir ArrayList a tabla
            int columna = 0;
            for (int fila = 0; fila < cadenas.size(); fila++) {
                tablita.setValueAt(cadenas.get(fila),fila,columna);
                //añadir una fila para poder agregar mas datos en un futuro
                model = (DefaultTableModel) tablita.getModel();
                Object[] newRow = {""};
                model.addRow(newRow);
                tablita.repaint();//repintar tablita
            }
            //detectar que tipo es
            columna=1;
            String regexPalabra = "^[a-zA-Z]+$";
            String regexNumero = "^[0-9]+$";
            String regexEspecial = "^[^a-zA-Z0-9]+$";
            String regexOperador = "[+\\-*/]";
            for (int fila = 0; fila < cadenas.size(); fila++) {
                if (cadenas.get(fila).matches(regexPalabra)) {
                    tablita.setValueAt("Palabra",fila,columna);
                } else if (cadenas.get(fila).matches(regexNumero)) {
                    tablita.setValueAt("Numero",fila,columna);
                } else if (cadenas.get(fila).matches(regexOperador)) {
                    tablita.setValueAt("Operador",fila,columna);
                } else if (cadenas.get(fila).matches(regexEspecial)) {
                    tablita.setValueAt("Caracteres especial",fila,columna);

                } else {
                    tablita.setValueAt("Desconocido",fila,columna);
                }
            }

            //cadenas.removeAll(cadenas);//limpiar el arraylist

        }
    }
}
