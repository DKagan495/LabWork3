import javax.swing.table.AbstractTableModel;

import static java.lang.Math.pow;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    public GornerTableModel(Double from, Double to, Double step,
                            Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }
    public Double getFrom() {
        return from;
    }
    public Double getTo() {
        return to;
    }
    public Double getStep() {
        return step;
    }
    public int getColumnCount() {
// В данной модели два столбца
        return 4;
    }
    public int getRowCount() {
// Вычислить количество точек между началом и концом отрезка
// исходя из шага табулирования
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }
    public Object getValueAt(int row, int col) {
// Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
        double x = from + step*row;
        if (col==0) {
// Если запрашивается значение 1-го столбца, то это X
            return x;
        } else
            if (col == 1)
            {
// Если запрашивается значение 2-го столбца, то это значение
// многочлена
            Double result = 0.0;
// Вычисление значения в точке по схеме Горнера.
// Вспомнить 1-ый курс и реализовать
               for(int j = 0; j < coefficients.length; j++)
               {
                   result = result*x + coefficients[j];
               }
// ...
            return result;
        }
        else if(col == 2)
            {
                Double temp = 1.0;
                Double result = 0.0;
                for(int i = 0; i < coefficients.length; i++)
                {
                    result += (coefficients[i]*pow(x, coefficients.length-1-i));
                }
                return result;
            }
        else {
                int howManySteps = 0;
                for (Double i = from; i < to; i += step) {
                    howManySteps++;
                }
                    return Double.parseDouble(String.valueOf(getValueAt(row, 1)))-Double.parseDouble(String.valueOf(getValueAt(row, 2)));

            }
    }
    public String getColumnName(int col) {
        switch (col) {
            case 0:
// Название 1-го столбца
                return "Значение X";
            case 1:
// Название 2-го столбца
                return "Значение многочлена";
            case 2:
                return "Not Horner";
            default:
                return "Pазность полученных значений";
        }
    }
    public Class<?> getColumnClass(int col) {
// И в 1-ом и во 2-ом столбце находятся значения типа Double
        return Double.class;
    }
}
