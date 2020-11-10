import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    // Ищем ячейки, строковое представление которых совпадает с needle
// (иголкой). Применяется аналогия поиска иголки в стоге сена, в роли
// стога сена - таблица
    private String needle = null;
    private String easyNeedle = null;
    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();
    private boolean isItEasy(int Number)
    {
        int counter = 0;
        for(int i = 1; i <= Number; i++)
        {
            if(Number%i == 0)
            {
                counter++;
            }
        }
        if(counter == 2)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public GornerTableCellRenderer() {
// Показывать только 5 знаков после запятой
        formatter.setMaximumFractionDigits(5);
// Не использовать группировку (т.е. не отделять тысячи
// ни запятыми, ни пробелами), т.е. показывать число как "1000",
// а не "1 000" или "1,000"
        formatter.setGroupingUsed(false);
// Установить в качестве разделителя дробной части точку, а не
// запятую. По умолчанию, в региональных настройках
// Россия/Беларусь дробная часть отделяется запятой
        DecimalFormatSymbols dottedDouble =
                formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
// Разместить надпись внутри панели
        panel.add(label);
// Установить выравнивание надписи по левому краю панели
        //panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }


    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row, int col) {
// Преобразовать double в строку с помощью форматировщика
        String formattedDouble = formatter.format(value);
// Установить текст надписи равным строковому представлению числа
        label.setText(formattedDouble);
        if ((col==1 || col == 2) && needle!=null && needle.equals(formattedDouble)) {
// Номер столбца = 1 (т.е. второй столбец) + иголка не null
// (значит что-то ищем) +
// значение иголки совпадает со значением ячейки таблицы -
// окрасить задний фон панели в красный цвет
            panel.setBackground(Color.RED);
        } else {
// Иначе - в обычный белый
            panel.setBackground(Color.WHITE);
        }
        if(Double.parseDouble(label.getText())>0)
        {
            panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        }
        else if(Double.parseDouble(label.getText())<0)
        {
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        }
        else
        {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        }
        if(easyNeedle != null) {
            if((isItEasy((int)Double.parseDouble(label.getText())) && (int)Double.parseDouble(label.getText())-Double.parseDouble(label.getText())>=(-0.1))|| (isItEasy((int)Double.parseDouble(label.getText())+1) && (((int)Double.parseDouble(label.getText())+1)-Double.parseDouble(label.getText())<=(0.1))))
            {
                panel.setBackground(Color.ORANGE);
            }
            else
            {
                panel.setBackground(Color.WHITE);
            }
        }
        return panel;
    }
    public void setNeedle(String needle) {
        this.needle = needle;
    }
    public void setEasyNeedle(String easyNeedle)
    {
        this.easyNeedle = easyNeedle;
    }
}
