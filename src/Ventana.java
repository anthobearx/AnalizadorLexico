import javax.swing.*;


public class Ventana extends JFrame {
    public Ventana(String titulo, int ancho, int alto, boolean visible) {
        super(titulo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*Aquí se agrega lo de mi panelito*/
        MiPanel miPanel = new MiPanel();
        add(miPanel);

        setSize(ancho, alto);
        setVisible(visible);
    }
}
